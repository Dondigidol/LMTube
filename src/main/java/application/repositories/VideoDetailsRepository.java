package application.repositories;

import application.entities.Video;
import application.entities.VideoDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VideoDetailsRepository extends CrudRepository<VideoDetails, Long> {

    @Transactional
    @Modifying
    @Query("Update VideoDetails Set views = views + 1 Where id = :id ")
    void updateVideoViews(@Param("id") long id);

    @Query("Select v From VideoDetails v Where title Like Concat('%',:title,'%')")
    List<VideoDetails> searchByTitle(@Param("title") String title);


}
