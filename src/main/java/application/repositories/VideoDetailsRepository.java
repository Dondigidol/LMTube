package application.repositories;

import application.entities.VideoDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoDetailsRepository extends CrudRepository<VideoDetails, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE VideoDetails SET views = views + 1 WHERE id = :id ")
    void incrementVideoViews(@Param("id") long id);

    @Query("SELECT vd FROM VideoDetails vd WHERE title LIKE Concat('%',:title,'%') AND available=:available")
    List<VideoDetails> getVideos(@Param("title") String title,
                                 @Param("available") boolean available);

    List<VideoDetails> findAll();

    //@Query("SELECT vd FROM VideoDetails vd WHERE id<>:id ORDER BY createdAt DESC Limit 20")
    default List<VideoDetails> findRecommendations(long id){
        return findTop20ByIdNotAndAvailableOrderByCreatedAtDesc(id, true);    }

    List<VideoDetails> findTop20ByIdNotAndAvailableOrderByCreatedAtDesc(long id, boolean available);

    @Query("SELECT vd FROM VideoDetails vd JOIN FETCH vd.videos v WHERE v.name=:name and v.resolution=:resolution")
    Optional<VideoDetails> findByFileNameAndResolution(@Param("name") String filename,
                                                       @Param("resolution") int resolution);

    @Query("SELECT vd FROM VideoDetails vd JOIN FETCH vd.author a WHERE a.username=:username")
    List<VideoDetails> findVideoDetailsByUser(@Param ("username") String username);

    @Transactional
    @Modifying
    @Query ("UPDATE VideoDetails SET available = :available WHERE id = :id ")
    void setAvailability(@Param("id") long id,
                                              @Param("available") boolean available);

}
