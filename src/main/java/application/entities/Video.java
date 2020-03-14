package application.entities;

import application.services.NameGeneratorService;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity

public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String videoId;
    private String fileName;
    private String name;
    private String description;
    private double rating;
    private long owner;
    private boolean isCommented;
    private boolean isActive;
    private Date date;

    public Long getId() {
        return id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean commented) {
        this.isCommented = commented;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return "Video{" +
                "id=" + this.id +
                ",videoId=" + this.videoId +
                ",filename=" + this.fileName +
                ",name=" + this.name +
                ",description=" + this.description +
                ",rating=" + this.rating +
                ",owner=" + this.owner +
                ",commenting=" + this.isCommented +
                ",active=" + this.isActive +
                ",date=" + this.date +
                "}";
    }
}
