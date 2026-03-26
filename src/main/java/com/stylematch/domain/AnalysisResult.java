package com.stylematch.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "analysis_results")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "input_type", nullable = false)
    private String inputType;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_type")
    private ColorType colorType;

    @Enumerated(EnumType.STRING)
    private Undertone undertone;

    @Enumerated(EnumType.STRING)
    @Column(name = "contrast_level")
    private ContrastLevel contrastLevel;

    @Column(name = "depth")
    private String depth;

    @Column(name = "chroma")
    private String chroma;

    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnalysisAnswer> answers;

    public AnalysisResult() {}

    public AnalysisResult(UUID id, User user, String inputType, ColorType colorType, 
                          Undertone undertone, ContrastLevel contrastLevel, String depth, String chroma,
                          String rawData, LocalDateTime createdAt, List<AnalysisAnswer> answers) {
        this.id = id;
        this.user = user;
        this.inputType = inputType;
        this.colorType = colorType;
        this.undertone = undertone;
        this.contrastLevel = contrastLevel;
        this.depth = depth;
        this.chroma = chroma;
        this.rawData = rawData;
        this.createdAt = createdAt;
        this.answers = answers;
    }

    public static AnalysisResultBuilder builder() {
        return new AnalysisResultBuilder();
    }

    public static class AnalysisResultBuilder {
        private UUID id;
        private User user;
        private String inputType;
        private ColorType colorType;
        private Undertone undertone;
        private ContrastLevel contrastLevel;
        private String depth;
        private String chroma;
        private String rawData;
        private LocalDateTime createdAt;
        private List<AnalysisAnswer> answers;

        public AnalysisResultBuilder id(UUID id) { this.id = id; return this; }
        public AnalysisResultBuilder user(User user) { this.user = user; return this; }
        public AnalysisResultBuilder inputType(String inputType) { this.inputType = inputType; return this; }
        public AnalysisResultBuilder colorType(ColorType colorType) { this.colorType = colorType; return this; }
        public AnalysisResultBuilder undertone(Undertone undertone) { this.undertone = undertone; return this; }
        public AnalysisResultBuilder contrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; return this; }
        public AnalysisResultBuilder depth(String depth) { this.depth = depth; return this; }
        public AnalysisResultBuilder chroma(String chroma) { this.chroma = chroma; return this; }
        public AnalysisResultBuilder rawData(String rawData) { this.rawData = rawData; return this; }
        public AnalysisResultBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AnalysisResultBuilder answers(List<AnalysisAnswer> answers) { this.answers = answers; return this; }

        public AnalysisResult build() {
            return new AnalysisResult(id, user, inputType, colorType, undertone, contrastLevel, depth, chroma, rawData, createdAt, answers);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getInputType() { return inputType; }
    public void setInputType(String inputType) { this.inputType = inputType; }
    public ColorType getColorType() { return colorType; }
    public void setColorType(ColorType colorType) { this.colorType = colorType; }
    public Undertone getUndertone() { return undertone; }
    public void setUndertone(Undertone undertone) { this.undertone = undertone; }
    public ContrastLevel getContrastLevel() { return contrastLevel; }
    public void setContrastLevel(ContrastLevel contrastLevel) { this.contrastLevel = contrastLevel; }
    public String getDepth() { return depth; }
    public void setDepth(String depth) { this.depth = depth; }
    public String getChroma() { return chroma; }
    public void setChroma(String chroma) { this.chroma = chroma; }
    public String getRawData() { return rawData; }
    public void setRawData(String rawData) { this.rawData = rawData; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<AnalysisAnswer> getAnswers() { return answers; }
    public void setAnswers(List<AnalysisAnswer> answers) { this.answers = answers; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
