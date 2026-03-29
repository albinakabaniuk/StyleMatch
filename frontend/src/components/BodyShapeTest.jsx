import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { analyzeBodyShape } from '../services/analysisService';
import '../index.css';
import Header from './Header';
import q1 from '../assets/body-shape/q1.png';
import q2 from '../assets/body-shape/q2.png';
import q3 from '../assets/body-shape/q3.png';
import q4 from '../assets/body-shape/q4.png';
import q5 from '../assets/body-shape/q5.png';
import q6 from '../assets/body-shape/q6.png';
import q7 from '../assets/body-shape/q7.png';

const BodyShapeTest = () => {
  const { t, i18n } = useTranslation();
  const [step, setStep] = useState(0);
  const [answers, setAnswers] = useState([]);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const QUESTIONS = [
    {
      id: 1,
      question: t('bodyShape.widestPartQuestion'),
      image: q1,
      options: [
        { id: 'A', text: t('bodyShape.shoulders'), value: 'A' },
        { id: 'B', text: t('bodyShape.bust'), value: 'B' },
        { id: 'C', text: t('bodyShape.waist'), value: 'C' },
        { id: 'D', text: t('bodyShape.hips'), value: 'D' },
        { id: 'E', text: t('bodyShape.equal'), value: 'E' }
      ]
    },
    {
      id: 2,
      question: t('bodyShape.waistlineDescription'),
      image: q2,
      options: [
        { id: 'A', text: t('bodyShape.veryDefined'), value: 'A' },
        { id: 'B', text: t('bodyShape.slightlyDefined'), value: 'B' },
        { id: 'C', text: t('bodyShape.straightAthletic'), value: 'C' },
        { id: 'D', text: t('bodyShape.wideFull'), value: 'D' },
        { id: 'E', text: t('bodyShape.highWaist'), value: 'E' }
      ]
    },
    {
      id: 3,
      question: t('bodyShape.hipCurveQuestion'),
      image: q3,
      options: [
        { id: 'A', text: t('bodyShape.roundedFull'), value: 'A' },
        { id: 'B', text: t('bodyShape.straightSlim'), value: 'B' },
        { id: 'C', text: t('bodyShape.widerThanShoulders'), value: 'C' },
        { id: 'D', text: t('bodyShape.narrowerThanShoulders'), value: 'D' },
        { id: 'E', text: t('bodyShape.average'), value: 'E' }
      ]
    },
    {
      id: 4,
      question: t('bodyShape.shoulderLineQuestion'),
      image: q4,
      options: [
        { id: 'A', text: t('bodyShape.broadStrong'), value: 'A' },
        { id: 'B', text: t('bodyShape.squarePiercing'), value: 'B' },
        { id: 'C', text: t('bodyShape.softSloped'), value: 'C' },
        { id: 'D', text: t('bodyShape.petiteNarrow'), value: 'D' }
      ]
    },
    {
      id: 5,
      question: t('bodyShape.bustRelativeQuestion'),
      image: q5,
      options: [
        { id: 'A', text: t('bodyShape.smallBust'), value: 'A' },
        { id: 'B', text: t('bodyShape.mediumBust'), value: 'B' },
        { id: 'C', text: t('bodyShape.largeBust'), value: 'C' }
      ]
    },
    {
      id: 6,
      question: t('bodyShape.weightDistQuestion'),
      image: q6,
      options: [
        { id: 'A', text: t('bodyShape.gainHips'), value: 'A' },
        { id: 'B', text: t('bodyShape.gainMiddle'), value: 'B' },
        { id: 'C', text: t('bodyShape.gainEvenly'), value: 'C' },
        { id: 'D', text: t('bodyShape.gainTop'), value: 'D' }
      ]
    },
    {
      id: 7,
      question: t('bodyShape.overallFrameQuestion'),
      image: q7,
      options: [
        { id: 'A', text: t('bodyShape.framePetite'), value: 'A' },
        { id: 'B', text: t('bodyShape.frameAverage'), value: 'B' },
        { id: 'C', text: t('bodyShape.frameAthletic'), value: 'C' }
      ]
    }
  ];

  const handleAnswer = (option) => {
    const newAnswers = [...answers, { questionId: QUESTIONS[step].id, answer: option.value }];
    setAnswers(newAnswers);

    if (step < QUESTIONS.length - 1) {
      setStep(step + 1);
    } else {
      finishTest(newAnswers);
    }
  };

  const handleBack = () => {
    if (step > 0) {
      setStep(step - 1);
      setAnswers(answers.slice(0, -1));
    }
  };

  const finishTest = async (finalAnswers) => {
    setLoading(true);
    setError('');
    try {
      console.log("Submitting body shape test...", { finalAnswers, lang: i18n.language });
      const data = await analyzeBodyShape(finalAnswers, i18n.language);
      console.log("Body Shape Analysis Response:", data);
      setResult(data);
    } catch (err) {
      console.error('Body shape analysis failed:', err);
      setError(t('bodyShape.analysisFailed'));
    } finally {
      setLoading(false);
    }
  };

  if (result) {
    return (
      <div className="analysis-container">
        <Header />
        <div className="container mobile-padding-sm" style={{ padding: '2rem', display: 'flex', justifyContent: 'center' }}>
          <div className="bratz-card mobile-padding-sm" style={{ maxWidth: 800, width: '100%', textAlign: 'center', padding: 'clamp(1.5rem, 5vw, 3rem)' }}>
            <h2 className="brand-font mobile-h1" style={{ 
              fontSize: 'clamp(2rem, 8vw, 3.5rem)', 
              marginBottom: '1rem', 
              color: 'var(--bratz-pink)', 
              filter: 'drop-shadow(0 10px 20px rgba(255,0,127,0.3))',
              textTransform: 'uppercase'
            }}>
              {t(`bodyShape.${(result.bodyShapeType || '').toLowerCase()}`, (result.bodyShapeType || 'Analysis').replace('_', ' '))}!
            </h2>
            <p style={{ fontSize: '1.25rem', marginBottom: '1.5rem', color: '#f0e6ff', lineHeight: 1.6, fontWeight: 500 }}>
              {t(result.explanation, "Your silhouette analysis is complete.")}
            </p>
            {result.personalizedAdvice && (
                <div style={{ 
                    marginBottom: '3rem', 
                    padding: '1.5rem', 
                    background: 'rgba(176,38,255,0.1)', 
                    borderRadius: '16px', 
                    border: '1px solid rgba(176,38,255,0.3)',
                    fontStyle: 'italic',
                    color: '#e0c3ff',
                    lineHeight: 1.6,
                    fontSize: '1.1rem'
                }}>
                    "{t(result.personalizedAdvice, result.personalizedAdvice)}"
                </div>
            )}


            <div className="glass-panel" style={{ textAlign: 'left', marginBottom: '3rem', padding: '2rem', borderLeft: '4px solid var(--bratz-pink)', background: 'rgba(255,255,255,0.03)' }}>
              <h3 style={{ marginBottom: '1.5rem', color: 'var(--bratz-pink)', textTransform: 'uppercase', letterSpacing: '2px', fontSize: '0.9rem', fontWeight: 700 }}>{t('bodyShape.topRecommendations')}:</h3>
              <ul style={{ paddingLeft: '0', listStyle: 'none', color: '#f0e6ff' }}>
                {(result.recommendations || []).map((rec, i) => (
                  <li key={i} style={{ marginBottom: '1.2rem', display: 'flex', alignItems: 'flex-start', gap: '12px' }}>
                      <span style={{ color: 'var(--bratz-pink)', fontSize: '1.2rem' }}>✦</span>
                      <span style={{ fontSize: '1.1rem', lineHeight: 1.4 }}>{t(rec, rec)}</span>
                  </li>
                ))}
              </ul>
            </div>

            <div className="mobile-stack" style={{ display: 'flex', gap: '1.5rem', justifyContent: 'center' }}>
              <button className="bratz-btn primary" onClick={() => navigate('/profile')} style={{ flex: 1, width: '100%' }}>
                  {t('bodyShape.viewProfileBtn')}
              </button>
              <button className="bratz-btn secondary" onClick={() => { setResult(null); setStep(0); setAnswers([]); }} style={{ flex: 1, width: '100%' }}>
                  {t('bodyShape.retakeTestBtn')}
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  const currentQ = QUESTIONS[step];

  return (
    <div className="analysis-container">
      <Header />
      <div className="container mobile-padding-sm" style={{ padding: '1rem', display: 'flex', justifyContent: 'center', minHeight: '80vh', alignItems: 'center' }}>
        <div className="bratz-card mobile-padding-sm" style={{ maxWidth: 600, width: '100%', padding: 'clamp(1rem, 4vw, 2.5rem)' }}>
          <div style={{ marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '10px', flexWrap: 'wrap' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                <button 
                    onClick={() => navigate('/')}
                    style={{ background: 'none', border: 'none', color: 'var(--bratz-pink)', cursor: 'pointer', fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: '5px' }}
                >
                    🏠 {t('common.homepage', 'Homepage')}
                </button>
                {step > 0 && (
                    <button 
                        onClick={handleBack}
                        style={{ background: 'none', border: 'none', color: 'var(--bratz-pink)', cursor: 'pointer', fontSize: '1.5rem', padding: '0 5px' }}
                        title="Back"
                    >
                        ←
                    </button>
                )}
                <span className="brand-font" style={{ fontSize: '1.2rem', color: 'var(--bratz-pink)' }}>{t('bodyShape.analysisTitle')}</span>
            </div>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>{t('bodyShape.stepLabel')} {step + 1} {t('bodyShape.stepOf')} {QUESTIONS.length}</span>
          </div>

          <div className="progress-bar" style={{ height: 4, background: 'rgba(255,255,255,0.1)', borderRadius: 2, marginBottom: '2rem', overflow: 'hidden' }}>
            <div style={{ 
              height: '100%', 
              width: `${((step + 1) / QUESTIONS.length) * 100}%`, 
              background: 'var(--bratz-pink)',
              transition: 'width 0.4s ease'
            }}></div>
          </div>

          {currentQ.image && (
              <div style={{ marginBottom: '2rem', borderRadius: '16px', overflow: 'hidden', boxShadow: '0 10px 30px rgba(0,0,0,0.3)' }}>
                  <img src={currentQ.image} alt="Question visual" style={{ width: '100%', height: 'auto', display: 'block' }} />
              </div>
          )}

          <h2 style={{ marginBottom: '2rem', fontSize: '1.5rem' }}>{currentQ.question}</h2>

          <div style={{ display: 'grid', gap: '1rem' }}>
            {currentQ.options.map((opt) => (
              <button
                key={opt.id}
                className="bratz-btn secondary"
                style={{ textAlign: 'left', padding: '1.2rem', justifyContent: 'flex-start', height: 'auto', fontWeight: 400 }}
                onClick={() => handleAnswer(opt)}
                disabled={loading}
              >
                {opt.text}
              </button>
            ))}
          </div>

          {loading && (
            <div style={{ marginTop: '2rem', textAlign: 'center', color: 'var(--bratz-pink)' }}>
              {t('bodyShape.calculatingSilhouette')}
            </div>
          )}

          {error && (
            <div style={{ marginTop: '2rem', color: '#ff4d4d', textAlign: 'center' }}>
              {error}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default BodyShapeTest;
