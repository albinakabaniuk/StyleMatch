package com.stylematch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylematch.domain.BodyShape;
import com.stylematch.domain.User;
import com.stylematch.domain.TestResult;
import com.stylematch.dto.AIAnalysisResult;
import com.stylematch.dto.BodyShapeResponse;
import com.stylematch.dto.BodyShapeTestRequest;
import com.stylematch.dto.TestAnswer;
import com.stylematch.repository.TestResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BodyShapeAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(BodyShapeAnalysisService.class);
    private final TestResultRepository testResultRepository;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    public BodyShapeAnalysisService(TestResultRepository testResultRepository, AIService aiService, ObjectMapper objectMapper) {
        this.testResultRepository = testResultRepository;
        this.aiService = aiService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public BodyShapeResponse analyzeBodyShape(User user, BodyShapeTestRequest request) {
        long start = System.currentTimeMillis();
        log.info("Analyzing body shape for user: {}", user.getEmail());
        String lang = request.getLanguage() != null ? request.getLanguage() : user.getPreferredLanguage();
        if (lang == null) lang = "en";

        List<TestAnswer> answers = request.getAnswers();

        // Prepare context for AI
        Map<String, String> answerMap = new HashMap<>();
        answers.forEach(a -> answerMap.put(
                String.valueOf(a.getQuestionId()),
                a.getAnswer()
        ));
        log.info("Built answerMap for BODY_SHAPE: {}", answerMap);

        // Call AI Service with isolation
        AIAnalysisResult aiResult;
        BodyShape shape;
        try {
            // DEBUG: Force failure by setting -DsimulateAIFailure=true in JVM options
            if (Boolean.getBoolean("simulateAIFailure")) {
                throw new RuntimeException("Simulated AI Failure");
            }
            aiResult = aiService.analyzeFashion(user, "BODY_SHAPE", answerMap, lang);
            shape = BodyShape.valueOf(aiResult.getResultType());
        } catch (Exception e) {
            log.warn("Body Shape AI Analysis failed for user {}: {}. Using local calculation fallback.", user.getEmail(), e.getMessage());
            shape = calculateBodyShape(answers);
            aiResult = AIAnalysisResult.builder()
                    .resultType(shape.name())
                    .summary("result.aiSummaryFallback")
                    .recommendations(getDefaultRecommendations(shape, lang))
                    .personalizedAdvice("result.aiOffline")
                    .build();
        }

        // Map result to Response
        BodyShapeResponse response = BodyShapeResponse.builder()
                .bodyShapeType(shape)
                .bodyShapeDisplayName(aiResult.getResultType() != null ? aiResult.getResultType() : shape.name())
                .explanation(aiResult.getSummary() != null ? aiResult.getSummary() : "No explanation available.")
                .recommendations(aiResult.getRecommendations() != null ? aiResult.getRecommendations() : List.of())
                .aiResult(aiResult)
                .personalizedAdvice(aiResult.getPersonalizedAdvice() != null ? aiResult.getPersonalizedAdvice() : "No advice available.")
                .palette(List.of())
                .season(aiResult.getSeason() != null ? aiResult.getSeason() : "N/A")
                .build();

        // Save result
        try {
            Map<String, Object> metadataMap = new HashMap<>();
            metadataMap.put("summary", response.getExplanation());
            metadataMap.put("advice", response.getPersonalizedAdvice());
            metadataMap.put("season", response.getSeason());
            metadataMap.put("palette", List.of());
            
            String jsonMetadata = objectMapper.writeValueAsString(metadataMap);
            
            TestResult testResult = TestResult.builder()
                    .user(user)
                    .testType("BODY_SHAPE")
                    .result(response.getBodyShapeType().name())
                    .metadata(jsonMetadata)
                    .summary(response.getExplanation())
                    .build();
            testResultRepository.save(testResult);
        } catch (Exception e) {
            log.error("Failed to save BodyShape history for user {}: {}", user.getEmail(), e.getMessage());
        }

        log.info("[PERF] body shape analysis done — {} ({}ms)",
                response.getBodyShapeType(), System.currentTimeMillis() - start);

        return response;
    }


    private BodyShape calculateBodyShape(List<TestAnswer> answers) {
        if (answers == null || answers.isEmpty()) return BodyShape.RECTANGLE;

        // Simple heuristic based on common patterns
        long aCount = answers.stream().filter(a -> "A".equalsIgnoreCase(a.getAnswer())).count(); // Shoulders/Broad
        long dCount = answers.stream().filter(a -> "D".equalsIgnoreCase(a.getAnswer())).count(); // Hips/Narrow

        if (aCount >= 2 && dCount >= 2) return BodyShape.HOURGLASS;
        if (dCount >= 3) return BodyShape.PEAR;
        if (aCount >= 3) return BodyShape.INVERTED_TRIANGLE;
        
        return BodyShape.RECTANGLE;
    }

    private List<String> getDefaultRecommendations(BodyShape shape, String lang) {
        boolean isUk = "uk".equalsIgnoreCase(lang);
        boolean isDe = "de".equalsIgnoreCase(lang);
        boolean isEs = "es".equalsIgnoreCase(lang);
        boolean isFr = "fr".equalsIgnoreCase(lang);
        boolean isKo = "ko".equalsIgnoreCase(lang);

        return switch (shape) {
            case HOURGLASS -> isUk ? List.of("Підкреслюйте талію", "Сукні з запахом", "V-подібні вирізи") :
                             isDe ? List.of("Betonen Sie Ihre Taille", "Wickelkleider", "V-Ausschnitte") :
                             isEs ? List.of("Resalta tu cintura", "Vestidos cruzados", "Escotes en V") :
                             isFr ? List.of("Soulignez votre taille", "Robes portefeuille", "Cols en V") :
                             isKo ? List.of("허리라인 강조", "랩 드레스", "V넥") :
                             List.of("Emphasize your waist", "Wrap dresses", "V-necks");
            case PEAR -> isUk ? List.of("Додайте об'єму плечам", "А-силует спідниць", "Масивні прикраси") :
                         isDe ? List.of("Volumen an den Schultern", "A-Linien-Röcke", "Auffällige Halsketten") :
                         isEs ? List.of("Añade volumen a los hombros", "Faldas de línea A", "Collares llamativos") :
                         isFr ? List.of("Ajoutez du volume aux épaules", "Jupes trapèze", "Colliers imposants") :
                         isKo ? List.of("어깨 볼륨 추가", "A라인 스커트", "스테이트먼트 목걸이") :
                         List.of("Add volume to shoulders", "A-line skirts", "Statement necklaces");
            case RECTANGLE -> isUk ? List.of("Створюйте криві поясами", "Пеплум топи", "Деталізація з рюшами") :
                             isDe ? List.of("Kurven mit Gürteln schaffen", "Schößchen-Tops", "Rüschendetails") :
                             isEs ? List.of("Crea curvas con cinturones", "Tops con péplum", "Detalles con volantes") :
                             isFr ? List.of("Créez des courbes avec des ceintures", "Tops à basques", "Détails froncés") :
                             isKo ? List.of("벨트로 곡선 만들기", "페플럼 상의", "셔링 디테일") :
                             List.of("Create curves with belts", "Peplum tops", "Ruched detailing");
            case INVERTED_TRIANGLE -> isUk ? List.of("Широкі штани", "V-подібні вирізи", "А-силует") :
                                     isDe ? List.of("Weite Hosen", "V-Ausschnitte", "A-Linien-Silhouetten") :
                                     isEs ? List.of("Pantalones de pierna ancha", "Escotes en V", "Siluetas de línea A") :
                                     isFr ? List.of("Pantalons larges", "Cols en V", "Silhouettes trapèze") :
                                     isKo ? List.of("와이드 팬츠", "V넥", "A라인 실루엣") :
                                     List.of("Wide-leg trousers", "V-necks", "A-line silhouettes");
            case APPLE -> isUk ? List.of("Завищена талія (ампір)", "Структуровані жакети", "Монохромні образи") :
                         isDe ? List.of("Empire-Taille", "Strukturierte Jacken", "Monochrome Looks") :
                         isEs ? List.of("Cintura imperio", "Chaquetas estructuradas", "Looks monocromáticos") :
                         isFr ? List.of("Tailles empire", "Vestes structurées", "Looks monochromes") :
                         isKo ? List.of("엠파이어 웨이스트", "구조적인 자켓", "모노크롬 룩") :
                         List.of("Empire waists", "Structured jackets", "Monochromatic looks");
        };
    }

}
