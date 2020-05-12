package application.repositories;

import application.entities.Video;
import application.entities.VideoDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    List<VideoDetails> findAll();

    //@Query("SELECT vd FROM VideoDetails vd WHERE id<>:id ORDER BY createdAt DESC Limit 20")
    default List<VideoDetails> findRecommendations(long id){
        return findTop20ByIdNotOrderByCreatedAtDesc(id);    }

    List<VideoDetails> findTop20ByIdNotOrderByCreatedAtDesc(long id);






}
