package application.entities;

public class File {

    private String id;

    private String mimeType;

    private long contentLength;

    public File(){

    }

    public File(String id, String mimeType, long contentLength){
        this.id = id;
        this.mimeType = mimeType;
        this.contentLength = contentLength;
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

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", contentLength=" + contentLength +
                '}';
    }
}
