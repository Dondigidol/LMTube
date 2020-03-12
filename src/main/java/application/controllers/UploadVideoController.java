package application.controllers;


import application.entities.Video;
import application.services.NameGeneratorService;
import application.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Controller
public class UploadVideoController {
    private static final String UPLOADPATH = "./videos/";

    private String videoId;
    private String videoFileName;

    @Autowired
    private NameGeneratorService nameGeneratorService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/add-video")
    public String uploadPage(){
        return "add-video";
    }

    @PostMapping("/uploadVideo")
    public String uploadVideo(@RequestParam("videoForUpload") MultipartFile file,
                              Model model){
        if (file.isEmpty()){
            model.addAttribute("message", "Please select a file to upload.");
            return "add-video";
        }

        String extension = nameGeneratorService.getExtension(file.getOriginalFilename());
        String fileId = nameGeneratorService.getUniqueName();
        this.videoId = fileId;
        String fileName = fileId + "." + extension;
        this.videoFileName = fileName;

        try {
            Path path = Paths.get(UPLOADPATH + fileName);
            Files.copy(file.getInputStream(), path);
        } catch (IOException e){
            e.printStackTrace();
        }
        model.addAttribute("message", "You successfully uploaded " + fileName);
        return "add-video";
    }

    @PostMapping("/add-video")
    public String saveVideo(@RequestParam("name") String name,
                            @RequestParam("description") String description,
                            @RequestParam(value = "isCommented", required = false) boolean isCommented,
                            @RequestParam(value = "isActive", required = false) boolean isActive,
                            Model model){
        if (videoId == null || videoFileName == null){
            model.addAttribute("message", "First, please upload a video file.");
            return "add-video";
        }
        Video video = new Video();
        video.setFileName(videoFileName);
        video.setName(name);
        video.setDescription(description);
        video.setCommented(isCommented);
        video.setActive(isActive);
        video.setDate(new Date());
        try {
            videoService.saveVideo(video);
            model.addAttribute("message", "Video successfully saved in repository!");
        } catch (Exception e){
            e.printStackTrace();
        }
        videoService.saveVideo(video);


        return "add-video";
    }

}
