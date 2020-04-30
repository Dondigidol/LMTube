package application.services;

import application.entities.Poster;
import application.entities.Video;
import application.entities.VideoDetails;
import application.exceptions.VideoIdException;
import application.repositories.VideoDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoDetailsService {

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    public void save(VideoDetails videoDetails){
        videoDetailsRepository.save(videoDetails);
    }

    public VideoDetails getById(long id){
        Optional<VideoDetails> vd = videoDetailsRepository.findById(id);
        if (vd.isPresent()){
            return vd.get();
        } else
            throw new VideoIdException("Видео с ID '" + id + "' не найдено!");

    }

    public List<VideoDetails> searchByTitle(String title){
        return videoDetailsRepository.searchByTitle(title);
    }
}
