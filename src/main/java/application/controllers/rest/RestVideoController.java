package application.controllers.rest;

import application.entities.Poster;
import application.entities.Video;
import application.entities.VideoDetails;
import application.services.PosterService;
import application.services.VideoDetailsService;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/video")
public class RestVideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private PosterService posterService;

    @Autowired
    private VideoDetailsService videoDetailsService;

    @PostMapping("/upload")
    public ResponseEntity<VideoDetails> uploadVideo(@RequestParam("title") String title,
                                         @RequestParam("description") String description,
                                         @RequestParam("videoFile") MultipartFile videoFile,
                                         @RequestParam("posterFile") MultipartFile posterFile){


        List<Video> videos = videoService.upload(videoFile);
        Poster poster = posterService.upload(posterFile);

        VideoDetails videoDetails = new VideoDetails();
        videoDetails.setTitle(title);
        videoDetails.setDescription(description);
        videoDetails.setVideos(videos);
        videoDetails.setPoster(poster);
        videoDetailsService.save(videoDetails);
        return new ResponseEntity<>(videoDetails, HttpStatus.OK);
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

        return new ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK);

    }


}
