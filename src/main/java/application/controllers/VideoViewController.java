package application.controllers;

import application.entities.Video;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VideoViewController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/video")
    public String getVideo(@RequestParam("id") long id,
                           Model model){
        Optional<Video> v = videoService.getById(id);
        if (v.isPresent()){
            Video video = v.get();
            model.addAttribute("videoSource", "http://localhost:8080/files/"+video.getId());
            model.addAttribute("mimeType", video.getMimeType());
            model.addAttribute("name", video.getName());
            model.addAttribute("description", video.getDescription());
        }




        return "video";
    }
}
