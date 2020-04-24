package application.controllers;


import application.entities.Video;
import application.entities.VideoDetails;
import application.services.VideoService;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/video")
@CrossOrigin
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
                            @RequestParam("resolutions") ArrayList<Integer> resolutions,
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
        

        videoDetails.setSupportedResolutions(resolutions);
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
        List<Integer> list =  video.getVideoDetails().getSupportedResolutions();

        HashMap<Integer, String> videoSources = new HashMap<>();
        List<Integer> resolutions = video.getVideoDetails().getSupportedResolutions();

        for (Integer resolution : resolutions) {
            videoSources.put(resolution, "/api/video/stream/" + resolution + "/" + video.getId());
        }

        model.addAttribute("videoSources", videoSources);
        model.addAttribute("posterSource", "/api/video/poster/"+video.getId());
        model.addAttribute("videoMimeType", video.getVideoDetails().getVideoMimeType());
        model.addAttribute("title", video.getTitle());
        model.addAttribute("description", video.getVideoDetails().getDescription());
        return "video";
    }

}
