package com.stylematch.service;

import com.stylematch.dto.AIAnalysisResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

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
