package application.repositories;

import application.entities.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

}
