
package kz.myproject.techboot.springsecurity.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Posts extends BaseModel {
    @Entity
    @Table(name = "posts")
    @Getter
    @Setter
    public static class Post extends BaseModel {

        @Column(name = "title")
        private String title;
        @Column(name = "content")
        private String content;
        @Column(name = "likes")
        private Integer likes = 0;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User author;
        @Column(name = "publish_date")
        private LocalDateTime publishDate;
        @Column(name = "photo_url")
        private String photoUrl;

        public Post() {
            this.publishDate = LocalDateTime.now();
        }
        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
        private List<Comment> comments = new ArrayList<>();

        public String getFormattedPublishDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            return publishDate.format(formatter);
        }



    }}