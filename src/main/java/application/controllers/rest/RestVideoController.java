package application.controllers.rest;

import application.entities.Video;
import application.services.FileServiceImpl;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
public class RestVideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getContent(@PathVariable("id") long id){
        Optional<Video> v= videoService.getById(id);
        if (v.isPresent()){
            Video video = v.get();
            InputStreamResource resource = new InputStreamResource(videoService.loadVideoFile(video));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(video.getVideoDetails().getVideoContentLength());
            headers.set("Content-Type", video.getVideoDetails().getVideoMimeType());
            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        }
        return null;
    }

    @GetMapping(value = "/poster/{id}")
    public ResponseEntity<?> getPoster(@PathVariable("id") long id){
        Optional<Video> v = videoService.getById(id);
        if (v.isPresent()){
            Video video = v.get();
            InputStreamResource resource = new InputStreamResource(videoService.loadPosterFile(video.getVideoDetails().getPosterFileId()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(video.getVideoDetails().getPosterContentLength());
            headers.set("Content-Type", video.getVideoDetails().getPosterMimeType());
            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        }

        return null;
    }
}
