package application.controllers.rest;

import application.entities.Video;
import application.services.FFmpeg.Resolution;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/video")
public class RestVideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping(value = "/stream/{resolution}/{id}")
    public ResponseEntity<?> loadVideoStream(@PathVariable("id") long id,
                                             @PathVariable("resolution") int resolution){

        Video video= videoService.getVideoById(id);
        InputStreamResource resource = new InputStreamResource(videoService.loadVideoFile(video, resolution));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", video.getVideoDetails().getVideoMimeType());
        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/streams/{id}")
    public ResponseEntity<Map<String, String>> loadStreamsPaths(@PathVariable ("id") long id){

        Map<String, String> resources = new HashMap<>();
        Video video = videoService.getVideoById(id);
        List<Integer> resolutions = video.getVideoDetails().getSupportedResolutions();
        for (Integer resolution : resolutions) {
            String url = "/api/video/stream/" + resolution + "/" + video.getId();
            resources.put(resolution.toString(), url);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
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

    @GetMapping("/resolutions")
    public ResponseEntity<List<Resolution>> getSupportedResolutions(){
        List<Resolution> resolutions= videoService.getStreamVideoResolutions();
        return new ResponseEntity<>(resolutions, HttpStatus.OK);
    }





}
