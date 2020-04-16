package application.entities;

import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;

@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "owner")
    private long owner;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "date")
    private Date date;

    @Column(name = "views")
    private long views;

    @Column(name = "tags")
    private HashSet<String> tags;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_detail_id")
    private VideoDetails videoDetails;

    public Video(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
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

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public VideoDetails getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoDetails videoDetails) {
        this.videoDetails = videoDetails;
    }


    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner=" + owner +
                ", isActive=" + isActive +
                ", date=" + date +
                ", views=" + views +
                ", tags=" + tags +
                ", videoDetails=" + videoDetails +
                '}';
    }
}
