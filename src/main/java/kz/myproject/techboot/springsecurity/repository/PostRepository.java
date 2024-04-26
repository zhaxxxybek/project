package kz.myproject.techboot.springsecurity.repository;

import kz.myproject.techboot.springsecurity.model.Posts.Post;
import kz.myproject.techboot.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);
    List<Post> findTop10ByOrderByLikesDesc();
    List<Post> findTop10ByOrderByCommentsSizeDesc();

}
