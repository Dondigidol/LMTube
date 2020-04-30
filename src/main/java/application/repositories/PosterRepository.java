package application.repositories;

import application.entities.Poster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosterRepository extends CrudRepository<Poster, Long> {
}
