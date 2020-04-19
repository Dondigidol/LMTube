package application.controllers.rest;

import application.entities.Video;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/video")
public class RestVideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping(value = "/stream/{id}")
    public ResponseEntity<?> loadVideoFileById(@PathVariable("id") long id){
        Video video= videoService.getVideoById(id);
        InputStreamResource resource = new InputStreamResource(videoService.loadVideoFile(video));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(video.getVideoDetails().getVideoContentLength());
        headers.set("Content-Type", video.getVideoDetails().getVideoMimeType());
        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/poster/{id}")
    public ResponseEntity<?> loadPosterFileById(@PathVariable("id") long id){
        Video video = videoService.getVideoById(id);
        InputStreamResource resource = new InputStreamResource(videoService.loadPosterFile(video.getVideoDetails().getPosterFileId()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(video.getVideoDetails().getPosterContentLength());
        headers.set("Content-Type", video.getVideoDetails().getPosterMimeType());
        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVideoById(@PathVariable long id){
        videoService.deleteVideoById(id);
        return new ResponseEntity<String>("Video with ID '" + id + "' was deleted", HttpStatus.OK);
    }


}
