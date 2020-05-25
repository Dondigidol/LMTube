package application.controllers;

import application.entities.Poster;
import application.entities.User;
import application.entities.Video;
import application.entities.VideoDetails;
import application.payload.VideoUploadRequest;
import application.security.JwtTokenProvider;
import application.services.*;
import application.validators.UploadFormValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
@CrossOrigin
public class RestVideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private PosterService posterService;

    @Autowired
    private VideoDetailsService videoDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UploadFormValidator uploadFormValidator;

    @InitBinder
    protected void initBinderVideoUpload(WebDataBinder binder){
        binder.setValidator(uploadFormValidator);
    }

    @Autowired
    private JwtTokenProvider tokenProvider;


    @GetMapping
    public ResponseEntity<List<VideoDetails>> getVideos(){
        List<VideoDetails> videos = videoDetailsService.getVideosDetails();
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VideoDetails>> searchVideos(@RequestParam("title") String title){
        List<VideoDetails> videos = videoDetailsService.searchByTitle(title);
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDetails> getVideo(@PathVariable("id") long id){
        return new ResponseEntity<>(videoDetailsService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<?> getVideoStream(@PathVariable("id") String videoFileName,
                                            @RequestParam("res") int resolution){
        InputStreamResource resource = videoService.load(videoFileName, resolution);
        Video video = videoService.getVideoInfo(videoFileName, resolution);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", video.getMimeType());
        headers.set("Content-length", String.valueOf(video.getContentLength()));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<List<VideoDetails>> getRecommendations(@PathVariable("id") long id){
        List<VideoDetails> recommendedVideos = videoDetailsService.getRecommendations(id);

        return new ResponseEntity<>(recommendedVideos, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadForm(@Valid  VideoUploadRequest videoUploadRequest,
                                        BindingResult result)
                                       // Principal principal)
    {

        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null){
            return  errorMap;
        }

        //User user = userService.getUser(principal.getName());
        List<Video> videosList = videoService.upload(videoUploadRequest.getVideoFile());
        Poster poster = posterService.upload(videoUploadRequest.getPosterFile());

        VideoDetails videoDetails = new VideoDetails();
        videoDetails.setTitle(videoUploadRequest.getTitle());
        videoDetails.setDescription(videoUploadRequest.getDescription());
        videoDetails.setPoster(poster);
        videoDetails.setVideos(videosList);
        //videoDetails.setAuthor(user);
        videoDetailsService.save(videoDetails);
        return new ResponseEntity<>(videoDetails, HttpStatus.OK);


    }
}
