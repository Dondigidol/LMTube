package application.controllers;

import application.entities.*;
import application.exceptions.VideoIdException;
import application.payload.VideoUploadRequest;
import application.security.JwtTokenProvider;
import application.services.*;
import application.validators.UploadFormValidator;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

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
        List<VideoDetails> videos = videoDetailsService.gelAllVideos();
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/videos")
    public ResponseEntity<List<VideoDetails>> searchVideos(@RequestParam("title") String title,
                                                           @RequestParam("available") boolean isAvailable,
                                                           Principal principal){
        boolean authenticated = false;
        try {
            User user = userService.getUser(principal.getName());
            if (user != null){
                authenticated = true;
            }
        } catch (NullPointerException e){

        }

        if (!authenticated && !isAvailable){
            isAvailable = true;
        }

        List<VideoDetails> videos = videoDetailsService.getVideos(title, isAvailable);
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDetails> getVideo(@PathVariable("id") String varId,
                                                 Principal principal){


        boolean authenticated = false;

        Long id = null;

        try {
            id = Long.parseLong(varId);
            User user = userService.getUser(principal.getName());
            if (user != null) authenticated = true;

        } catch (NullPointerException e){}
        catch (NumberFormatException e){
            throw new VideoIdException("Видео не найдено, попробуйте воспользоваться поиском.");
        }
        return new ResponseEntity<>(videoDetailsService.getById(id, authenticated), HttpStatus.OK);
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

       // try {
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
            //LoggerService.log(Level.INFO, user.getUsername() + ": Video with ID '"+ videoDetails.getId() +"' was uploaded.");
            return new ResponseEntity<>(videoDetails, HttpStatus.OK);
       // } catch (Exception e){
        //    LoggerService.log(Level.ERROR, e.getMessage());
        //}

        //return null;

    }

    @PostMapping("/availability")
    public ResponseEntity<?> videoAvailability(@RequestParam ("id") long id,
                                          @RequestParam ("available") boolean isAvailable,
                                          Principal principal){

        boolean authenticated  = false;

        try {
            User user =  userService.getUser(principal.getName());
            if (user != null) authenticated = true;
            Role userRole = user.getRole();
            switch (userRole){
                case ADMINISTRATOR:
                case MODERATOR:
                    videoDetailsService.setAvailability(id, isAvailable);
                    break;
                case CREATOR:
                    VideoDetails videoDetails = videoDetailsService.getById(id, authenticated);
                    if (videoDetails.getAuthor().getUsername().equals(user.getUsername())){
                        videoDetailsService.setAvailability(id, isAvailable);
                    } else
                        return new ResponseEntity<>("Недостаточно полномочий!", HttpStatus.FORBIDDEN);
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e){
            String message = "Недостаточно полномочий!";
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        VideoDetails videoDetails = videoDetailsService.getById(id, authenticated);

        return new ResponseEntity<>(videoDetails, HttpStatus.OK);
    }
}
