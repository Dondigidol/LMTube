package application.controllers;

import application.entities.*;
import application.payload.VideoUploadRequest;
import application.security.JwtTokenProvider;
import application.services.*;
import application.validators.UploadFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.jar.JarOutputStream;

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

    @GetMapping("/videos")
    public ResponseEntity<List<VideoDetails>> searchVideos(@RequestParam("title") String title,
                                                           @RequestParam("available") boolean isAvailable){
        List<VideoDetails> videos = videoDetailsService.getVideos(title, isAvailable);
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDetails> getVideo(@PathVariable("id") long id){
        return new ResponseEntity<>(videoDetailsService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/stream/{resolution}/{id}")
    public ResponseEntity<?> getVideoStream(@PathVariable("resolution") int resolution,
                                            @PathVariable("id") String videoFileName){


        Video video = videoService.getVideoInfo(videoFileName, resolution);
        videoService.incrementView(videoFileName, resolution);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", video.getMimeType());
        headers.set("Content-length", String.valueOf(video.getContentLength()));

        FileSystemResource fsr =  new FileSystemResource(new File(videoService.getPath(videoFileName, resolution)));

        return new ResponseEntity<>(fsr, headers, HttpStatus.OK);

    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<List<VideoDetails>> getRecommendations(@PathVariable("id") long id){
        List<VideoDetails> recommendedVideos = videoDetailsService.getRecommendations(id);

        return new ResponseEntity<>(recommendedVideos, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadForm(@Valid  VideoUploadRequest videoUploadRequest,
                                        BindingResult result,
                                        Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null){
            return  errorMap;
        }

        User user = userService.getUser(principal.getName());
        List<Video> videosList = videoService.upload(videoUploadRequest.getVideoFile());
        Poster poster = posterService.upload(videoUploadRequest.getPosterFile());

        VideoDetails videoDetails = new VideoDetails();
        videoDetails.setTitle(videoUploadRequest.getTitle());
        videoDetails.setDescription(videoUploadRequest.getDescription());
        videoDetails.setPoster(poster);
        videoDetails.setVideos(videosList);
        videoDetails.setAuthor(user);
        videoDetailsService.save(videoDetails);
        return new ResponseEntity<>(videoDetails, HttpStatus.OK);


    }

    @PostMapping("/availability")
    public ResponseEntity<?> videoAvailability(@RequestParam ("id") long id,
                                          @RequestParam ("available") boolean isAvailable,
                                          Principal principal){


        User user =  userService.getUser(principal.getName());
        Role userRole = user.getRole();


        switch (userRole){
            case ADMINISTRATOR:
            case MODERATOR:
                videoDetailsService.setAvailability(id, isAvailable);
                break;
            case CREATOR:
                VideoDetails videoDetails = videoDetailsService.getById(id);
                if (videoDetails.getAuthor().getUsername().equals(user.getUsername())){
                    videoDetailsService.setAvailability(id, isAvailable);
                } else
                    return new ResponseEntity<>("Недостаточно полномочий!", HttpStatus.FORBIDDEN);
                break;
            default:
                break;
        }

        return null;
    }
}
