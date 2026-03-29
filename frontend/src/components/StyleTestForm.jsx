import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { analyzeTestAnswers } from '../services/analysisService';
import ResultCard from './ResultCard';
import ImageOptionCard from './ImageOptionCard';
import './StyleTestForm.css';

// ── Local generated images (language-neutral) ─────────────────────────────────
import eyesBlue from '../assets/eyes/blue.png';
import eyesGreen from '../assets/eyes/green.png';
import eyesBrown from '../assets/eyes/brown.png';
// D and E removed as per user request
import jewelGold from '../assets/jewelry/gold.png';
import jewelSilver from '../assets/jewelry/silver.png';
import jewelRose from '../assets/jewelry/rosegold.png';
import contrastHigh from '../assets/contrast/high.png';
import contrastMedium from '../assets/contrast/medium.png';
import contrastLow from '../assets/contrast/low.png';

// ── NEW professional diagnostic assets ────────────────────────────────────────
import skinFair from '../assets/skin/fair_cool.png';
import skinMedium from '../assets/skin/medium_neutral.png';
import skinDeep from '../assets/skin/deep_warm.png';
import sunBurn from '../assets/sun/burn.png';
import sunTan from '../assets/sun/tan.png';
import veinsBlue from '../assets/veins/blue.png';
import veinsGreen from '../assets/veins/green.png';
import veinsMixed from '../assets/veins/mixed.png';
import lipsPink from '../assets/lips/pink_cool.png';
import lipsPeach from '../assets/lips/peach_warm.png';

/**
 * QUESTION CONFIG — image + answer code per option.
 * Editing here NEVER breaks the backend: only codes A/B/C/D/E are sent.
 *
 * questionId 1 – skin tone       → 3 options A/B/C  (unsplash photos)
 * questionId 2 – hair color      → 3 options A/B/C  (unsplash photos)
 * questionId 3 – eye color       → 5 options A/B/C/D/E (generated close-ups)
 * questionId 4 – jewelry         → 3 options A/B/C  (generated jewelry shots)
 * questionId 5 – hair/skin contrast → 3 options A/B/C  (generated portraits)
 */
const QUESTION_OPTIONS = {
    1: [
        { code: 'A', img: skinFair },
        { code: 'B', img: skinMedium },
        { code: 'C', img: skinDeep },
    ],
    2: [
        { code: 'A', img: 'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=500&q=85' },
        { code: 'B', img: 'https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=500&q=85' },
        { code: 'C', img: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=500&q=85' },
    ],
    3: [
        { code: 'A', img: eyesBlue },
        { code: 'B', img: eyesGreen },
        { code: 'C', img: eyesBrown },
    ],
    4: [
        { code: 'A', img: jewelGold },
        { code: 'B', img: jewelSilver },
        { code: 'C', img: jewelRose },
    ],
    5: [
        { code: 'A', img: contrastLow },
        { code: 'B', img: contrastMedium },
        { code: 'C', img: contrastHigh },
    ],
    6: [
        { code: 'A', img: sunBurn },
        { code: 'B', img: sunTan },
    ],
    7: [
        { code: 'A', img: veinsBlue },
        { code: 'B', img: veinsGreen },
        { code: 'C', img: veinsMixed },
    ],
    8: [
        { code: 'A', img: lipsPink },
        { code: 'B', img: lipsPeach },
    ],
};

const QUESTION_IDS = [1, 2, 3, 4, 5, 6, 7, 8];

const StyleTestForm = () => {
    const { t, i18n } = useTranslation();
    const [currentStep, setCurrentStep] = useState(0);
    const [answers, setAnswers] = useState({});
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);
    const navigate = useNavigate();

    // Build questions from i18n — answers count matches QUESTION_OPTIONS
    const questions = QUESTION_IDS.map((id) => ({
        id,
        text: t(`test.questions.${id}.text`),
        options: QUESTION_OPTIONS[id].map((opt) => ({
            value: opt.code,
            text: t(`test.questions.${id}.answers.${opt.code}`),
            imageUrl: opt.img,
        })),
    }));

    const currentQ = questions[currentStep];
    const progressPercent = ((currentStep) / questions.length) * 100;

    const handleSelect = (value) => {
        setAnswers((prev) => ({ ...prev, [currentQ.id]: value }));
    };

    const handleNext = () => {
        if (currentStep < questions.length - 1) {
            setCurrentStep((s) => s + 1);
        } else {
            submitTest();
        }
    };

    const handleBack = () => {
        if (currentStep > 0) {
            setCurrentStep((s) => s - 1);
        }
    };

    const submitTest = async () => {
        setLoading(true);
        // API contract preserved: { questionId: Integer, answer: "A"|"B"|"C"|"D" }
        const payload = Object.entries(answers).map(([qId, answer]) => ({
            questionId: parseInt(qId, 10),
            answer,
        }));
        try {
            console.log("Submitting color analysis test...", { payload, lang: i18n.language });
            const data = await analyzeTestAnswers(payload, i18n.language);
            console.log("Color Analysis Response:", data);
            setResult(data);
        } catch (err) {
            console.error("Color Analysis Error:", err);
            alert(t('errors.generic'));
        } finally {
            setLoading(false);
        }
    };

    const resetTest = () => { setResult(null); setCurrentStep(0); setAnswers({}); };

    if (result) return <ResultCard result={result} onReset={resetTest} />;

    // Dynamic grid: 4-option questions use 2×2, 3-option use 3 columns
    const cols = currentQ.options.length === 4 ? 2 : 3;

    return (
        <div className="style-test-container">
            {/* Progress bar */}
            <div className="progress-bar-bg">
                <div className="progress-bar-fill" style={{ width: `${progressPercent}%` }} />
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                <button 
                    onClick={() => navigate('/')}
                    style={{ background: 'none', border: 'none', color: 'var(--bratz-pink)', cursor: 'pointer', fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: '5px' }}
                >
                    🏠 {t('nav.backToHome')}
                </button>
                <p className="step-counter" style={{ margin: 0 }}>
                    {t('analysis.question')} {currentStep + 1} {t('analysis.of')} {questions.length}
                </p>
            </div>

            {/* Question text */}
            <h2 className="question-text">{currentQ.text}</h2>

            {/* Image option cards — 2-col for 4 options, 3-col for 3 options */}
            <div
                className="options-grid"
                style={{ gridTemplateColumns: `repeat(${cols}, 1fr)` }}
            >
                {currentQ.options.map((opt) => (
                    <ImageOptionCard
                        key={opt.value}
                        imageUrl={opt.imageUrl}
                        label={opt.text}
                        badge={opt.value}
                        selected={answers[currentQ.id] === opt.value}
                        onClick={() => handleSelect(opt.value)}
                    />
                ))}
            </div>

            <div style={{ display: 'flex', gap: '1rem', marginTop: '10px' }}>
                {currentStep > 0 && (
                    <button
                        className="bratz-btn ghost"
                        onClick={handleBack}
                        style={{ flex: 1 }}
                    >
                        {t('analysis.back', 'Back')}
                    </button>
                )}
                <button
                    className="submit-button"
                    onClick={handleNext}
                    disabled={!answers[currentQ.id] || loading}
                    style={{ flex: currentStep > 0 ? 2 : 1 }}
                >
                    {loading
                        ? t('analysis.analyzing')
                        : currentStep === questions.length - 1
                            ? t('analysis.reveal')
                            : t('analysis.next')}
                </button>
            </div>
        </div>
    );
};

export default StyleTestForm;
