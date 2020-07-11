package application.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VideoEditRequest {

    @NotNull
    private long id;
    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

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

    @Override
    public String toString() {
        return "VideoEditRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
