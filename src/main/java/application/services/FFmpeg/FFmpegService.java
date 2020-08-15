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
import java.sql.SQLOutput;
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


        //long originalBitRate = checkBitRate(videosTempPath, filename);
        Resolution originalResolution = checkVideoResolution(videosTempPath, filename);
        long originalBitRate = calculateOriginalBitRate(originalResolution.getWidth(), originalResolution.getHeight(), width, height, checkBitRate(videosTempPath, filename));
        long targetBitRate = calculateBitRate(width, height);
        long bitRate;
        System.out.println("original bitrate: " + originalBitRate + ", target bitrate: " + targetBitRate);

        if (originalBitRate < targetBitRate){
            bitRate = originalBitRate;
            System.out.println("original bitrate");
        } else {
            bitRate = targetBitRate;
            System.out.println("target bitrate");
        }

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .overrideOutputFiles(true)
                .addOutput( resultFilePath.toString())
                .setFormat(videoFormat)
                .setVideoBitRate(bitRate)
                .setVideoFrameRate(25,1)
                //.addExtraArgs("-aspect", "16:9")
                .addExtraArgs("-lavfi", "[0:v]scale=ih*16/9:-1,boxblur=luma_radius=min(h\\,w)/20:luma_power=1:chroma_radius=min(cw\\,ch)/20:chroma_power=1[bg];[bg][0:v]overlay=(W-w)/2:(H-h)/2")
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
        FFmpegProbeResult probeResult = ffprobe.probe(path + filename);
        int width = 0;
        int height = 0;
        for (FFmpegStream stream1: probeResult.streams){
            if (stream1.height>0 && stream1.width>0){
                width = stream1.width;
                height = stream1.height;
            }
        }
        Resolution resolution = new Resolution();
        resolution.setWidth(width);
        resolution.setHeight(height);
        return resolution;
    }

    private long checkBitRate(String path, String filename) throws IOException{
        FFmpegProbeResult probeResult = ffprobe.probe(path + filename);
        long frame = 0;
        for (FFmpegStream stream: probeResult.streams){
            System.out.println("sd"+stream.);
            if (stream.bit_rate > frame) frame = stream.bit_rate;
        }

        return frame;
    }

    private long calculateBitRate(int width, int height){
        double quality = 0.1;
        int framesPerSecond = 25;
        if (width * height == 0){
            throw new NullPointerException("Video height or width is 0!");
        }

        long bitRate = (long)(quality * width * height * framesPerSecond);
        return bitRate;

    }

    private long calculateOriginalBitRate(int originalWidth,
                                          int originalHeight,
                                          int targetWidth,
                                          int targetHeight,
                                          long originalBitRate){

        int framesPerSec = 25;
        double k = (double)originalBitRate / (originalWidth * originalHeight) * framesPerSec;
        System.out.println(k);
        return (long)(k * targetWidth * targetHeight * framesPerSec);

    }




}
