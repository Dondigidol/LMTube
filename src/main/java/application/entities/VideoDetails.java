package application.entities;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "video_details")
public class VideoDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "description")
    private String description;
    @Column(name = "video_file_id")
    private String videoFileId;
    @Column(name = "video_mime_type")
    private String videoMimeType;
    @Column(name = "video_content_length")
    private long videoContentLength;
    @Column(name = "poster_file_id")
    private String posterFileId;
    @Column(name = "poster_mime_type")
    private String posterMimeType;
    @Column(name = "poster_content_length")
    private long posterContentLength;
    @Column(name = "rating")
    private float rating;
    @Column(name = "is_commented")
    private boolean isCommented = false;
    @Column(name = "supported_resolutions")
    private String supportedResolutions;

    public VideoDetails(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoFileId() {
        return videoFileId;
    }

    public void setVideoFileId(String videoFileId) {
        this.videoFileId = videoFileId;
    }

    public String getPosterFileId() {
        return posterFileId;
    }

    public void setPosterFileId(String posterFileId) {
        this.posterFileId = posterFileId;
    }

    public String getPosterMimeType() {
        return posterMimeType;
    }

    public long getPosterContentLength() {
        return posterContentLength;
    }

    public void setPosterContentLength(long posterContentLength) {
        this.posterContentLength = posterContentLength;
    }

    public void setPosterMimeType(String posterMimeType) {
        this.posterMimeType = posterMimeType;
    }

    public String getVideoMimeType() {
        return videoMimeType;
    }

    public void setVideoMimeType(String videoMimeType) {
        this.videoMimeType = videoMimeType;
    }

    public long getVideoContentLength() {
        return videoContentLength;
    }

    public void setVideoContentLength(long videoContentLength) {
        this.videoContentLength = videoContentLength;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean commented) {
        isCommented = commented;
    }

    public List<Integer> getSupportedResolutions() {
        String[] resolutions = this.supportedResolutions.split(",");
        List<Integer> res = new ArrayList<>();
        int i = 0;
        for (String resolution : resolutions) {
            res.add(Integer.parseInt(resolutions[i]));
            i++;
        }
        return res;
    }

    public void setSupportedResolutions(List<Integer> supportedResolutions) {
        String resolutions="";
        for (int i = 0; i < supportedResolutions.size(); i++) {
            resolutions += supportedResolutions.get(i);
            if (i < supportedResolutions.size()-1) resolutions += ",";
        }
        this.supportedResolutions = resolutions;
    }

    @Override
    public String toString() {
        return "VideoDetails{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", videoFileId='" + videoFileId + '\'' +
                ", videoMimeType='" + videoMimeType + '\'' +
                ", videoContentLength=" + videoContentLength +
                ", posterFileId='" + posterFileId + '\'' +
                ", posterMimeType='" + posterMimeType + '\'' +
                ", posterContentLength=" + posterContentLength +
                ", rating=" + rating +
                ", isCommented=" + isCommented +
                ", supportedResolutions=" + supportedResolutions +
                '}';
    }
}
