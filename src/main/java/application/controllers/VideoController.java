package application.controllers;

import application.entities.Video;
import application.repositories.VideoRepository;
import application.services.NameGeneratorService;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/video")
    public String getVideo(@RequestParam("id") String id,
                           Model model){
        Video video = videoService.getByVideoId(id);

        model.addAttribute("videoSource", "./videos/" + video.getFileName());
        model.addAttribute("name", video.getName());
        model.addAttribute("description", video.getDescription());

        return "video";
    }
}
