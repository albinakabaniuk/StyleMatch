import React from 'react';
import { useTranslation } from 'react-i18next';
import avatarCool from '../assets/avatars/avatar_cool.png';
import avatarWarm from '../assets/avatars/avatar_warm.png';
import avatarNeutral from '../assets/avatars/avatar_neutral.png';
import AvatarCard from './AvatarCard';
import userService from '../services/userService';

// Map analysis results → local generated avatars (no copyright, all original)
const RESULT_CONFIG = {
    // ── WINTER ──────────────────────────────────────────────────────────────
    DEEP_WINTER: {
        avatar: avatarCool,
        name: 'Deep Winter',
        colors: ['#000000', '#1a1a1a', '#002366', '#004d40', '#4a0e0e', '#ffffff'],
        glow: 'rgba(0,35,102,0.35)',
    },
    COOL_WINTER: {
        avatar: avatarCool,
        name: 'Cool Winter',
        colors: ['#0047ab', '#ff007f', '#ffffff', '#e5e4e2', '#36454f', '#000080'],
        glow: 'rgba(0,71,171,0.35)',
    },
    BRIGHT_WINTER: {
        avatar: avatarCool,
        name: 'Bright Winter',
        colors: ['#ff00ff', '#1f51ff', '#000000', '#ffffff', '#ffff00', '#00ff00'],
        glow: 'rgba(255,0,255,0.35)',
    },
    // ── SUMMER ──────────────────────────────────────────────────────────────
    LIGHT_SUMMER: {
        avatar: avatarCool,
        name: 'Light Summer',
        colors: ['#add8e6', '#ffb6c1', '#e6e6fa', '#f5f5f5', '#98fb98', '#ffdae0'],
        glow: 'rgba(173,216,230,0.35)',
    },
    COOL_SUMMER: {
        avatar: avatarCool,
        name: 'Cool Summer',
        colors: ['#80daeb', '#778899', '#bc8f8f', '#967117', '#9370db', '#f0f8ff'],
        glow: 'rgba(128,218,235,0.35)',
    },
    SOFT_SUMMER: {
        avatar: avatarCool,
        name: 'Soft Summer',
        colors: ['#5d3954', '#4e5d6c', '#8a624a', '#a18e8d', '#d3d3d3', '#36454f'],
        glow: 'rgba(93,57,84,0.35)',
    },
    // ── SPRING ──────────────────────────────────────────────────────────────
    LIGHT_SPRING: {
        avatar: avatarWarm,
        name: 'Light Spring',
        colors: ['#ffe5b4', '#fbceb1', '#ffffed', '#ff7f50', '#fffdd0', '#fffafa'],
        glow: 'rgba(255,229,180,0.35)',
    },
    WARM_SPRING: {
        avatar: avatarWarm,
        name: 'Warm Spring',
        colors: ['#fb8e7e', '#ffdb58', '#98fb98', '#c19a6b', '#fffaf0', '#f4a460'],
        glow: 'rgba(251,142,126,0.35)',
    },
    BRIGHT_SPRING: {
        avatar: avatarWarm,
        name: 'Bright Spring',
        colors: ['#00ced1', '#ff1493', '#ffd700', '#ff8c00', '#32cd32', '#ffffff'],
        glow: 'rgba(0,206,209,0.35)',
    },
    // ── AUTUMN ──────────────────────────────────────────────────────────────
    DEEP_AUTUMN: {
        avatar: avatarWarm,
        name: 'Deep Autumn',
        colors: ['#2e1503', '#8b4513', '#556b2f', '#ff8c00', '#d2691e', '#3d2b1f'],
        glow: 'rgba(139,69,19,0.35)',
    },
    WARM_AUTUMN: {
        avatar: avatarWarm,
        name: 'Warm Autumn',
        colors: ['#e2725b', '#808000', '#daa520', '#b87333', '#cc7722', '#ffefd5'],
        glow: 'rgba(226,114,91,0.35)',
    },
    SOFT_AUTUMN: {
        avatar: avatarWarm,
        name: 'Soft Autumn',
        colors: ['#fa8072', '#f0e68c', '#808b96', '#eae0c8', '#a0a0a0', '#414a4c'],
        glow: 'rgba(250,128,114,0.35)',
    },
    DEFAULT: {
        avatar: avatarNeutral,
        name: 'Your Style Identity',
        colors: ['#581c87', '#7c3aed', '#a855f7', '#c084fc', '#e879f9', '#fdf4ff'],
        glow: 'rgba(168,85,247,0.35)',
    },
};

const ResultCard = ({ result, onReset }) => {
    const { t } = useTranslation();
    console.log("ResultCard received data:", result);
    
    // History results have result.metadata as a JSON string
    let metadata = {};
    if (typeof result?.metadata === 'string') {
        try {
            metadata = JSON.parse(result.metadata) || {};
        } catch (e) {
            console.warn("ResultCard: Could not parse metadata JSON", e);
        }
    } else if (result?.metadata && typeof result.metadata === 'object') {
        metadata = result.metadata;
    }

    const colorType = result?.colorType || result?.aiResult?.resultType || result?.result || 'DEFAULT';
    const config = RESULT_CONFIG[colorType] || RESULT_CONFIG.DEFAULT;

    // Use dynamic backend data if available, otherwise config (for avatars/glow)
    const palette = result?.palette || result?.aiResult?.palette || metadata?.palette || config.colors || [];
    const advice = result?.personalizedAdvice || result?.aiResult?.personalizedAdvice || metadata?.advice || config.advice || t('result.noAdvice');
    const summary = result?.message || result?.summary || result?.aiResult?.summary || metadata?.summary || '';
    const recommendations = result?.recommendations || result?.aiResult?.recommendations || metadata?.recommendations || [];
    
    const undertone = result?.undertone || metadata?.undertone || '—';
    const contrast = result?.contrastLevel || metadata?.contrast || '—';
    const season = result?.season || metadata?.season || config.name.split(' ')[1] || '—';

    // ── Personalized Avatar Logic ──
    const [profileAvatar, setProfileAvatar] = React.useState(null);
    React.useEffect(() => {
        const fetchProfile = async () => {
            try {
                const p = await userService.getProfile();
                setProfileAvatar(p?.avatar);
            } catch (err) { /* ignore */ }
        };
        fetchProfile();
    }, []);

    const isUrl = (path) => path && (path.startsWith('http') || path.startsWith('/') || path.startsWith('data:'));
    
    // Priority: Uploaded photo (from analysis) > Profile Avatar > Silhouette Config
    const heroImage = result?.uploadedPhoto || (isUrl(profileAvatar) ? profileAvatar : config.avatar);

    return (
        <div className="bratz-card mobile-padding-sm" style={{
            maxWidth: '800px',
            margin: '0 auto',
            animation: 'fadeInUp 0.8s cubic-bezier(0.22, 1, 0.36, 1)',
            padding: 'clamp(1.25rem, 5vw, 2.5rem)',
        }}>
            {/* Header section */}
            <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
                 <div style={{ position: 'relative', display: 'inline-block' }}>
                    <div className="avatar-glow" style={{ 
                        position: 'absolute', inset: -20, borderRadius: '50%',
                        background: config.glow, filter: 'blur(40px)', opacity: 0.6, zIndex: 0
                    }} />
                    <AvatarCard
                        src={heroImage}
                        size="lg"
                        active={true}
                        style={{ 
                            position: 'relative', 
                            zIndex: 1,
                            objectFit: 'cover',
                            border: '4px solid var(--bratz-pink)',
                            boxShadow: '0 0 30px rgba(255,0,127,0.5)'
                        }}
                    />
                 </div>

                <h1 className="brand-font mobile-h1" style={{
                    fontSize: 'clamp(2.2rem, 10vw, 3.5rem)',
                    marginTop: '1.5rem',
                    marginBottom: '0.5rem',
                    background: 'linear-gradient(135deg, #fff 0%, #ff9ed2 50%, #c084fc 100%)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                    filter: 'drop-shadow(0 10px 20px rgba(176,38,255,0.4))',
                }}>
                    {t(`colorTypes.${colorType}`, config.name)}
                </h1>
                {summary && (
                    <p style={{ fontSize: '1.2rem', color: '#f0e6ff', marginBottom: '1rem', fontStyle: 'italic' }}>
                        {t(summary, summary)}
                    </p>
                )}
                <p style={{ fontSize: '1.1rem', color: '#9d7ab8', letterSpacing: '2px', textTransform: 'uppercase' }}>
                    {t(`seasons.${season}`, season)} • {t(`bodyTypes.${result?.bodyShape || metadata?.bodyShape}`, result?.bodyShape || metadata?.bodyShape || '')}
                </p>
            </div>

            {/* Analysis Grid */}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))', gap: '1rem', marginBottom: '2.5rem' }}>
                {[
                    { label: t('analysis.colorTypeTitle', 'Color Type'), value: t(`colorTypes.${colorType}`, colorType.replace('_', ' ')) },
                    { label: t('result.result.undertone', 'Undertone'), value: t(`result.undertones.${undertone.toUpperCase()}`, undertone) },
                    { label: t('result.result.contrast', 'Contrast'), value: t(`result.contrastLevels.${contrast.toUpperCase()}`, contrast) },
                    result?.depth && { label: t('result.result.depth', 'Depth'), value: t(`result.depths.${result.depth.toUpperCase()}`, result.depth) },
                    result?.chroma && { label: t('result.result.chroma', 'Chroma'), value: t(`result.chromas.${result.chroma.toUpperCase()}`, result.chroma) },
                ].filter(Boolean).map(({ label, value }) => (
                    <div key={label} className="glass-panel" style={{ padding: '1rem', textAlign: 'center' }}>
                        <span style={{ display: 'block', fontSize: '0.65rem', color: 'var(--bratz-pink)', letterSpacing: '1px', marginBottom: '0.5rem' }}>{label}</span>
                        <span style={{ fontSize: '0.95rem', fontWeight: 800, color: '#fff' }}>{value ? value.toString().replace('_', ' ') : '—'}</span>
                    </div>
                ))}
            </div>

            {/* Palette section */}
            <div className="glass-panel" style={{ padding: '2rem', marginBottom: '2.5rem' }}>
                <h3 className="brand-font" style={{ fontSize: '1.5rem', color: '#fff', marginBottom: '1.5rem', textAlign: 'center' }}>
                    {t('result.result.signaturePalette', 'Your Signature Palette')}
                </h3>
                <div style={{ display: 'flex', gap: '8px', height: 'clamp(80px, 15vh, 110px)', flexWrap: 'wrap' }}>
                    {(palette || []).map((color, i) => (
                        <div key={i} className="shine-effect color-swatch" style={{
                            flex: 1, minWidth: '35px', backgroundColor: color, borderRadius: '12px',
                            boxShadow: '0 8px 30px rgba(0,0,0,0.4)', transition: 'all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275)',
                            cursor: 'pointer', zIndex: 1, position: 'relative', overflow: 'hidden',
                            display: 'flex', alignItems: 'flex-end', justifyContent: 'center', paddingBottom: '10px'
                        }} 
                        onMouseEnter={e => { 
                            e.currentTarget.style.flex = '2.5'; 
                            e.currentTarget.style.transform = 'translateY(-10px)';
                            e.currentTarget.querySelector('.hex-label').style.opacity = '1';
                        }}
                        onMouseLeave={e => { 
                            e.currentTarget.style.flex = '1'; 
                            e.currentTarget.style.transform = 'translateY(0)';
                            e.currentTarget.querySelector('.hex-label').style.opacity = '0';
                        }}
                        >
                            <span className="hex-label" style={{
                                opacity: 0, fontSize: '0.7rem', fontWeight: 'bold', color: '#fff',
                                backgroundColor: 'rgba(0,0,0,0.5)', padding: '4px 8px', borderRadius: '4px',
                                transition: 'opacity 0.3s ease', textShadow: '0 1px 2px rgba(0,0,0,0.5)'
                            }}>
                                {color}
                            </span>
                        </div>
                    ))}
                </div>
            </div>

            {/* Advice section */}
            <div className="glass-panel" style={{ 
                padding: '2.5rem', 
                marginBottom: '3rem',
                borderLeft: '4px solid var(--bratz-pink)',
                background: 'linear-gradient(to right, rgba(255,0,127,0.1), transparent)'
            }}>
                <h3 style={{ fontSize: '0.8rem', color: 'var(--bratz-pink)', textTransform: 'uppercase', letterSpacing: '2px', marginBottom: '1rem' }}>
                    {t('result.result.personalAdvice', 'AI Style Advice')}
                </h3>
                <p style={{ fontSize: '1.1rem', lineHeight: 1.8, color: '#f0e6ff', fontStyle: 'italic' }}>
                    "{t(advice, advice)}"
                </p>

                {recommendations.length > 0 && (
                    <div style={{ marginTop: '2rem', borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '1.5rem' }}>
                        <h4 style={{ fontSize: '0.9rem', color: 'rgba(255,255,255,0.4)', textTransform: 'uppercase', letterSpacing: '1px', marginBottom: '1rem' }}>
                            {t('bodyShape.topRecommendations', 'Styling Tips')}
                        </h4>
                        <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'grid', gap: '0.8rem' }}>
                            {recommendations.map((tip, idx) => (
                                <li key={idx} style={{ 
                                    display: 'flex', gap: '12px', fontSize: '0.95rem', color: '#fff',
                                    alignItems: 'flex-start', background: 'rgba(255,255,255,0.03)',
                                    padding: '0.8rem 1rem', borderRadius: '8px'
                                }}>
                                    <span style={{ color: 'var(--bratz-pink)', fontWeight: 'bold' }}>✦</span>
                                    {t(tip, tip)}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            {/* Actions */}
            <div className="mobile-stack" style={{ display: 'flex', gap: '1.5rem' }}>
                <button className="bratz-btn primary" onClick={() => window.location.href = '/profile'} style={{ flex: 1, width: '100%' }}>
                    {t('bodyShape.viewProfileBtn', 'View Profile')}
                </button>
                <button className="bratz-btn secondary" onClick={onReset} style={{ flex: 1, width: '100%' }}>
                    {t('bodyShape.retakeTestBtn', 'Try Again ✨')}
                </button>
            </div>
        </div>
    );
};

export default ResultCard;
