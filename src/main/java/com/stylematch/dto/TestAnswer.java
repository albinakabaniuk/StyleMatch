package com.stylematch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Represents an individual answer to a style questionnaire")
public class TestAnswer {

    public TestAnswer() {}

    public TestAnswer(Integer questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public static TestAnswerBuilder builder() {
        return new TestAnswerBuilder();
    }

    public static class TestAnswerBuilder {
        private Integer questionId;
        private String answer;

        public TestAnswerBuilder questionId(Integer questionId) { this.questionId = questionId; return this; }
        public TestAnswerBuilder answer(String answer) { this.answer = answer; return this; }

        public TestAnswer build() {
            return new TestAnswer(questionId, answer);
        }
    }

    @Schema(description = "The ID of the question answered", example = "1")
    @NotNull(message = "Question ID must not be null")
    private Integer questionId;

    @Schema(description = "The selected option (e.g., A, B, C, D)", example = "A")
    @NotBlank(message = "Answer cannot be blank")
    private String answer;

    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    @Override
    public String toString() {
        return "TestAnswer(questionId=" + questionId + ", answer=" + answer + ")";
    }
}
