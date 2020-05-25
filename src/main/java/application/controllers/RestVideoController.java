package application.controllers;

import application.entities.Poster;
import application.entities.User;
import application.entities.Video;
import application.entities.VideoDetails;
import application.security.JwtTokenProvider;
import application.services.PosterService;
import application.services.UserService;
import application.services.VideoDetailsService;
import application.services.VideoService;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> uploadForm(@RequestParam ("title") String title,
                                        @RequestParam ("description") String description,
                                        @RequestParam (value = "videoFile") MultipartFile videoFile,
                                        @RequestParam (value = "posterFile") MultipartFile posterFile,
                                        HttpServletResponse response)
                                       // Principal principal)
    {

        Map<String, Object> errorsMap = new HashMap<>();
        ArrayList<String> errorsList = new ArrayList<>();

        System.out.println(response.getStatus());
/*        if (principal== null){
            errorsMap.put("error", "Вы не авторизованы");
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        }*/

        if (title.isEmpty()) errorsMap.put("title", "Вы не заполнили заголовок");
        if (description.isEmpty()) errorsMap.put("description", "Вы не заполнили описание");
        if (videoFile.isEmpty() || videoFile == null) errorsMap.put("videoFile", "Вы не выбрали видеофайл");
        if (posterFile.isEmpty() || posterFile == null) errorsMap.put("posterFile","Вы не выбрали постер");

        if (errorsMap.isEmpty()){
            try{
                //User user = userService.getUser(principal.getName());
                List<Video> videosList = videoService.upload(videoFile);
                Poster poster = posterService.upload(posterFile);

                VideoDetails videoDetails = new VideoDetails();
                videoDetails.setTitle(title);
                videoDetails.setDescription(description);
                videoDetails.setPoster(poster);
                videoDetails.setVideos(videosList);
                //videoDetails.setAuthor(user);
                videoDetailsService.save(videoDetails);
                return new ResponseEntity<>(videoDetails, HttpStatus.OK);
            } catch (NullPointerException ex){
                errorsList.add("Ошибка корректности данных: " + ex);
                System.out.println("Ошибка корректности данных: " + ex);
            } catch (Exception ex){
                errorsList.add("Непредвиденная ошибка: " + ex);
                System.out.println("Непредвиденная ошибка: " + ex);
            }
        }
        if (!errorsList.isEmpty()) errorsMap.put("error", errorsList);
        System.out.println(errorsMap);
        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);

    }
}
