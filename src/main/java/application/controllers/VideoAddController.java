package application.controllers;


import application.entities.Video;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Controller
public class VideoAddController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/add-video")
    public String uploadPage(){
        return "add-video";
    }

    @PostMapping("/add-video")
    public String saveVideo(@RequestParam("videoForUpload") MultipartFile file,
                            @RequestParam("name") String name,
                            @RequestParam("description") String description,
                            @RequestParam(value = "isCommented", required = false) boolean isCommented,
                            @RequestParam(value = "isActive", required = false) boolean isActive,
                            Model model){
        if (file.isEmpty()){
            model.addAttribute("message", "First, please upload a video file.");
        }

        Video video = new Video();
        video.setName(name);
        video.setDescription(description);
        video.setCommented(isCommented);
        video.setActive(isActive);
        video.setDate(new Date());
        try {
            videoService.saveContent(video, file.getInputStream());
            video.setMimeType(file.getContentType());
        } catch (Exception e){
            e.printStackTrace();
        }
        videoService.saveVideo(video);
        return "add-video";
    }

}
