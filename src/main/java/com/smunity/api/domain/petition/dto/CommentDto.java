package com.smunity.api.domain.petition.dto;

import com.smunity.api.domain.account.entity.User;
import com.smunity.api.domain.petition.entity.Comment;
import com.smunity.api.domain.petition.entity.Petition;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
public class CommentDto {
    private Long id;
    private Long author_id;
    private Long petition_id;
    private String content;
    private LocalDateTime create_date;
    private LocalDateTime modify_date;

    public Comment toEntity(User user, Petition petition) {
        return Comment.builder()
                .content(content)
                .author(user)
                .petition(petition)
                .build();
    }

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author_id(comment.getAuthor().getId())
                .petition_id(comment.getPetition().getId())
                .content(comment.getContent())
                .create_date(comment.getCreate_date())
                .modify_date(comment.getModify_date())
                .build();
    }
}
