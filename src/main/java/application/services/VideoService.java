package application.services;

import application.entities.Video;
import application.exceptions.VideoIdException;
import application.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private SessionService sessionService;

    @Value("${storage.videos.path}")
    private String videosPath;

    @Value("${storage.posters.path}")
    private String postersPath;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileServiceImpl fileService;

    public void saveOrUpdateVideo(Video video, MultipartFile videoFile, MultipartFile previewFile){
        try {
            String videoId = fileService.saveFile(videosPath, videoFile.getInputStream());
            video.getVideoDetails().setVideoFileId(videoId);
            video.getVideoDetails().setVideoMimeType(videoFile.getContentType());
            video.getVideoDetails().setVideoContentLength(videoFile.getSize());

            String previewId = fileService.saveFile(postersPath, previewFile.getInputStream());
            video.getVideoDetails().setPosterFileId(previewId);
            video.getVideoDetails().setPosterMimeType(previewFile.getContentType());
            video.getVideoDetails().setPosterContentLength(previewFile.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoRepository.save(video);
    }

    public void deleteVideo(Video video){
        String videoFileId = video.getVideoDetails().getVideoFileId();
        String previewFileId = video.getVideoDetails().getPosterFileId();

        fileService.deleteFile(videosPath, videoFileId);
        fileService.deleteFile(postersPath, previewFileId);

        videoRepository.delete(video);
    }

    public Iterable<Video> getAllVideo(){
        return videoRepository.findAll();
    }

    public Video getVideoById(long id){
        Optional<Video> v = videoRepository.findById(id);
        if (!v.isPresent()){
            throw new VideoIdException("The video with ID '" + id + "' doesn't exist");
        }
        return v.get();
    }

    public void deleteVideoById(long id){
        Optional<Video> v= videoRepository.findById(id);
        if (!v.isPresent()){
            throw new VideoIdException("Video with ID '" + id + "' doesn't exist");
        }
        deleteVideo(v.get());
    }

    public InputStream loadVideoFile(Video video){
        try {

            InputStream stream = fileService.loadFile(videosPath, video.getVideoDetails().getVideoFileId());

            if (!sessionService.isPresent(video.getId())) {
                videoRepository.updateVideoViews(video.getId());
                sessionService.addToViews(video.getId());
            }

            return stream;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public InputStream loadPosterFile(String posterFileId){
        try {
            InputStream stream = fileService.loadFile(postersPath, posterFileId);
            return stream;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }






}
