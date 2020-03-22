package application.services;

import application.entities.Video;
import application.repositories.VideoRepository;
import application.repositories.VideoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoStorage videoStorage;

    public void saveVideo(Video video){
        videoRepository.save(video);
    }
    public Iterable<Video> getAllVideo(){
        return videoRepository.findAll();
    }

    public Optional<Video> getById(long id){
        return videoRepository.findById(id);
    }
    public void saveContent(Video video, InputStream stream){
        videoStorage.setContent(video, stream);
    }

    public InputStream getVideoContent (Video video){
        return videoStorage.getContent(video);
    }
}
