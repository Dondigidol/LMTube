package application.repositories;

import application.entities.Video;
import application.entities.VideoDetails;
import org.springframework.content.commons.repository.ContentStore;

public interface VideoStorage extends ContentStore<VideoDetails, String> {
}
