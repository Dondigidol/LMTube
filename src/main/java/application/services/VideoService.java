package application.services;

import application.entities.Video;
import application.entities.VideoDetails;
import application.exceptions.VideoIdException;
import application.repositories.VideoDetailsRepository;
import application.repositories.VideoRepository;
import application.services.FFmpeg.FFmpegService;
import application.services.FFmpeg.Resolution;
import net.bytebuddy.description.type.TypeDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private FileService fileService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private SessionService sessionService;

    @Value("${storage.videos.temp.path}")
    private String videosTempPath;

    @Value("${storage.videos.path}")
    private String videosPath;

    public List<Video> upload(MultipartFile videoFile){
        try {
            String videoFileName = fileService.saveFile(videosTempPath, videoFile.getInputStream());
            List<Video> videos = new ArrayList<>();
            List<Resolution> resolutions = getStreamVideoResolutions();
            FFmpegService fFmpegService = new FFmpegService();

            Resolution resolution = fFmpegService.checkVideoResolution(videosTempPath, videoFileName);

            for (Resolution resolution1: resolutions){
                if (resolution.getHeight() >= resolution1.getHeight()){
                    Map<String, Object> result = fFmpegService.convert(resolution1.getWidth(), resolution1.getHeight(), videoFileName);
                    Video video = new Video();
                    video.setName(videoFileName);
                    video.setContentLength((long)result.get("length"));
                    video.setMimeType((String)result.get("mimeType"));
                    video.setResolution(resolution1.getHeight());
                    videos.add(video);
                }
            }
            return videos;
        } catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public InputStreamResource load(String videoFileName, int resolution){
        try {
            Path filePath = Paths.get(videosPath + "\\" + resolution + "p\\" + videoFileName);
            if (!Files.exists(filePath)) throw new VideoIdException("Видео с ID '" + videoFileName + "' не существует");

            InputStreamResource isr = new InputStreamResource(new FileInputStream(filePath.toFile()));

            Optional<VideoDetails> vd = videoDetailsRepository.findByFileNameAndResolution(videoFileName, resolution);
            if (vd.isPresent()){
                VideoDetails video = vd.get();
                if (!sessionService.isPresent(video.getId())) {
                    videoDetailsRepository.updateVideoViews(video.getId());
                    sessionService.addToViews(video.getId());
                }
            }


            return isr;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public Video getVideoInfo(String videoName, int resolution){
        Optional<Video> v = videoRepository.findByNameAndResolution(videoName, resolution);
        if (v.isPresent()){
            return v.get();
        } else throw new VideoIdException("Видеофайл с именем '" + videoName + "' и разрешением " + resolution+ "p не существует!");

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
                        Resolution res = new Resolution(Short.parseShort(width), Short.parseShort(height));
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
