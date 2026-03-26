package com.stylematch.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "analysis_answers")
public class AnalysisAnswer {

    public AnalysisAnswer() {}

    public AnalysisAnswer(UUID id, AnalysisResult analysisResult, Integer questionId, String answerValue) {
        this.id = id;
        this.analysisResult = analysisResult;
        this.questionId = questionId;
        this.answerValue = answerValue;
    }

    public static AnalysisAnswerBuilder builder() {
        return new AnalysisAnswerBuilder();
    }

    public static class AnalysisAnswerBuilder {
        private UUID id;
        private AnalysisResult analysisResult;
        private Integer questionId;
        private String answerValue;

        public AnalysisAnswerBuilder id(UUID id) { this.id = id; return this; }
        public AnalysisAnswerBuilder analysisResult(AnalysisResult analysisResult) { this.analysisResult = analysisResult; return this; }
        public AnalysisAnswerBuilder questionId(Integer questionId) { this.questionId = questionId; return this; }
        public AnalysisAnswerBuilder answerValue(String answerValue) { this.answerValue = answerValue; return this; }

        public AnalysisAnswer build() {
            return new AnalysisAnswer(id, analysisResult, questionId, answerValue);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public AnalysisResult getAnalysisResult() { return analysisResult; }
    public void setAnalysisResult(AnalysisResult analysisResult) { this.analysisResult = analysisResult; }
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public String getAnswerValue() { return answerValue; }
    public void setAnswerValue(String answerValue) { this.answerValue = answerValue; }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisResult analysisResult;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Column(name = "answer_value", nullable = false)
    private String answerValue;
}
