package application.repositories;

import application.entities.Video;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    @Transactional
    @Modifying
    @Query("Update Video Set views = views + 1 Where id = :id ")
    void updateVideoViews(@Param("id") long id);


}
