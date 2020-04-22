package application.entities;

import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Poster {

    @Id
    private String id;
    @MimeType
    private String mimeType;
    @ContentLength
    private long contentLength;

    public Poster(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
