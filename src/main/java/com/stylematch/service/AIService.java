package com.stylematch.service;

import com.stylematch.domain.User;
import com.stylematch.dto.AIAnalysisResult;
import com.stylematch.dto.TestAnswer;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);

    @Value("${app.openai.api-key:demo}")
    private String apiKey;

    private static final Map<Integer, String> COLOR_TYPE_QUESTIONS = Map.of(
        1, "Natural skin tone?",
        2, "Natural hair color?", 
        3, "Natural eye color?",
        4, "Jewelry metal preference?",
        5, "Contrast level?",
        6, "Sun exposure reaction?",
        7, "Vein color on wrist?",
        8, "Natural lip tone?"
    );

    private static final Map<Integer, Map<String, String>> COLOR_TYPE_ANSWERS = Map.of(
        1, Map.of("A", "Fair / Porcelain", "B", "Medium / Neutral", "C", "Deep / Rich"),
        2, Map.of("A", "Light Blonde / Red", "B", "Ashy Brown / Black", "C", "Golden Brown / Auburn"),
        3, Map.of("A", "Blue / Gray", "B", "Green / Hazel", "C", "Brown / Black"),
        4, Map.of("A", "Sun-kissed Gold", "B", "Icy Silver / Platinum", "C", "Soft Rose Gold / Both"),
        5, Map.of("A", "Low (Soft & Delicate)", "B", "Medium (Balanced)", "C", "High (Striking & Dramatic)"),
        6, Map.of("A", "Burns easily / Turns pink", "B", "Tans easily / Turns golden"),
        7, Map.of("A", "Blue / Purple (Cool)", "B", "Green / Olive (Warm)", "C", "Mixed / Neutral"),
        8, Map.of("A", "Cool Pink / Mauve", "B", "Warm Peach / Coral")
    );

    private static final Map<Integer, String> BODY_SHAPE_QUESTIONS = Map.of(
        1, "What is the widest part of your frame?",
        2, "How would you describe your waistline?",
        3, "Look at your hip curve...",
        4, "Your shoulder line is..."
    );

    private static final Map<Integer, Map<String, String>> BODY_SHAPE_ANSWERS = Map.of(
        1, Map.of("A", "Shoulders", "B", "Bust", "C", "Waist", "D", "Hips", "E", "Roughly equal"),
        2, Map.of("A", "Very defined / Narrow", "B", "Slightly defined", "C", "Straight / Athletic", "D", "Wide / Full", "E", "High waist"),
        3, Map.of("A", "Rounded and full", "B", "Straight and slim", "C", "Wider than shoulders", "D", "Narrower than shoulders", "E", "Average"),
        4, Map.of("A", "Broad and strong", "B", "Square / Piercing", "C", "Soft / Sloped", "D", "Average", "E", "Petite / Narrow")
    );

    public interface FashionAIProvider {
        @SystemMessage("""
            You are a professional color analyst and stylist using the advanced 12-season color system. 
            Your task is to accurately analyze a user's appearance based on their answers and generate a complete, personalized result.
            
            Evaluate holistically:
            - Temperature (cool vs warm)
            - Depth (light / medium / deep)
            - Chroma (soft/muted vs clear/bright)
            - Contrast level
            
            12 SEASON SYSTEM:
            Select ONLY ONE: 
            Winter (Deep, Cool, Bright), Summer (Light, Cool, Soft), Spring (Light, Warm, Bright), Autumn (Deep, Warm, Soft).
            
            ========================================
            TASK REQUIREMENTS
            ========================================
            - Respond strictly in: {{language}}
            - Return ONLY valid JSON.
            - Use physical data (Age: {{age}}, Height: {{height}}cm, Weight: {{weight}}kg, Gender: {{gender}}) to refine advice.
            - Generate a detailed IMAGE PROMPT for a fashion portrait matching the detected type.
            
            JSON FORMAT:
            {
              "resultType": "string (e.g. BRIGHT_SPRING, DEEP_WINTER)",
              "season": "WINTER | SPRING | SUMMER | AUTUMN",
              "undertone": "COOL | WARM",
              "contrastLevel": "LOW | MEDIUM | HIGH",
              "depth": "LIGHT | MEDIUM | DEEP",
              "chroma": "SOFT | CLEAR",
              "summary": "Detailed, professional explanation of why this type was selected.",
              "palette": ["emerald", "terracotta", "...8-12 color names..."],
              "recommendations": ["Wear X", "Avoid Y", "Style tips..."],
              "personalizedAdvice": "Practical fashion advice including contrast usage and outfit tips.",
              "imagePrompt": "A realistic portrait of a [gender] with [undertone] skin, [hair], [eyes], and [contrast] contrast. Wearing [palette colors]. Fashion photography style."
            }
            """)
        @UserMessage("Analyze styling for user {{name}} with answers {{answers}} in {{languageName}} (code: {{languageCode}})")
        AIAnalysisResult analyzeStyling(
            @V("name") String name,
            @V("age") Integer age,
            @V("gender") String gender,
            @V("height") Double height,
            @V("weight") Double weight,
            @V("testType") String testType,
            @V("answers") String answers,
            @V("languageName") String languageName,
            @V("languageCode") String languageCode
        );
    }

    public AIAnalysisResult analyzeFashion(User user, String testType, List<TestAnswer> answers, String language) {
        if ("demo".equalsIgnoreCase(apiKey)) {
            log.info("API Key is 'demo', returning mock AI result for {}", testType);
            return getMockResult(testType, language, user, answers);
        }
        return analyze(user, testType, answers, language);
    }

    public AIAnalysisResult analyzeFashion(User user, String testType, java.util.Map<String, String> answerMap, String language) {
        List<TestAnswer> answers = answerMap.entrySet().stream()
            .map(e -> TestAnswer.builder()
                .questionId(tryParse(e.getKey()))
                .answer(e.getValue())
                .build())
            .collect(Collectors.toList());
        return analyzeFashion(user, testType, answers, language);
    }

    private Integer tryParse(String key) {
        try { return Integer.parseInt(key); }
        catch (Exception e) { return null; }
    }

    public AIAnalysisResult analyze(User user, String testType, List<TestAnswer> answers, String language) {
        String normalizedLang = normalizeLanguage(language);
        String langName = getLanguageName(normalizedLang);

        if ("demo".equalsIgnoreCase(apiKey)) {
            log.info("API Key is 'demo', returning mock AI result for {} in {}", testType, langName);
            return getMockResult(testType, normalizedLang, user, answers);
        }

        try {
            OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .temperature(0.8)
                .build();
            FashionAIProvider provider = AiServices.create(FashionAIProvider.class, model);

            String answersStr = answers.stream()
                .map(a -> {
                    String qText = getQuestionText(testType, a.getQuestionId());
                    String aText = getAnswerText(testType, a.getQuestionId(), a.getAnswer());
                    return "Q: " + qText + " | A: " + aText;
                })
                .collect(Collectors.joining("\n"));

            return provider.analyzeStyling(
                user.getName(),
                user.getAge(),
                user.getGender(), 
                user.getHeight(),
                user.getWeight(),
                testType,
                answersStr,
                langName,
                normalizedLang
            );
        } catch (Exception e) {
            log.error("AI Analysis failed: {}", e.getMessage());
            return getMockResult(testType, normalizedLang, user, answers);
        }
    }

    private String getQuestionText(String testType, Integer qId) {
        if (qId == null) return "Unknown Question";
        if ("COLOR_TYPE".equalsIgnoreCase(testType)) {
            return COLOR_TYPE_QUESTIONS.getOrDefault(qId, "Question " + qId);
        } else if ("BODY_SHAPE".equalsIgnoreCase(testType)) {
            return BODY_SHAPE_QUESTIONS.getOrDefault(qId, "Question " + qId);
        }
        return "Question " + qId;
    }

    private String getAnswerText(String testType, Integer qId, String answerCode) {
        if (qId == null || answerCode == null) return answerCode;
        Map<Integer, Map<String, String>> mappings = "COLOR_TYPE".equalsIgnoreCase(testType) ? COLOR_TYPE_ANSWERS : BODY_SHAPE_ANSWERS;
        Map<String, String> qAnswers = mappings.get(qId);
        if (qAnswers != null) {
            return qAnswers.getOrDefault(answerCode, answerCode);
        }
        return answerCode;
    }

    private String normalizeLanguage(String lang) {
        if (lang == null) return "en";
        String normalized = lang.split("[-_]")[0].toLowerCase();
        return List.of("en", "uk", "de", "es", "fr", "ko").contains(normalized) ? normalized : "en";
    }

    private String getLanguageName(String code) {
        return switch (code.toLowerCase()) {
            case "uk" -> "Ukrainian";
            case "de" -> "German";
            case "es" -> "Spanish";
            case "fr" -> "French";
            case "ko" -> "Korean";
            default -> "English";
        };
    }

    private AIAnalysisResult getMockResult(String testType, String lang, User user, List<TestAnswer> answers) {
        String normalizedLang = normalizeLanguage(lang);
        
        log.info("MOCK AI [{}] processing for user {}. Answers: {}", testType, user != null ? user.getEmail() : "anonymous", answers);
        Map<Integer, String> ansMap = new HashMap<>();
        for (TestAnswer a : answers) {
            if (a.getQuestionId() != null) {
                ansMap.put(a.getQuestionId(), a.getAnswer());
            }
        }

        boolean isUk = "uk".equals(normalizedLang);

        if ("COLOR_TYPE".equals(testType)) {
            String q1 = ansMap.getOrDefault(1, "B"); // Skin
            String q2 = ansMap.getOrDefault(2, "B"); // Hair
            String q3 = ansMap.getOrDefault(3, "B"); // Eyes
            String q4 = ansMap.getOrDefault(4, "B"); // Jewelry
            String q5 = ansMap.getOrDefault(5, "B"); // Contrast
            String q6 = ansMap.getOrDefault(6, "A"); // Sun
            String q7 = ansMap.getOrDefault(7, "A"); // Veins
            String q8 = ansMap.getOrDefault(8, "A"); // Lips

            // 1. Temperature Scoring
            int coolPoints = 0;
            int warmPoints = 0;
            if ("B".equals(q4)) coolPoints++; if ("A".equals(q4)) warmPoints++;
            if ("A".equals(q6)) coolPoints++; if ("B".equals(q6)) warmPoints++;
            if ("A".equals(q7)) coolPoints++; if ("B".equals(q7)) warmPoints++;
            if ("A".equals(q8)) coolPoints++; if ("B".equals(q8)) warmPoints++;
            
            String undertone = (coolPoints >= warmPoints) ? "COOL" : "WARM";
            
            // 2. Depth Scoring
            int depthScore = 0; // 0=Light, 1=Medium, 2=Deep
            if ("B".equals(q1)) depthScore += 1; else if ("C".equals(q1)) depthScore += 2;
            if ("B".equals(q2)) depthScore += 1; else if ("C".equals(q2)) depthScore += 2;
            if ("B".equals(q3)) depthScore += 1; else if ("C".equals(q3)) depthScore += 2;
            String depth = (depthScore <= 2) ? "LIGHT" : (depthScore >= 5 ? "DEEP" : "MEDIUM");

            // 3. Chroma/Contrast
            String contrastLevel = "B".equals(q5) ? "MEDIUM" : ("C".equals(q5) ? "HIGH" : "LOW");
            String chroma = ("C".equals(q5) || "A".equals(q2)) ? "CLEAR" : "SOFT";

            // 4. Final 12-Season Mapping
            String season;
            String colorTypeStr;
            
            if ("COOL".equals(undertone)) {
                if ("DEEP".equals(depth)) {
                    season = "WINTER"; colorTypeStr = "DEEP_WINTER";
                } else if ("LIGHT".equals(depth)) {
                    season = "SUMMER"; colorTypeStr = "LIGHT_SUMMER";
                } else if ("HIGH".equals(contrastLevel)) {
                    season = "WINTER"; colorTypeStr = "BRIGHT_WINTER";
                } else if ("LOW".equals(contrastLevel)) {
                    season = "SUMMER"; colorTypeStr = "SOFT_SUMMER";
                } else {
                    season = (depthScore >= 4) ? "WINTER" : "SUMMER";
                    colorTypeStr = season.equals("WINTER") ? "COOL_WINTER" : "COOL_SUMMER";
                }
            } else { // WARM
                if ("DEEP".equals(depth)) {
                    season = "AUTUMN"; colorTypeStr = "DEEP_AUTUMN";
                } else if ("LIGHT".equals(depth)) {
                    season = "SPRING"; colorTypeStr = "LIGHT_SPRING";
                } else if ("HIGH".equals(contrastLevel)) {
                    season = "SPRING"; colorTypeStr = "BRIGHT_SPRING";
                } else if ("LOW".equals(contrastLevel)) {
                    season = "AUTUMN"; colorTypeStr = "SOFT_AUTUMN";
                } else {
                    season = (depthScore >= 4) ? "AUTUMN" : "SPRING";
                    colorTypeStr = season.equals("AUTUMN") ? "WARM_AUTUMN" : "WARM_SPRING";
                }
            }

            List<String> palette = switch (colorTypeStr) {
                case "DEEP_WINTER" -> List.of("#000000", "#1a1a1a", "#002366", "#004d40", "#4a0e0e", "#ffffff");
                case "COOL_WINTER" -> List.of("#0047ab", "#ff007f", "#ffffff", "#e5e4e2", "#36454f", "#000080");
                case "BRIGHT_WINTER" -> List.of("#ff00ff", "#1f51ff", "#000000", "#ffffff", "#ffff00", "#00ff00");
                case "LIGHT_SUMMER" -> List.of("#add8e6", "#ffb6c1", "#e6e6fa", "#f5f5f5", "#98fb98", "#ffdae0");
                case "COOL_SUMMER" -> List.of("#80daeb", "#778899", "#bc8f8f", "#967117", "#9370db", "#f0f8ff");
                case "SOFT_SUMMER" -> List.of("#5d3954", "#4e5d6c", "#8a624a", "#a18e8d", "#d3d3d3", "#36454f");
                case "LIGHT_SPRING" -> List.of("#ffe5b4", "#fbceb1", "#ffffed", "#ff7f50", "#fffdd0", "#fffafa");
                case "WARM_SPRING" -> List.of("#fb8e7e", "#ffdb58", "#98fb98", "#c19a6b", "#fffaf0", "#f4a460");
                case "BRIGHT_SPRING" -> List.of("#00ced1", "#ff1493", "#ffd700", "#ff8c00", "#32cd32", "#ffffff");
                case "DEEP_AUTUMN" -> List.of("#2e1503", "#8b4513", "#556b2f", "#ff8c00", "#d2691e", "#3d2b1f");
                case "WARM_AUTUMN" -> List.of("#e2725b", "#808000", "#daa520", "#b87333", "#cc7722", "#ffefd5");
                case "SOFT_AUTUMN" -> List.of("#fa8072", "#f0e68c", "#808b96", "#eae0c8", "#a0a0a0", "#414a4c");
                default -> List.of("#581c87", "#7c3aed", "#a855f7", "#c084fc", "#e879f9", "#fdf4ff");
            };

            List<String> recommendations = new ArrayList<>();
            if ("COOL".equals(undertone)) {
                recommendations.addAll(List.of("tips.silverJewelry", "tips.berryTones", "tips.crispWhite", "tips.charcoalGrey"));
            } else {
                recommendations.addAll(List.of("tips.goldJewelry", "tips.earthTones", "tips.creamyBeige", "tips.warmTerracotta"));
            }
            
            // Add season-specific tip
            String seasonTip = switch (season) {
                case "WINTER" -> "tips.winter.contrast";
                case "SUMMER" -> "tips.summer.muted";
                case "SPRING" -> "tips.spring.warm";
                case "AUTUMN" -> "tips.autumn.earthy";
                default -> null;
            };
            if (seasonTip != null) recommendations.add(seasonTip);

            String summaryText = isUk ? 
                String.format("Базуючись на вашому %s підтоні (%d/4) та %s глибині, ви — %s. Контрастність: %s.", 
                    undertone.equals("COOL") ? "холодному" : "теплому", Math.max(coolPoints, warmPoints), depth.toLowerCase(), colorTypeStr.replace("_", " "), contrastLevel.toLowerCase()) :
                String.format("Based on your %s undertone (%d/4) and %s depth, you are a %s. Contrast level: %s.", 
                    undertone.toLowerCase(), Math.max(coolPoints, warmPoints), depth.toLowerCase(), colorTypeStr.replace("_", " "), contrastLevel.toLowerCase());

            String imagePrompt = String.format(
                "A realistic portrait of a %s with %s skin, %s depth features, and %s contrast. Wearing outfits in %s. Professional fashion photography.",
                user != null && user.getGender() != null ? user.getGender().toLowerCase() : "person", undertone.toLowerCase(), depth.toLowerCase(), contrastLevel.toLowerCase(), colorTypeStr
            );

            return AIAnalysisResult.builder()
                .resultType(colorTypeStr)
                .season(season)
                .undertone(undertone)
                .contrastLevel(contrastLevel)
                .depth(depth)
                .chroma(chroma)
                .summary(summaryText)
                .palette(palette)
                .recommendations(recommendations)
                .personalizedAdvice(isUk ? 
                    "Використовуйте кольори, що підкреслюють вашу природну яскравість та температурні особливості." :
                    "Focus on colors that enhance your natural brightness and temperature characteristics.")
                .imagePrompt(imagePrompt)
                .build();
        } else {
            // Updated Body Shape Analysis (7-Question Weighted Scoring)
            String q1 = ansMap.getOrDefault(1, "E"); // Widest Part
            String q2 = ansMap.getOrDefault(2, "C"); // Waistline
            String q3 = ansMap.getOrDefault(3, "E"); // Hip Curve
            String q4 = ansMap.getOrDefault(4, "C"); // Shoulder Line
            String q5 = ansMap.getOrDefault(5, "B"); // Bust Relative
            String q6 = ansMap.getOrDefault(6, "C"); // Weight Dist
            String q7 = ansMap.getOrDefault(7, "B"); // Overall Frame

            int hourglass = 0, pear = 0, apple = 0, rectangle = 0, invertedTriangle = 0;

            // Q1: Widest Part
            if ("A".equals(q1)) invertedTriangle += 10;
            else if ("B".equals(q1)) { apple += 5; invertedTriangle += 5; }
            else if ("C".equals(q1)) apple += 10;
            else if ("D".equals(q1)) pear += 10;
            else if ("E".equals(q1)) { hourglass += 5; rectangle += 5; }

            // Q2: Waistline
            if ("A".equals(q2)) hourglass += 10;
            else if ("B".equals(q2)) { hourglass += 5; pear += 5; }
            else if ("C".equals(q2)) rectangle += 10;
            else if ("D".equals(q2)) apple += 10;
            else if ("E".equals(q2)) { apple += 5; pear += 5; }

            // Q3: Hip Curve
            if ("A".equals(q3)) { hourglass += 5; pear += 5; }
            else if ("B".equals(q3)) { rectangle += 5; invertedTriangle += 5; }
            else if ("C".equals(q3)) pear += 10;
            else if ("D".equals(q3)) invertedTriangle += 10;
            else if ("E".equals(q3)) { rectangle += 5; hourglass += 5; }

            // Q4: Shoulder Line
            if ("A".equals(q4)) invertedTriangle += 10;
            else if ("B".equals(q4)) { rectangle += 5; invertedTriangle += 5; }
            else if ("C".equals(q4)) { pear += 5; apple += 5; }
            else if ("D".equals(q4)) pear += 10;

            // Q5: Bust Relative
            if ("A".equals(q5)) { pear += 5; rectangle += 5; }
            else if ("B".equals(q5)) hourglass += 5;
            else if ("C".equals(q5)) { apple += 5; invertedTriangle += 5; }

            // Q6: Weight Distribution
            if ("A".equals(q6)) pear += 10;
            else if ("B".equals(q6)) apple += 10;
            else if ("C".equals(q6)) { hourglass += 10; rectangle += 5; }
            else if ("D".equals(q6)) invertedTriangle += 10;

            // Q7: Overall Frame
            if ("A".equals(q7)) pear += 5;
            else if ("B".equals(q7)) hourglass += 5;
            else if ("C".equals(q7)) { rectangle += 5; invertedTriangle += 5; }

            log.info("Scoring results - H:{}, P:{}, A:{}, R:{}, IT:{}", hourglass, pear, apple, rectangle, invertedTriangle);

            String resultType = "HOURGLASS";
            int maxScore = hourglass;

            if (pear > maxScore) { maxScore = pear; resultType = "PEAR"; }
            if (apple > maxScore) { maxScore = apple; resultType = "APPLE"; }
            if (invertedTriangle > maxScore) { maxScore = invertedTriangle; resultType = "INVERTED_TRIANGLE"; }
            if (rectangle > maxScore) { maxScore = rectangle; resultType = "RECTANGLE"; }
            
            // Special tie-breaker: If Rectangle and Hourglass are tied, check waist again
            if (hourglass == rectangle && maxScore == hourglass) {
                 if ("A".equals(q2) || "B".equals(q2)) resultType = "HOURGLASS";
                 else resultType = "RECTANGLE";
            }

            log.info("Analysis complete: Body type={} scores: H={}, P={}, A={}, R={}, IT={}", 
                     resultType, hourglass, pear, apple, rectangle, invertedTriangle);

            String typeKey = resultType.toLowerCase();
            
            // Return localization-ready keys for recommendations
            return AIAnalysisResult.builder()
                .resultType(resultType)
                .summary("bodyShape." + typeKey)
                .palette(List.of())
                .recommendations(List.of(
                    "tips." + typeKey + ".tops",
                    "tips." + typeKey + ".bottoms",
                    "tips." + typeKey + ".dresses"
                ))
                .personalizedAdvice("Focus on techniques to " + (
                    "HOURGLASS".equals(resultType) ? "maintain your naturally balanced proportions." :
                    "PEAR".equals(resultType) ? "draw attention to your upper body to balance your hips." :
                    "APPLE".equals(resultType) ? "create a more defined waistline and highlight your legs." :
                    "RECTANGLE".equals(resultType) ? "create the illusion of curves with volume and belts." :
                    "narrow your shoulders and add volume to your lower body."
                ))
                .build();
        }
    }

    private String getTranslated(String lang, String en, String uk) {
        return "uk".equalsIgnoreCase(lang) ? uk : en;
    }
}
