package com.smunity.api.domain.petition.dto;

import java.time.LocalDateTime;
import com.smunity.api.domain.account.entity.User;
import com.smunity.api.domain.petition.entity.Petition;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PetitionDto {
    private Long id;
    private Long author_id;
    private String subject;
    private String content;
    private Integer category;
    private Boolean anonymous;
    private LocalDateTime create_date;
    private LocalDateTime end_date;
    private LocalDateTime modify_date;
    private Integer status;

    public Petition toEntity(User user) {
        return Petition.builder()
                .subject(subject)
                .content(content)
                .category(category)
                .anonymous(anonymous)
                .end_date(end_date)
                .status(status)
                .author(user)
                .build();
    }

    public static PetitionDto of(Petition petition) {
        return PetitionDto.builder()
                .id(petition.getId())
                .author_id(petition.getAuthor().getId())
                .subject(petition.getSubject())
                .content(petition.getContent())
                .category(petition.getCategory())
                .create_date(petition.getCreate_date())
                .end_date(petition.getEnd_date())
                .modify_date(petition.getModify_date())
                .status(petition.getStatus())
                .build();
    }
}
