package application.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "video_details")
public class VideoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @JsonFormat(pattern = "dd.MM.yyyy")
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "views")
    private long views;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private List<Video> videos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "poster_id")
    private Poster poster;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    @PrePersist
    public void creating() {
        this.createdAt = new Date();
    }

}