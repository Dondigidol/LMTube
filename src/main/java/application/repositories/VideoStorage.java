package application.repositories;

import application.entities.Video;
import org.springframework.content.commons.repository.ContentStore;

public interface VideoStorage extends ContentStore<Video, String> {
}
