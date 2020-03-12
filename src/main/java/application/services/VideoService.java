package application.services;

import application.entities.Video;
import application.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public Video saveVideo(Video video){
        return videoRepository.save(video);
    }
    public Iterable<Video> getAllVideo(){
        return videoRepository.findAll();
    }
}
