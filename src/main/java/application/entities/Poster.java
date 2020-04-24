package application.entities;

import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;

@Entity
@Table(name = "posters")
public class Poster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "content_length")
    private long contentLength;

    public Poster(){

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
