import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import StyleTestForm from '../components/StyleTestForm';
import PhotoUpload from '../components/PhotoUpload';
import Header from '../components/Header';
import AvatarCard from '../components/AvatarCard';
import avatarWarm from '../assets/avatars/avatar_warm.png';
import avatarCool from '../assets/avatars/avatar_cool.png';
import avatarNeutral from '../assets/avatars/avatar_neutral.png';
import heroBanner from '../assets/avatars/hero_banner.png';

const StyleAnalysis = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [view, setView] = useState('start'); // 'start', 'test', 'photo'
    const [activeTab, setActiveTab] = useState('test');


    return (
        <div className="analysis-container">
            <Header />

            {/* ── Hero Section ── */}
            <div style={{
                borderRadius: 'var(--radius-card)',
                overflow: 'hidden',
                marginBottom: 'clamp(1.5rem, 5vw, 2.5rem)',
                position: 'relative',
                boxShadow: '0 30px 80px rgba(176,38,255,0.35)',
                border: '1.5px solid rgba(255,0,127,0.25)',
            }}>
                <img
                    src={heroBanner}
                    alt="Style DNA"
                    style={{ width: '100%', height: 'clamp(180px, 40vh, 400px)', objectFit: 'cover', objectPosition: 'center top', display: 'block' }}
                />
                <div style={{
                    position: 'absolute', inset: 0,
                    background: 'linear-gradient(to right, rgba(10,0,20,0.7) 0%, transparent 40%, transparent 60%, rgba(10,0,20,0.7) 100%)',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px',
                    padding: '24px',
                }}>
                    <h1 className="mobile-h1" style={{
                        fontFamily: "'Playfair Display', serif",
                        fontSize: 'clamp(1.8rem, 6vw, 2.6rem)',
                        fontWeight: 900,
                        textAlign: 'center',
                        margin: 0,
                        background: 'linear-gradient(135deg, #fff 30%, #f0abfc)',
                        WebkitBackgroundClip: 'text',
                        WebkitTextFillColor: 'transparent',
                        backgroundClip: 'text',
                        textShadow: 'none',
                        filter: 'drop-shadow(0 2px 12px rgba(255,0,127,0.5))',
                    }}>
                        {t('analysis.heroTitle')}
                    </h1>
                    <p style={{
                        color: 'rgba(255,255,255,0.85)',
                        fontFamily: 'Poppins, sans-serif',
                        fontSize: '1rem',
                        margin: 0,
                        textAlign: 'center',
                        fontWeight: 400,
                    }}>
                        {t('analysis.heroSub')} ✨
                    </p>
                </div>
            </div>

            {/* ── Mini avatar row ── */}
            <div style={{ display: 'flex', justifyContent: 'center', gap: '20px', marginBottom: '32px', flexWrap: 'wrap' }}>
                <AvatarCard src={avatarWarm} name="Warm Autumn" palette="🍂 Golden tones" size="sm" />
                <AvatarCard src={avatarNeutral} name="Your Style" palette="✨ Balanced hues" size="sm" active />
                <AvatarCard src={avatarCool} name="Cool Winter" palette="❄️ Icy shades" size="sm" />
            </div>

            {/* ── View Selection / Tabs ── */}
            {view !== 'start' && (
                <div className="tabs-container">
                    {[
                        { key: 'test', label: t('analysis.colorType') },
                        { key: 'photo', label: t('analysis.photoScan') },
                    ].map(tab => (
                        <button
                            key={tab.key}
                            className={`tab-btn ${activeTab === tab.key && view !== 'start' ? 'active' : ''}`}
                            onClick={() => {
                                if (tab.key === 'body') {
                                    navigate('/body-shape-test');
                                } else if (tab.key === 'photo') {
                                    setView('photo');
                                    setActiveTab('photo');
                                } else {
                                    setView('test');
                                    setActiveTab('test');
                                }
                            }}
                        >
                            {tab.label}
                        </button>
                    ))}
                    <button className="tab-btn" onClick={() => setView('start')}>← {t('common.homepage', 'Homepage')}</button>
                </div>
            )}

            {/* ── Content ── */}
            {view === 'start' ? (
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
                    gap: '24px',
                    padding: '10px'
                }}>
                    {/* Color Type Card */}
                    <div 
                        className="bratz-card neon-border"
                        style={{ cursor: 'pointer', textAlign: 'center', transition: 'transform 0.3s' }}
                        onClick={() => { setView('test'); setActiveTab('test'); }}
                        onMouseEnter={(e) => e.currentTarget.style.transform = 'translateY(-10px)'}
                        onMouseLeave={(e) => e.currentTarget.style.transform = 'translateY(0)'}
                    >
                        <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>🎨</div>
                        <h2 className="brand-font" style={{ fontSize: '1.8rem', marginBottom: '1rem' }}>{t('analysis.colorType')}</h2>
                        <p style={{ color: 'var(--text-muted)', marginBottom: '1.5rem' }}>{t('analysis.colorTestSubtitle', 'Discover your perfect palette based on skin, eyes, and hair.')}</p>
                        <button className="bratz-btn" style={{ width: '100%' }}>{t('analysis.startColorTest', 'Start Color Test ✨')}</button>
                    </div>

                    {/* Body Shape Card */}
                    <div 
                        className="bratz-card neon-border"
                        style={{ cursor: 'pointer', textAlign: 'center', transition: 'transform 0.3s' }}
                        onClick={() => navigate('/body-shape-test')}
                        onMouseEnter={(e) => e.currentTarget.style.transform = 'translateY(-10px)'}
                        onMouseLeave={(e) => e.currentTarget.style.transform = 'translateY(0)'}
                    >
                        <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>👗</div>
                        <h2 className="brand-font" style={{ fontSize: '1.8rem', marginBottom: '1rem' }}>{t('nav.bodyShape', 'Body Shape')}</h2>
                        <p style={{ color: 'var(--text-muted)', marginBottom: '1.5rem' }}>{t('analysis.bodyTestSubtitle', 'Find the most flattering silhouettes for your unique frame.')}</p>
                        <button className="bratz-btn" style={{ width: '100%', background: 'linear-gradient(45deg, #a855f7, #ec4899)' }}>{t('analysis.startBodyTest', 'Start Body Test ✨')}</button>
                    </div>

                    {/* Photo Scan Card */}
                    <div 
                        className="bratz-card neon-border mobile-padding-sm"
                        style={{ cursor: 'pointer', textAlign: 'center', transition: 'transform 0.3s', gridColumn: '1 / -1' }}
                        onClick={() => { setView('photo'); setActiveTab('photo'); }}
                        onMouseEnter={(e) => e.currentTarget.style.transform = 'translateY(-10px)'}
                        onMouseLeave={(e) => e.currentTarget.style.transform = 'translateY(0)'}
                    >
                        <div className="mobile-stack" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '30px', flexWrap: 'wrap' }}>
                             <div style={{ fontSize: 'min(4rem, 15vw)' }}>📸</div>
                             <div className="mobile-center" style={{ textAlign: 'left', flex: 1, minWidth: '200px' }}>
                                <h2 className="brand-font" style={{ fontSize: 'clamp(1.4rem, 5vw, 1.8rem)', marginBottom: '0.5rem' }}>{t('analysis.photoScan')}</h2>
                                <p style={{ color: 'var(--text-muted)', marginBottom: '1rem', fontSize: '0.95rem' }}>{t('analysis.photoScanSubtitle', 'Upload a selfie for instant AI-powered seasonal analysis.')}</p>
                             </div>
                             <button className="bratz-btn" style={{ minWidth: '200px', width: '100%' }}>{t('analysis.tryPhotoScan', 'Try Photo Scan ✨')}</button>
                        </div>
                    </div>
                </div>
            ) : (
                <>
                    {activeTab === 'test' ? <StyleTestForm /> : <PhotoUpload />}
                </>
            )}

            {/* ── Footer stickers ── */}
            <div style={{
                textAlign: 'center',
                padding: '32px 0 16px',
                fontSize: 'clamp(1rem, 5vw, 1.5rem)',
                letterSpacing: 'clamp(4px, 2vw, 10px)',
                opacity: 0.35,
                whiteSpace: 'nowrap',
                overflow: 'hidden'
            }}>
                👠 💄 💅 🌸 ✨ 💫 👛 🎀
            </div>
        </div>
    );
};

export default StyleAnalysis;
