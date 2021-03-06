package application.services;

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

    public List<VideoDetails> gelAllVideos(){
        return videoDetailsRepository.findAll();
    }

    public VideoDetails getById(long id){
        Optional<VideoDetails> vd = videoDetailsRepository.findById(id);
        return vd.orElse(null);
    }

    public List<VideoDetails> getVideos(String title, boolean isAvailable){
        return videoDetailsRepository.getVideos(title, isAvailable);
    }

    public List<VideoDetails> getRecommendations(long id){
        return videoDetailsRepository.findRecommendations(id);
    }

    public List<VideoDetails> getUserVideoDetails(String username){
        return videoDetailsRepository.findVideoDetailsByUser(username);
    }

    public void setAvailability(long id, boolean available){
        videoDetailsRepository.setAvailability(id, available);
    }

    public void incrementViews(long videoId){
        videoDetailsRepository.incrementVideoViews(videoId);
    }

}
