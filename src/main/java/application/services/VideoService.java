package application.services;

import application.entities.Video;
import application.exceptions.VideoIdException;
import application.repositories.VideoRepository;
import application.services.FFmpeg.FFmpegService;
import application.services.FFmpeg.Resolution;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private SessionService sessionService;

    @Value("${storage.videos.path}")
    private String videosPath;
    @Value("${storage.posters.path}")
    private String postersPath;
    @Value ("${storage.videos.temp.path}")
    private String videosTempPath;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileServiceImpl fileService;

    public void saveOrUpdateVideo(Video video, MultipartFile videoFile, MultipartFile previewFile){
        try {

            String videoFileId = fileService.saveFile(videosTempPath, videoFile.getInputStream());
            video.getVideoDetails().setVideoFileId(videoFileId);
            video.getVideoDetails().setVideoMimeType(videoFile.getContentType());
            video.getVideoDetails().setVideoContentLength(videoFile.getSize());

            String posterFileId = fileService.saveFile(postersPath, previewFile.getInputStream());
            video.getVideoDetails().setPosterFileId(posterFileId);
            video.getVideoDetails().setPosterMimeType(previewFile.getContentType());
            video.getVideoDetails().setPosterContentLength(previewFile.getSize());

            Thread thread = new Thread();

            thread.start();
            thread.run();

            FFmpegService ffmpegService = new FFmpegService();
            Resolution resolution = ffmpegService.checkVideoResolution(videosTempPath, videoFileId);
            List<Resolution> resolutions = getStreamVideoResolutions();
            for (Resolution resolution1 : resolutions) {
                if (resolution.getHeight() >= resolution1.getHeight()){
                    ffmpegService.convert(resolution1.getWidth(), resolution1.getHeight(), videoFileId);
                }
            }

            //fileService.deleteFile(videosTempPath, videoFileId);


            videoRepository.save(video);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteVideo(Video video){

        try {
            String videoFileId = video.getVideoDetails().getVideoFileId();
            String previewFileId = video.getVideoDetails().getPosterFileId();

            fileService.deleteFile(videosPath, videoFileId);
            fileService.deleteFile(postersPath, previewFileId);

            videoRepository.delete(video);
        } catch (IOException e){
            e.printStackTrace();
        }

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

    public List<Resolution> getStreamVideoResolutions(){
        try {
            FileInputStream propertyFile = new FileInputStream(".\\config\\streamResolutions.cfg");
            BufferedReader reader = new BufferedReader(new InputStreamReader(propertyFile));
            ArrayList<Resolution> resolutions = new ArrayList<>();

            while (true){
                String value = reader.readLine();
                if (value == null) break;
                String[] resolution = value.split("x");
                if (resolution.length == 2){
                    String width = resolution[0].trim().replaceAll("[^\\d]", "");
                    String height = resolution[1].trim().replaceAll("[^\\d]", "");
                    if (width.length() > 0 && height.length() >0){
                        Resolution res = new Resolution(Integer.parseInt(width), Integer.parseInt(height));
                        resolutions.add(res);
                    }
                }
            }

            reader.close();
            propertyFile.close();
            return resolutions;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }






}
