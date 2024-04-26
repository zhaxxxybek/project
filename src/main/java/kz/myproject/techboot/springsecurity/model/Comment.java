package kz.myproject.techboot.springsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts.Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "size")
    private int size;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.size = 1000;
    }

    public String getTimeElapsed() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "несколько секунд назад";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return "КОММЕНТАРИЙ НАПИСАН " + minutes + " МИН НАЗАД";
        } else {
            long hours = seconds / 3600;
            return "КОММЕНТАРИЙ НАПИСАН " + hours + " Ч НАЗАД";
        }
    }
}
