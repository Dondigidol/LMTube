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
    public String saveVideo(@RequestParam("videoFile") MultipartFile videoFile,
                            @RequestParam("posterFile") MultipartFile posterFile,
                            @RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam(value = "isCommented", required = false) boolean isCommented,
                            @RequestParam(value = "isActive", required = false) boolean isActive,
                            Model model){
        if (videoFile.isEmpty()){
            model.addAttribute("message", "First, please upload a video file.");
            return "add-video";
        }

        VideoDetails videoDetails = new VideoDetails();

        videoDetails.setDescription(description);
        videoDetails.setCommented(isCommented);

        Video video = new Video();

        video.setTitle(title);
        video.setActive(isActive);
        video.setDate(new Date());
        video.setVideoDetails(videoDetails);

        videoService.saveOrUpdateVideo(video, videoFile, posterFile);

        return "add-video";
    }

    @GetMapping("/view")
    public String getVideo(@RequestParam("id") long id,
                           Model model){
        Video video = videoService.getVideoById(id);
        model.addAttribute("videoSource", "/api/video/stream/"+video.getId());
        model.addAttribute("posterSource", "/api/video/poster/"+video.getId());
        model.addAttribute("videoMimeType", video.getVideoDetails().getVideoMimeType());
        model.addAttribute("title", video.getTitle());
        model.addAttribute("description", video.getVideoDetails().getDescription());
        return "video";
    }

}
