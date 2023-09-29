package com.smunity.api.domain.petition.service.impl;

import com.smunity.api.domain.account.domain.User;
import com.smunity.api.domain.account.repository.UserRepository;
import com.smunity.api.domain.petition.domain.Respond;
import com.smunity.api.domain.petition.domain.Petition;
import com.smunity.api.domain.petition.dto.RespondDto;
import com.smunity.api.domain.petition.repository.RespondRepository;
import com.smunity.api.domain.petition.repository.PetitionRepository;
import com.smunity.api.domain.petition.service.RespondService;
import com.smunity.api.global.config.security.JwtTokenProvider;
import com.smunity.api.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class RespondServiceImpl implements RespondService {
    public JwtTokenProvider jwtTokenProvider;
    public UserRepository userRepository;
    private PetitionRepository petitionRepository;
    private RespondRepository respondRepository;

    @Autowired
    public RespondServiceImpl(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, PetitionRepository petitionRepository, RespondRepository respondRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.petitionRepository = petitionRepository;
        this.respondRepository = respondRepository;
    }

    @Override
    public List<RespondDto> findAllAnswers() {
        List<RespondDto> respondDtoList = new ArrayList<>();
        List<Respond> respondList = respondRepository.findAll();
        for (Respond respond : respondList) {
            RespondDto respondDto = RespondDto.toDto(respond);
            respondDtoList.add(respondDto);
        }
        return respondDtoList;
    }

    @Override
    public RespondDto getAnswer(Long id) {
        Respond respond = respondRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));
        return RespondDto.toDto(respond);
    }

    @Override
    public RespondDto createAnswer(RespondDto respondDto, String token) {
        if (!jwtTokenProvider.validateToken(token))
            throw new CustomException(HttpStatus.UNAUTHORIZED);
        Petition petition = petitionRepository.findById(respondDto.getPetition_id())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));
        String username = jwtTokenProvider.getUsername(token);
        User user = userRepository.getByUsername(username);
        Respond respond = respondDto.toEntity(user, petition);
        Respond saveRespond = respondRepository.save(respond);
        RespondDto answerResponseDto = RespondDto.toDto(saveRespond);
        return answerResponseDto;
    }

    @Override
    public RespondDto changeAnswer(Long id, RespondDto respondDto, String token) {
        Respond respond = respondRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));
        if (!jwtTokenProvider.validateToken(token))
            throw new CustomException(HttpStatus.UNAUTHORIZED);
        if (!jwtTokenProvider.getUsername(token).equals(respond.getAuthor().getUsername()) && !jwtTokenProvider.getIsSuperuser(token))
            throw new CustomException(HttpStatus.FORBIDDEN);
        respond.setContent(respondDto.getContent());
        Respond changedRespond = respondRepository.save(respond);
        return RespondDto.toDto(changedRespond);
    }

    @Override
    public void deleteAnswer(Long id, String token) {
        Respond respond = respondRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NO_CONTENT));
        if (!jwtTokenProvider.validateToken(token))
            throw new CustomException(HttpStatus.UNAUTHORIZED);
        if (!jwtTokenProvider.getUsername(token).equals(respond.getAuthor().getUsername()) && !jwtTokenProvider.getIsSuperuser(token))
            throw new CustomException(HttpStatus.FORBIDDEN);
        respondRepository.delete(respond);
    }
}