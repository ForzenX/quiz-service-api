package engine.mapper;

import engine.dto.CreateQuizDto;
import engine.dto.QuizInfoDto;
import engine.entity.QuizEntity;
import engine.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

    public QuizInfoDto mapEntityToDto(QuizEntity quizEntity) {
        return QuizInfoDto
                .builder()
                .id(quizEntity.getId())
                .title(quizEntity.getTitle())
                .text(quizEntity.getText())
                .options(quizEntity.getOptions())
                .build();
    }

    public QuizEntity mapDtoToEntity(CreateQuizDto quizDto, UserEntity userEntity) {
        return QuizEntity
                .builder()
                .author(userEntity)
                .title(quizDto.getTitle())
                .text(quizDto.getText())
                .options(quizDto.getOptions())
                .answer(quizDto.getAnswer())
                .build();
    }

}
