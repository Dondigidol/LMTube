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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/video")
public class RestVideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoStorage videoStorage;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getContent(@PathVariable("id") long id){
        Optional<Video> v= videoService.getById(id);
        if (v.isPresent()){
            Video video = v.get();
            InputStreamResource resource = new InputStreamResource(videoService.getVideoContent(video));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(video.getVideoDetails().getContentLength());
            headers.set("Content-Type", video.getVideoDetails().getMimeType());
            return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
        }
        return null;
    }
}
