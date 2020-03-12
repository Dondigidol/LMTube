package application.controllers;

import application.repositories.VideoRepository;
import application.services.NameGeneratorService;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VideoController {

    @GetMapping("/video")
    public String getVideo(@RequestParam(required = false) Long id,
                           Model model){

        model.addAttribute("id", String.valueOf(id));
        return "video";
    }



}
