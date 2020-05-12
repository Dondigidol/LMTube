package application.controllers;


import application.entities.Poster;
import application.services.PosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/poster")
@CrossOrigin
public class RestPosterController {


    @Autowired
    private PosterService posterService;

    @PostMapping("/upload-poster")
    public ResponseEntity<Poster> uploadPosterFile(@RequestParam("posterFile") MultipartFile posterFile){
        Poster poster = posterService.upload(posterFile);
        return new ResponseEntity<>(poster, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPosterFile(@PathVariable("id") long posterId){

        Resource posterFile = posterService.load(posterId);
        Poster poster = posterService.getPoster(posterId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", poster.getMimeType());
        headers.set("Content-Length", String.valueOf(poster.getContentLength()));
        return new ResponseEntity<Object>(posterFile, headers, HttpStatus.OK);
    }

/*    @DeleteMapping("/{name}")
    public ResponseEntity<?> deletePosterFile(@PathVariable("name") String posterName){
        HashMap<String, Boolean> result = new HashMap<>();
        HttpStatus status;
        if (posterService.delete(posterName)){
            status = HttpStatus.OK;
            result.put("deleted", true);
        } else {
            status = HttpStatus.NOT_FOUND;
            result.put("deleted", false);
        }
        return new ResponseEntity<>(result, status);
    }*/

}
