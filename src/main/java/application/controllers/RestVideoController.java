package application.controllers;

import application.entities.Poster;
import application.entities.User;
import application.entities.Video;
import application.entities.VideoDetails;
import application.exceptions.VideoIdException;
import application.payload.VideoUploadRequest;
import application.security.JwtTokenProvider;
import application.services.*;
import application.validators.UploadFormValidator;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
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
    public ResponseEntity<List<VideoDetails>> getVideos(@RequestParam("title") String title,
                                                        @RequestParam("available") boolean isAvailable,
                                                        Principal principal){

        if (principal != null){
            try {
                User user = userService.getUser(principal.getName());
                switch (user.getRole()){
                    case ADMINISTRATOR:
                    case MODERATOR:
                        LoggerService.log(Level.INFO, "User '" + user.getUsername() + "' has loaded videos, with availability: " + isAvailable);
                        return new ResponseEntity<>(videoDetailsService.getVideos(title, isAvailable), HttpStatus.OK);
                    case CREATOR:
                        LoggerService.log(Level.INFO, "User '" + user.getUsername() + "' has loaded videos, with availability: true");
                       return new ResponseEntity<>(videoDetailsService.getVideos(title, true), HttpStatus.OK);
                }

            } catch (NullPointerException e){
                LoggerService.log(Level.WARN, "User '" + principal.getName() + "' has not permission to run this operation in special mode. Operation will run in simple mode");
                return new ResponseEntity<>(videoDetailsService.getVideos(title, true), HttpStatus.OK);
            }
        } else {
            LoggerService.log(Level.INFO, "Unauthorised user has loaded videos, with availability: true");
            return new ResponseEntity<>(videoDetailsService.getVideos(title, true), HttpStatus.OK);
        }

        return null;



/*        boolean authenticated = false;
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
        return new ResponseEntity<>(videos, HttpStatus.OK);*/
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDetails> getVideo(@PathVariable("id") String varId,
                                                 Principal principal){

        try {
            long id = Long.parseLong(varId);
            if (principal !=null){
                try{
                    User user = userService.getUser(principal.getName());
                    VideoDetails vd;
                    switch (user.getRole()){
                        case ADMINISTRATOR:
                        case MODERATOR: {
                            VideoDetails videoDetails = getVideo(id, user.getUsername(), true);
                            LoggerService.log(Level.INFO, "User '"+user.getUsername()+"' has loaded video with ID '"+id+"'");
                            return new ResponseEntity<>(videoDetails, HttpStatus.OK);
                        }
                        case CREATOR: {
                            VideoDetails videoDetails = getVideo(id, user.getUsername(), false);
                            LoggerService.log(Level.INFO, "User '"+user.getUsername()+"' has loaded video with ID '"+id+"'");
                            return new ResponseEntity<>(videoDetails, HttpStatus.OK);
                        }
                    }
                } catch (NullPointerException e){
                    VideoDetails vd = getVideo(id, null, false);
                    LoggerService.log(Level.WARN, "User '" + principal.getName() + "' has not permission to run this operation in special mode. Operation will run in simple mode");
                    return new ResponseEntity<>(vd, HttpStatus.OK);

                }
            } else {
                VideoDetails vd = getVideo(id, null, false);
                LoggerService.log(Level.INFO, "Unauthorised user loaded video with ID '" + id + "'");
                return new ResponseEntity<>(vd, HttpStatus.OK);
            }
        } catch (NumberFormatException e){
            LoggerService.log(Level.ERROR, "Incorrect video ID ('"+varId+"') format loading attempt");
            throw new VideoIdException("Видео с ID '" +varId+ "' не существует.");
        }

        return null;

/*        boolean authenticated = false;

        Long id = null;

        try {
            id = Long.parseLong(varId);
            User user = userService.getUser(principal.getName());
            if (user != null) authenticated = true;

        } catch (NullPointerException e){}
        catch (NumberFormatException e){
            throw new VideoIdException("Видео не найдено, попробуйте воспользоваться поиском.");
        }
        return new ResponseEntity<>(videoDetailsService.getById(id, authenticated), HttpStatus.OK);*/
    }

    private VideoDetails getVideo(long id, String username, boolean isModerator){
        try {
            VideoDetails videoDetails = videoDetailsService.getById(id);

            if (videoDetails.isAvailable() || isModerator) return videoDetails;
            else {
                if (videoDetails.getAuthor().getUsername().equals(username)) {
                    return videoDetails;
                }
                else {
                    throw new VideoIdException("Видео снято с публикации и недоступно для просмотра.");
                }
            }

        } catch (NullPointerException e){
            throw new VideoIdException("Видео с ID '" + id + "' не существует.");
        }
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
        LoggerService.log(Level.INFO, "Video file '"+videoFileName+"' with resolution '"+resolution+"' was loaded.");
        return new ResponseEntity<>(fsr, headers, HttpStatus.OK);

    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<List<VideoDetails>> getRecommendations(@PathVariable("id") String id){

        List<VideoDetails> recommendedVideos;
        try {
            long videoId = Long.parseLong(id);
            recommendedVideos = videoDetailsService.getRecommendations(videoId);
        } catch (NumberFormatException e){
            recommendedVideos = videoDetailsService.getRecommendations(-1);
        }

        LoggerService.log(Level.INFO, "Recommendations for video ID '"+id+"' were loaded.");
        return new ResponseEntity<>(recommendedVideos, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadForm(@Valid  VideoUploadRequest videoUploadRequest,
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

    @PostMapping("/availability")
    public ResponseEntity<?> setVideoAvailability(@RequestParam ("id") long id,
                                                  @RequestParam ("available") boolean isAvailable,
                                                  Principal principal){

        if (principal != null){
            try {
                User user = userService.getUser(principal.getName());
                switch (user.getRole()){
                    case ADMINISTRATOR:
                    case MODERATOR:
                        videoDetailsService.setAvailability(id, isAvailable);
                        break;
                    case CREATOR: {
                        LoggerService.log(Level.ERROR, "User '" + principal.getName() + "' has not permissions to run this operation.");
                        return new ResponseEntity<>("У вас недостаточно полномочий для выполнения этой операции!", HttpStatus.FORBIDDEN);
                    }
                }

            } catch (NullPointerException e){
                LoggerService.log(Level.ERROR, "User '"+principal.getName()+"' has not permissions to run this operation.");
                return new ResponseEntity<>("У вас недостаточно полномочий для выполнения этой операции!", HttpStatus.FORBIDDEN);
            }
        } else {
            LoggerService.log(Level.ERROR, "Unauthorised user has not permissions to run this operation.");
            return new ResponseEntity<>("У вас недостаточно полномочий для выполнения этой операции!", HttpStatus.FORBIDDEN);
        }
        LoggerService.log(Level.INFO, "Availability for video with ID '"+id+"' was changed to '" + isAvailable+ "' by '"+principal.getName()+"'");
        VideoDetails videoDetails = videoDetailsService.getById(id);
        return new ResponseEntity<>(videoDetails, HttpStatus.OK);
    }
}
