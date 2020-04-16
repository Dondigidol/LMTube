package application.controllers;

import application.entities.Video;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/")
    public String index(Model model) {
        Iterable<Video> videoList = videoService.getAllVideo();
        model.addAttribute("postersUrl", "/api/video/poster/");
        model.addAttribute("videos", videoList);
        return "main";
    }
}
