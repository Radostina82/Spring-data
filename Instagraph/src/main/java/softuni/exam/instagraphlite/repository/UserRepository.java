package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String username);

    @Query("SELECT u FROM User u" +
            " JOIN u.posts p" +
            " ORDER BY size(u.posts) Desc, u.id")
    List<User> findByUserPostOrderByCountOfPostId();
}
