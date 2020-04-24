package application.controllers.rest;


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
public class RestPosterController {


    @Autowired
    private PosterService posterService;

/*    @PostMapping("/upload")
    public String uploadPosterFile(@RequestParam("posterFile") MultipartFile posterFile){
        return posterService.upload(posterFile).getName();
    }*/

/*    @GetMapping("/{name}")
    public ResponseEntity<?> getPosterFile(@PathVariable("name") String posterName,
                                           HttpServletRequest request){

        HashMap<String, Object> poster = posterService.load(posterName);

        Resource resource = (Resource) poster.get("resource");
        String contentLength = (String) poster.get("contentLength");
        String mimeType = (String) poster.get("mimeType");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", mimeType);
        headers.set("Content-Length", contentLength);
        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
    }*/

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
