package application.services.FFmpeg;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.nut.NutDataInputStream;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FFmpegService{

    private String ffmpegPath;
    private String ffprobePath;
    private String videosTempPath;
    private String videoFormat;
    private String videosPath;

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    public FFmpegService(){
        try {
            FileInputStream fis = new FileInputStream("./config/environment.properties");

            Properties properties = new Properties();
            properties.load(fis);

            ffmpegPath = properties.getProperty("ffmpeg.path");
            ffprobePath = properties.getProperty("ffprobe.path");
            videosTempPath = properties.getProperty("storage.videos.temp.path");
            videoFormat = properties.getProperty("ffmpeg.video.format");
            videosPath = properties.getProperty("storage.videos.path");

            this.ffmpeg = new FFmpeg(ffmpegPath);
            this.ffprobe = new FFprobe(ffprobePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Map<String, Object> convert(int width, int height, String filename) throws IOException{
        Path path = Paths.get(videosPath + height + "p");
        if (!Files.exists(path)) new File(path.toString()).mkdir();

        Map<String, Object> result = new HashMap<>();

        FFmpegProbeResult probeResult = ffprobe.probe(videosTempPath + filename);

        Path resultFilePath = Paths.get(path.toString()+"\\"+filename);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .overrideOutputFiles(true)
                .addOutput( resultFilePath.toString())
                .setFormat(videoFormat)
                .setVideoResolution(width, height)
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        long length = Files.size(resultFilePath);

        result.put("mimeType", "video/mp4");
        result.put("length", length);

        return result;
    }

    public Resolution checkVideoResolution(String path, String filename) throws IOException{
        FFmpegProbeResult probeResult = ffprobe.probe(path + "\\" + filename);
        FFmpegStream stream = probeResult.streams.get(0);
        Resolution resolution = new Resolution();
        resolution.setWidth(stream.width);
        resolution.setHeight(stream.height);
        return resolution;



    }




}
