package application.controllers.rest;

import application.entities.Video;
import application.repositories.VideoStorage;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class VideoRestController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoStorage videoStorage;

    @GetMapping(value = "/files/{id}")
    public ResponseEntity<?> getContent(@PathVariable("id") long id){
        Optional<Video> v= videoService.getById(id);
        if (v.isPresent()){
            Video video = v.get();
            InputStreamResource resource = new InputStreamResource(videoService.getVideoContent(video));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(video.getContentLength());
            headers.set("Content-Type", video.getMimeType());
            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        }
        return null;
    }
}
