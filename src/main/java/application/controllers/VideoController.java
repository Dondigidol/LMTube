package application.controllers;


import application.entities.Video;
import application.entities.VideoDetails;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/add-video")
    public String uploadPage(){
        return "add-video";
    }

    @PostMapping("/add-video")
    public String saveVideo(@RequestParam("videoForUpload") MultipartFile file,
                            @RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam(value = "isCommented", required = false) boolean isCommented,
                            @RequestParam(value = "isActive", required = false) boolean isActive,
                            Model model){
        if (file.isEmpty()){
            model.addAttribute("message", "First, please upload a video file.");
        }

        Video video = new Video();
        video.setTitle(title);
        VideoDetails videoDetails = new VideoDetails();
        video.setVideoDetails(videoDetails);
        video.getVideoDetails().setDescription(description);
        video.getVideoDetails().setCommented(isCommented);
        video.setActive(isActive);
        video.setDate(new Date());
        try {
            videoService.saveContent(video, file.getInputStream());
            video.getVideoDetails().setMimeType(file.getContentType());
        } catch (Exception e){
            e.printStackTrace();
        }
        videoService.saveVideo(video);
        return "add-video";
    }

    @GetMapping("/view")
    public String getVideo(@RequestParam("id") long id,
                           Model model){
        Optional<Video> v = videoService.getById(id);
        if (v.isPresent()){
            Video video = v.get();
            model.addAttribute("videoSource", "/api/video/"+video.getId());
            model.addAttribute("mimeType", video.getVideoDetails().getMimeType());
            model.addAttribute("name", video.getTitle());
            model.addAttribute("description", video.getVideoDetails().getDescription());
        }
        return "video";
    }

}
