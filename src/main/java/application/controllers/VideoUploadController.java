package application.controllers;

import application.entities.Poster;
import application.entities.User;
import application.entities.Video;
import application.entities.VideoDetails;
import application.payload.VideoUploadRequest;
import application.services.*;
import application.validators.UploadFormValidator;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/video")
@CrossOrigin
public class VideoUploadController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PosterService posterService;

    @Autowired
    private VideoDetailsService videoDetailsService;

    @Autowired
    private UploadFormValidator uploadFormValidator;

    @InitBinder
    protected void initBinderVideoUpload(WebDataBinder binder){
        binder.setValidator(uploadFormValidator);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadForm(@Valid VideoUploadRequest videoUploadRequest,
                                        BindingResult result,
                                        Principal principal){

        try {
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
            LoggerService.log(Level.INFO, user.getUsername() + ": Video with ID '"+ videoDetails.getId() +"' was uploaded.");
            return new ResponseEntity<>(videoDetails, HttpStatus.OK);
        } catch (NullPointerException e){
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return null;

    }
}
