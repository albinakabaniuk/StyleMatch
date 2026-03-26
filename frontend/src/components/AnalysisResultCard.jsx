import React from 'react';
import { useTranslation } from 'react-i18next';

const AnalysisResultCard = ({ result, onReset }) => {
    const { t } = useTranslation();
    if (!result) return null;

    const season = result.season || 'N/A';
    const palette = result.palette || [];
    const recommendations = result.recommendations || [];

    return (
        <div className="bratz-card glass-card" style={{
            padding: '2rem',
            marginTop: '2rem',
            textAlign: 'center',
            maxWidth: '600px',
            margin: '2rem auto',
            animation: 'fadeIn 0.6s ease-out'
        }}>
            <div style={{ marginBottom: '1.5rem' }}>
                <span style={{
                    fontSize: '0.8rem',
                    background: 'var(--bratz-pink)',
                    padding: '5px 15px',
                    borderRadius: '20px',
                    textTransform: 'uppercase',
                    fontWeight: '800',
                    boxShadow: '0 0 15px rgba(255,0,127,0.4)'
                }}>
                    {t('analysis.colorTypeTitle', 'Color Type')}
                </span>
            </div>

            <h2 className="brand-font" style={{ fontSize: '2.5rem', marginBottom: '0.5rem' }}>
                {t(`colorTypes.${result.colorType}`, result.colorType)}
            </h2>

            {/* Season removed to avoid redundancy with Color Type */}

            <div style={{
                background: 'rgba(255,255,255,0.05)',
                padding: '1.5rem',
                borderRadius: '16px',
                border: '1px solid rgba(255,255,255,0.1)',
                marginBottom: '2rem'
            }}>
                <p style={{ margin: 0, fontSize: '1rem', lineHeight: '1.6', color: 'rgba(255,255,255,0.9)' }}>
                    {t(result.message, result.message)}
                </p>
            </div>

            {palette.length > 0 && (
                <div style={{ marginBottom: '2rem' }}>
                    <h4 style={{ fontSize: '0.9rem', color: 'rgba(255,255,255,0.5)', marginBottom: '1rem', textTransform: 'uppercase' }}>
                        {t('result.signaturePalette', 'Your Signature Palette')}
                    </h4>
                    <div style={{ display: 'flex', gap: '10px', justifyContent: 'center', flexWrap: 'wrap' }}>
                        {palette.map((color, idx) => (
                            <div
                                key={idx}
                                style={{
                                    width: '45px',
                                    height: '45px',
                                    backgroundColor: color,
                                    borderRadius: '12px',
                                    border: '2px solid rgba(255,255,255,0.2)',
                                    boxShadow: '0 5px 15px rgba(0,0,0,0.3)',
                                    transition: 'transform 0.3s ease'
                                }}
                                onMouseEnter={e => e.currentTarget.style.transform = 'translateY(-5px) scale(1.1)'}
                                onMouseLeave={e => e.currentTarget.style.transform = 'translateY(0) scale(1)'}
                                title={color}
                            />
                        ))}
                    </div>
                </div>
            )}

            {recommendations.length > 0 && (
                <div style={{ marginBottom: '2rem', textAlign: 'left', background: 'rgba(255,255,255,0.02)', padding: '1.5rem', borderRadius: '12px' }}>
                    <h4 style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.4)', marginBottom: '1rem', textTransform: 'uppercase', letterSpacing: '1px' }}>
                        {t('bodyShape.topRecommendations', 'Styling Tips')}
                    </h4>
                    <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'grid', gap: '0.6rem' }}>
                        {recommendations.map((tip, idx) => (
                            <li key={idx} style={{ display: 'flex', gap: '10px', fontSize: '0.9rem', color: 'rgba(255,255,255,0.9)' }}>
                                <span style={{ color: 'var(--bratz-pink)' }}>✦</span>
                                {t(tip, tip)}
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <button
                    onClick={() => window.location.href = '/profile'}
                    className="bratz-btn ghost"
                    style={{ width: '100%' }}
                >
                    {t('bodyShape.viewProfileBtn', 'View Profile')}
                </button>
                <button
                    onClick={onReset}
                    className="bratz-btn"
                    style={{ width: '100%' }}
                >
                    {t('bodyShape.retakeTestBtn', 'Try Again ✨')}
                </button>
            </div>
        </div>
    );
};

export default AnalysisResultCard;
