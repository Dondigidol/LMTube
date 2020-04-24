package application.entities;

import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "resolution")
    private int resolution;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "content_length")
    private long contentLength;

    public Video(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
}
