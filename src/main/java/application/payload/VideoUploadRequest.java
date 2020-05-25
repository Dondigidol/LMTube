package application.payload;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class VideoUploadRequest {

    @NotBlank(message = "Заполните заголовок")
    private String title;
    @NotBlank(message = "Заполинте описание")
    private String description;
    @NotBlank(message = "Выбирете видеофайл")
    private MultipartFile videoFile;
    @NotBlank(message = "Выбирете постер")
    private MultipartFile posterFile;

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

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }

    public MultipartFile getPosterFile() {
        return posterFile;
    }

    public void setPosterFile(MultipartFile posterFile) {
        this.posterFile = posterFile;
    }
}
