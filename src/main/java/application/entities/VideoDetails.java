package application.entities;


import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;

@Entity
@Table(name = "video_details")
public class VideoDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    @ContentId
    @Column(name = "file_id")
    private String fileId;

    @MimeType
    @Column(name = "mime_type")
    private String mimeType;

    @ContentLength
    @Column(name = "content_length")
    private long contentLength;

    @Column(name = "rating")
    private float rating;

    @Column(name = "is_commented")
    private boolean isCommented = false;



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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    @Override
    public String toString() {
        return "VideoDetails{" +
                "id=" + id +
                ", description=" + description +
                ", fileId='" + fileId + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", contentLength=" + contentLength +
                ", rating=" + rating +
                ", isCommented=" + isCommented +
                '}';
    }
}
