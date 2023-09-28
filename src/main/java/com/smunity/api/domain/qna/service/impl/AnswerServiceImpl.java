package com.smunity.api.domain.qna.service.impl;

import com.smunity.api.domain.account.domain.User;
import com.smunity.api.domain.account.repository.UserRepository;
import com.smunity.api.domain.qna.domain.Answer;
import com.smunity.api.domain.qna.domain.Question;
import com.smunity.api.domain.qna.dto.AnswerDto;
import com.smunity.api.domain.qna.dto.QuestionDto;
import com.smunity.api.domain.qna.repository.AnswerRepository;
import com.smunity.api.domain.qna.repository.QuestionRepository;
import com.smunity.api.domain.qna.service.AnswerService;
import com.smunity.api.global.config.security.JwtTokenProvider;
import com.smunity.api.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    public JwtTokenProvider jwtTokenProvider;
    public UserRepository userRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public List<AnswerDto> findAllAnswers() {
        return null;
    }

    @Override
    public AnswerDto getAnswer(Long id) {
        return null;
    }

    @Override
    public AnswerDto createAnswer(AnswerDto answerDto, String token) {
        if (!jwtTokenProvider.validateToken(token))
            throw new CustomException(HttpStatus.UNAUTHORIZED);
        Question question = questionRepository.findById(answerDto.getQuestion_id())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND));
        String username = jwtTokenProvider.getUsername(token);
        User user = userRepository.getByUsername(username);
        Answer answer = answerDto.toEntity(user, question);
        Answer saveAnswer = answerRepository.save(answer);
        AnswerDto answerResponseDto = AnswerDto.toDto(saveAnswer);
        return answerResponseDto;
    }

    @Override
    public AnswerDto changeAnswer(Long id, AnswerDto answerDto, String token) {
        return null;
    }

    @Override
    public void deleteAnswer(Long id, String token) {

    }
}