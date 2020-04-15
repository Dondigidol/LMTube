package application.services;

import application.entities.Video;
import application.repositories.VideoRepository;
import application.repositories.VideoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

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
        videoStorage.setContent(video.getVideoDetails(), stream);
    }

    public InputStream getVideoContent (Video video){
        return videoStorage.getContent(video.getVideoDetails());
    }
}
