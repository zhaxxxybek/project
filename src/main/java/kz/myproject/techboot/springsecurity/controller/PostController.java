package kz.myproject.techboot.springsecurity.controller;

import kz.myproject.techboot.springsecurity.Service.PostService;
import kz.myproject.techboot.springsecurity.model.Comment;
import kz.myproject.techboot.springsecurity.model.Posts;
import kz.myproject.techboot.springsecurity.model.User;
import kz.myproject.techboot.springsecurity.repository.PostRepository;
import kz.myproject.techboot.springsecurity.repository.UserRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final UserRepostitory userRepository;

    @Autowired
    public PostController(PostRepository postRepository, PostService postService, UserRepostitory userRepository) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{postId}/uploadPhoto")
    @PreAuthorize("isAuthenticated()")
    public String uploadPhoto(@PathVariable("postId") Long postId, @RequestParam("photoUrl") String photoUrl) {
        Posts.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        post.setPhotoUrl(photoUrl);
        postRepository.save(post);
        return "redirect:/posts/all";
    }

    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Posts.Post());
        return "create_post";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createPost(@AuthenticationPrincipal User user, Posts.Post post) {
        post.setAuthor(user);
        postRepository.save(post);
        return "redirect:/posts/all";
    }

    @GetMapping("/all")
    public String showAllPosts(Model model) {
        List<Posts.Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "all_posts";
    }

    @GetMapping("/{postId}/comments")
    public String showPostComments(@PathVariable("postId") Long postId, Model model) {
        Posts.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        Comment comment = new Comment();
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        return "post_comments";
    }

    @PostMapping("/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public String addCommentToPost(@AuthenticationPrincipal User user,
                                   @PathVariable("postId") Long postId,
                                   @ModelAttribute("comment") Comment comment) {
        Posts.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        comment.setPost(post);
        comment.setAuthor(user);
        post.getComments().add(comment);
        postRepository.save(post);
        return "redirect:/posts/all";
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public String likePost(@PathVariable("postId") Long postId) {
        Posts.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
        return "redirect:/posts/all";
    }

    @PostMapping("/{postId}/unlike")
    @PreAuthorize("isAuthenticated()")
    public String unlikePost(@PathVariable("postId") Long postId) {
        Posts.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
            postRepository.save(post);
        }
        return "redirect:/posts/all";
    }

    @GetMapping("/{postId}/activity")
    public String showPostActivity(@PathVariable("postId") Long postId, Model model) {
        Posts.Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        List<Posts.Post> topPostsByLikes = postRepository.findTop10ByOrderByLikesDesc();
        List<Posts.Post> topPostsByComments = postRepository.findTop10ByOrderByCommentsSizeDesc();
        int likesCount = post.getLikes();
        int commentsCount = post.getComments().size();
        model.addAttribute("post", post);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("commentsCount", commentsCount);
        model.addAttribute("topPostsByLikes", topPostsByLikes);
        model.addAttribute("topPostsByComments", topPostsByComments);
        return "post_activity";
    }
}
