package application.controllers;

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
@CrossOrigin
public class RestVideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private PosterService posterService;

    @Autowired
    private VideoDetailsService videoDetailsService;

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

    @PostMapping("/upload")
    public ResponseEntity<VideoDetails> uploadVideo(@RequestBody VideoDetails videoDetails){
        videoDetailsService.save(videoDetails);
        return new ResponseEntity<>(videoDetails, HttpStatus.OK);
    }

    @PostMapping("/upload-video")
    public ResponseEntity<List<Video>> uploadVideoFile(@RequestParam("videoFile") MultipartFile videoFile){
        List<Video> videos = videoService.upload(videoFile);
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

        return new ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<List<VideoDetails>> getRecommendations(@PathVariable("id") long id){
        List<VideoDetails> recommendedVideos = videoDetailsService.getRecommendations(id);

        return new ResponseEntity<>(recommendedVideos, HttpStatus.OK);
    }


}
