package kz.myproject.techboot.springsecurity.Service;

import kz.myproject.techboot.springsecurity.model.Posts;
import kz.myproject.techboot.springsecurity.model.User;
import kz.myproject.techboot.springsecurity.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Posts.Post> getPostsByUser(User user) {
        return postRepository.findByAuthor(user);
    }



}
