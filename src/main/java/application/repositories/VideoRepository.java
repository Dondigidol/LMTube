package application.repositories;

import application.entities.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    Optional<Video> findByNameAndResolution(String name, int resolution);
}
