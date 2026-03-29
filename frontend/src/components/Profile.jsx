import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import userService from '../services/userService';
import '../index.css';
import Header from './Header';
import LanguageSwitcher, { LANGUAGES } from './LanguageSwitcher';

// ── Helper Components ──────────────────────────────────────────────────────
const InfoItem = ({ label, value, icon }) => (
    <div style={{
        background: 'rgba(255,255,255,0.05)',
        padding: '1rem',
        borderRadius: '12px',
        border: '1px solid rgba(255,255,255,0.1)',
        display: 'flex',
        flexDirection: 'column',
        gap: '4px',
        flex: '1 1 140px'
    }}>
        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
            {icon} {label}
        </span>
        <span style={{ fontSize: '1.1rem', fontWeight: '600', color: '#fff' }}>
            {value || '—'}
        </span>
    </div>
);

const ResultHistoryCard = ({ res, onDelete, t }) => {
    let metadata = {};
    try {
        if (typeof res.metadata === 'string') {
            metadata = JSON.parse(res.metadata) || {};
        } else if (res.metadata && typeof res.metadata === 'object') {
            metadata = res.metadata;
        }
    } catch (e) {
        console.error("Failed to parse metadata for result ID:", res.id, e);
    }

    const summary = metadata?.summary || res?.summary || (typeof res.metadata === 'string' && !res.metadata.startsWith('{') ? res.metadata : '');
    const advice = (metadata?.advice || res?.personalizedAdvice || metadata?.personalizedAdvice);

    return (
        <div style={{
            background: 'rgba(255,255,255,0.03)',
            borderRadius: 20,
            padding: '1.5rem',
            border: '1px solid rgba(255,255,255,0.1)',
            marginBottom: '1rem',
            transition: 'all 0.3s ease',
            position: 'relative'
        }} className="glass-card result-item">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
                <div>
                    <span style={{
                        fontSize: '0.7rem',
                        background: res.testType === 'COLOR_TYPE' ? 'var(--bratz-pink)' : '#a855f7',
                        padding: '3px 10px',
                        borderRadius: '20px',
                        textTransform: 'uppercase',
                        fontWeight: '800',
                        marginRight: '10px'
                    }}>
                        {res.testType === 'COLOR_TYPE' ? t('profile.colorHistory') : t('profile.bodyShape')}
                    </span>
                    <span style={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.8rem' }}>
                        {new Date(res.createdAt).toLocaleDateString()}
                    </span>
                </div>
                <button
                    onClick={onDelete}
                    style={{
                        background: 'rgba(239,68,68,0.1)',
                        border: 'none',
                        color: '#ef4444',
                        cursor: 'pointer',
                        width: '28px',
                        height: '28px',
                        borderRadius: '50%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '0.8rem'
                    }}
                    title={t('common.delete', 'Delete')}
                >
                    ✕
                </button>
            </div>

            <div style={{ fontSize: '1.4rem', fontWeight: 'bold', color: '#fff', marginBottom: '0.5rem' }}>
                {res.testType === 'COLOR_TYPE' 
                    ? t(`colorTypes.${res.result}`, res.result?.replace(/_/g, ' ')) 
                    : t(`bodyTypes.${res.result}`, res.result?.replace(/_/g, ' '))}
            </div>

            {summary && (
                <div style={{ marginBottom: '1rem' }}>
                    <p style={{
                        margin: 0,
                        fontSize: '0.95rem',
                        color: 'rgba(255,255,255,0.8)',
                        lineHeight: '1.5',
                        fontStyle: 'italic'
                    }}>
                        "{t(summary, summary)}"
                    </p>
                </div>
            )}

            {/* AI Advice Section */}
            {advice && (
                <div style={{
                    background: 'rgba(168,85,247,0.08)',
                    padding: '1rem',
                    borderRadius: '12px',
                    border: '1px solid rgba(168,85,247,0.2)',
                    marginTop: '1rem'
                }}>
                    <div style={{ fontSize: '0.8rem', color: '#c084fc', fontWeight: 'bold', marginBottom: '0.5rem', textTransform: 'uppercase' }}>
                        ✨ {t('result.aiStylingAdvice', 'AI Styling Advice')}
                    </div>
                    <p style={{ margin: 0, fontSize: '0.9rem', color: 'rgba(255,255,255,0.9)', lineHeight: '1.4' }}>
                        {t(advice, advice)}
                    </p>
                </div>
            )}

            {/* Color Palette - Only for COLOR_TYPE */}
            {res.testType === 'COLOR_TYPE' && metadata?.palette && Array.isArray(metadata.palette) && (
                <div style={{ marginTop: '1.5rem' }}>
                    <div style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.8rem', fontWeight: '600' }}>
                        {t('result.recommendedPalette', 'Recommended Palette')}
                    </div>
                    <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                        {metadata.palette.map((color, idx) => (
                            <div
                                key={idx}
                                style={{
                                    width: '36px',
                                    height: '36px',
                                    backgroundColor: color,
                                    borderRadius: '8px',
                                    border: '1px solid rgba(255,255,255,0.2)',
                                    boxShadow: '0 4px 12px rgba(0,0,0,0.4)',
                                    cursor: 'help',
                                    transition: 'transform 0.2s ease'
                                }}
                                onMouseEnter={e => e.currentTarget.style.transform = 'scale(1.2) rotate(5deg)'}
                                onMouseLeave={e => e.currentTarget.style.transform = 'scale(1) rotate(0deg)'}
                                title={color}
                            />
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

const Profile = () => {
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();

    // ── Profile state ──────────────────────────────────────────────────────────
    const [profile, setProfile] = useState(null);
    const [colorResults, setColorResults] = useState([]);
    const [bodyShapeResults, setBodyShapeResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [resultsLoading, setResultsLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState('');

    // ── Edit profile state ─────────────────────────────────────────────────────
    const [showEditModal, setShowEditModal] = useState(false);
    const [editForm, setEditForm] = useState({
        name: '',
        age: '',
        gender: '',
        height: '',
        weight: '',
        preferredLanguage: '',
        avatar: '',
        clothingSize: ''
    });
    const [isSaving, setIsSaving] = useState(false);

    // ── Delete modal state ─────────────────────────────────────────────────────
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showResultDeleteModal, setShowResultDeleteModal] = useState(false);
    const [resultToDelete, setResultToDelete] = useState(null);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        setLoading(true);
        setErrorMsg('');
        try {
            const profileRes = await userService.getProfile();
            const pData = profileRes?.data ?? profileRes;
            if (!pData || (!pData.id && !pData.email)) {
                throw new Error("Profile data is incomplete. Please try logging in again.");
            }
            
            setProfile(pData);
            setEditForm({
                name: pData?.name || '',
                age: pData?.age || '',
                gender: pData?.gender || '',
                height: pData?.height || '',
                weight: pData?.weight || '',
                preferredLanguage: pData?.preferredLanguage || pData?.language || 'en',
                avatar: pData?.avatar || '',
                clothingSize: pData?.clothingSize || ''
            });

            try {
                const resultsRes = await userService.getResults();
                const resultsData = resultsRes?.data ?? resultsRes;
                setColorResults(resultsData?.colorTypeResults ?? []);
                setBodyShapeResults(resultsData?.bodyShapeResults ?? []);
            } catch (resErr) {
                console.error("Failed to fetch test results:", resErr);
            }
        } catch (err) {
            console.error("Fetch Data Error:", err);
            setErrorMsg(err.response?.data?.message || err.message || t('errors.generic'));
        } finally {
            setLoading(false);
            setResultsLoading(false);
        }
    };

    const handleAvatarChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            if (file.size > 10 * 1024 * 1024) { // 10MB limit
                setErrorMsg("Image size too large. Please select an image under 10MB.");
                return;
            }
            const reader = new FileReader();
            reader.onloadend = () => {
                setEditForm(prev => ({ ...prev, avatar: reader.result }));
            };
            reader.readAsDataURL(file);
        }
    };

    const handleSaveProfile = async (e) => {
        e.preventDefault();
        setIsSaving(true);
        setErrorMsg('');
        try {
            const profileToUpdate = {
                name: editForm.name,
                email: editForm.email,
                gender: editForm.gender,
                age: editForm.age ? parseInt(editForm.age) : null,
                height: editForm.height ? parseFloat(editForm.height) : null,
                weight: editForm.weight ? parseFloat(editForm.weight) : null,
                preferredLanguage: editForm.preferredLanguage,
                avatar: editForm.avatar,
                clothingSize: editForm.clothingSize
            };
            console.log("Sending profile update:", profileToUpdate);
            const res = await userService.updateProfile(profileToUpdate);
            const updatedProfile = res.data || res;
            console.log("Received updated profile from backend:", updatedProfile);
            setProfile(updatedProfile);
            
            // Sync i18n if language was changed (though we removed it from modal, it might still change via other means)
            if (updatedProfile.preferredLanguage && updatedProfile.preferredLanguage !== i18n.language) {
                i18n.changeLanguage(updatedProfile.preferredLanguage);
            }
            
            setShowEditModal(false);
        } catch (err) {
            console.error("Critical Profile Save Error:", err);
            if (err.response) {
                console.error("Error response data:", err.response.data);
                console.error("Error status:", err.response.status);
            }
            setErrorMsg(err.response?.data?.message || err.message || "Failed to update profile");
            alert("Error: " + (err.response?.data?.message || err.message));
        } finally {
            setIsSaving(false);
        }
    };

    const handleDeleteClick = (id) => {
        setResultToDelete(id);
        setShowResultDeleteModal(true);
    };

    const handleConfirmResultDelete = async () => {
        const idToDelete = resultToDelete;
        setShowResultDeleteModal(false);
        try {
            if (idToDelete) {
                setColorResults(prev => prev.filter(r => r.id !== idToDelete));
                setBodyShapeResults(prev => prev.filter(r => r.id !== idToDelete));
                await userService.deleteResult(idToDelete);
            } else {
                const userId = profile?.id;
                setColorResults([]);
                setBodyShapeResults([]);
                await userService.deleteAllResults(userId);
            }
        } catch (err) {
            console.error("Delete Error:", err);
            setErrorMsg(t('errors.generic'));
            fetchData();
        } finally {
            setResultToDelete(null);
        }
    };

    if (loading) {
        return (
            <div className="analysis-container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh' }}>
                <div className="spinner-container">
                    <div className="spinner" />
                    <p style={{ color: 'var(--text-muted)', marginTop: '1rem' }}>{t('loading', 'Loading profile…')}</p>
                </div>
            </div>
        );
    }

    if (errorMsg && !profile) {
        return (
            <div className="analysis-container">
                <Header />
                <div className="bratz-card" style={{ maxWidth: 600, margin: '2rem auto', textAlign: 'center', padding: '3rem' }}>
                    <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>😿</div>
                    <h2 className="brand-font">{t('not Loaded', 'Profile not loaded')}</h2>
                    <p style={{ color: 'var(--text-muted)', marginBottom: '2rem' }}>{errorMsg}</p>
                    <button className="bratz-btn" onClick={fetchData}>🔄 {t('retry', 'Retry')}</button>
                </div>
            </div>
        );
    }

    const isUrlAvatar = (avatar) => {
        if (!avatar) return false;
        // Check for common URL/path prefixes or file extensions
        return avatar.startsWith('/') || 
               avatar.startsWith('data:') || 
               avatar.startsWith('http') || 
               avatar.includes('.png') || 
               avatar.includes('.jpg') || 
               avatar.includes('.webp');
    };

    return (
        <div className="analysis-container">
            <Header />

            <div className="mobile-stack" style={{ maxWidth: 1000, margin: '0 auto', display: 'grid', gridTemplateColumns: '1fr 1.5fr', gap: '2rem', alignItems: 'start' }}>

                {/* ── Left Column: User Info ── */}
                <div className="mobile-no-sticky" style={{ position: 'sticky', top: '2rem' }}>
                    <div className="bratz-card glass-card" style={{ padding: '2rem', marginBottom: '1.5rem' }}>
                        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
                            <div style={{
                                width: '100px',
                                height: '100px',
                                background: isUrlAvatar(profile?.avatar) 
                                    ? `url("${profile.avatar.startsWith('/') || profile.avatar.startsWith('http') || profile.avatar.startsWith('data:') ? '' : '/'}${profile.avatar}") center/cover` 
                                    : 'linear-gradient(135deg, #ff007f, #7000ff)',
                                borderRadius: '50%',
                                margin: '0 auto 1rem',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: profile?.avatar && !isUrlAvatar(profile.avatar) 
                                    ? '3.5rem' 
                                    : (profile?.avatar ? '0' : '3rem'),
                                border: '4px solid rgba(255,255,255,0.15)',
                                boxShadow: '0 10px 40px rgba(255,0,127,0.4)',
                                overflow: 'hidden',
                                color: '#fff',
                                position: 'relative'
                            }}>
                                {profile?.avatar && !isUrlAvatar(profile.avatar) 
                                    ? profile.avatar 
                                    : (!profile?.avatar ? (profile?.name?.[0] || '✨') : null)}
                            </div>
                            <h2 className="brand-font" style={{ margin: 0, fontSize: '1.8rem' }}>{profile?.name || t('profile.user')}</h2>
                            <p style={{ color: 'rgba(255,255,255,0.5)', margin: '4px 0 0 0' }}>{profile?.email}</p>
                            {profile?.clothingSize && (
                                <div style={{ 
                                    display: 'inline-block', 
                                    marginTop: '8px', 
                                    padding: '2px 12px', 
                                    background: 'rgba(255,255,255,0.1)', 
                                    borderRadius: '12px',
                                    fontSize: '0.8rem',
                                    fontWeight: 'bold',
                                    color: 'var(--bratz-pink)'
                                }}>
                                    Size: {profile.clothingSize}
                                </div>
                            )}
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(140px, 1fr))', gap: '0.8rem', marginBottom: '2rem' }}>
                            <InfoItem icon="🎂" label={t('age')} value={profile?.age} />
                            <InfoItem icon="⚧" label={t('gender', 'Gender')} value={profile?.gender} />
                            <InfoItem icon="📏" label={t('height')} value={profile?.height ? `${profile.height} cm` : t('common.na', '—')} />
                            <InfoItem icon="⚖️" label={t('weight')} value={profile?.weight ? `${profile.weight} kg` : t('common.na', '—')} />
                            <InfoItem icon="👕" label={t('clothingSize', 'Size')} value={profile?.clothingSize} />
                        </div>

                        <button
                            className="bratz-btn ghost"
                            style={{ 
                                width: '100%', 
                                marginBottom: '1rem',
                                position: 'relative',
                                zIndex: 10,
                                transform: 'translateZ(0)' // Force composite layer to fix potential hover/click issues
                            }}
                            onClick={() => {
                                console.log("Edit Profile clicked!");
                                setShowEditModal(true);
                            }}
                        >
                            {t('edit Profile', 'Edit Profile')}
                        </button>
                    </div>

                    <div className="bratz-card glass-card" style={{ padding: '1.5rem' }}>
                        <h4 style={{ margin: '0 0 1rem 0', fontSize: '0.9rem', color: '#ff007f', textTransform: 'uppercase' }}>
                            {t('settings', 'Account Settings')}
                        </h4>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '0.8rem' }}>
                            <LanguageSwitcher 
                                style={{ width: '100%' }}
                                buttonStyle={{ 
                                    width: '100%', 
                                    justifyContent: 'flex-start',
                                    background: 'rgba(255,255,255,0.05)',
                                    border: '1px solid rgba(255,255,255,0.1)',
                                    borderRadius: '12px',
                                    padding: '0.8rem 1rem',
                                    height: '45px'
                                }}
                            />
                            <button
                                className="bratz-btn ghost small"
                                style={{ border: '1px solid rgba(239,68,68,0.3)', color: '#ef4444' }}
                                onClick={() => setShowDeleteModal(true)}
                            >
                                🗑️ {t('delete Account')}
                            </button>
                        </div>
                    </div>
                </div>

                {/* ── Right Column: Results ── */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>

                    {/* Color Type Section */}
                    <div className="bratz-card glass-card">
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem' }}>
                            <h3 className="brand-font" style={{ margin: 0, fontSize: '1.5rem' }}>
                                🎨 {t('profile.colorHistory')}
                            </h3>
                            {colorResults.length > 0 && (
                                <button
                                    className="bratz-btn ghost xsmall"
                                    onClick={() => handleDeleteClick(null)}
                                >
                                    {t('clear', 'Clear')}
                                </button>
                            )}
                        </div>

                        {resultsLoading ? (
                            <p style={{ color: 'rgba(255,255,255,0.3)' }}>{t('loading', 'Loading...')}</p>
                        ) : colorResults.length === 0 ? (
                            <div style={{ textAlign: 'center', padding: '2rem', color: 'rgba(255,255,255,0.4)', background: 'rgba(255,255,255,0.02)', borderRadius: '12px', border: '1px dashed rgba(255,255,255,0.1)' }}>
                                {t('profile.noResultsYet', 'No results yet')}
                            </div>
                        ) : (
                            colorResults.map(res => (
                                <ResultHistoryCard key={res.id} res={res} onDelete={() => handleDeleteClick(res.id)} t={t} />
                            ))
                        )}
                    </div>

                    {/* Body Shape Section */}
                    <div className="bratz-card glass-card">
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem' }}>
                            <h3 className="brand-font" style={{ margin: 0, fontSize: '1.5rem' }}>
                                ⏳ {t('profile.bodyShape')}
                            </h3>
                            {bodyShapeResults.length > 0 && (
                                <button
                                    className="bratz-btn ghost xsmall"
                                    onClick={() => handleDeleteClick(null)}
                                >
                                    {t('clear', 'Clear')}
                                </button>
                            )}
                        </div>

                        {resultsLoading ? (
                            <p style={{ color: 'rgba(255,255,255,0.3)' }}>{t('loading', 'Loading...')}</p>
                        ) : bodyShapeResults.length === 0 ? (
                            <div style={{ textAlign: 'center', padding: '2rem', color: 'rgba(255,255,255,0.4)', background: 'rgba(255,255,255,0.02)', borderRadius: '12px', border: '1px dashed rgba(255,255,255,0.1)' }}>
                                {t('no Results', 'No results yet')}
                            </div>
                        ) : (
                            bodyShapeResults.map(res => (
                                <ResultHistoryCard key={res.id} res={res} onDelete={() => handleDeleteClick(res.id)} t={t} />
                            ))
                        )}
                    </div>
                </div>
            </div>

            {/* ── Edit Profile Modal ── */}
            {showEditModal && (
                <div style={{ 
                    position: 'fixed', 
                    inset: 0, 
                    background: 'rgba(0,0,0,0.85)', 
                    backdropFilter: 'blur(16px)', 
                    zIndex: 1100, 
                    display: 'flex', 
                    alignItems: 'flex-start', // Better for long modals
                    justifyContent: 'center', 
                    padding: 'clamp(20px, 5vw, 40px)', // Responsive padding
                    overflowY: 'auto' // Allow vertical scrolling on the overlay
                }}>
                    <div className="bratz-card glass-card bratz-modal-content" style={{ 
                        maxWidth: 600, 
                        width: '100%', 
                        padding: '2.5rem', 
                        overflow: 'visible', // Allow avatar presets/dropdowns to pop out
                        margin: 'auto 0' // Vertically center within the scrollable area if possible
                    }}>
                        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
                            <div style={{ fontSize: '2.5rem', marginBottom: '0.5rem' }}>✨</div>
                            <h2 className="brand-font" style={{ margin: 0 }}>{t('edit Profile', 'Edit Profile')}</h2>
                        </div>

                        <form onSubmit={handleSaveProfile} className="profile-edit-form">
                            <div style={{ gridColumn: 'span 2' }}>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('avatarOptions', 'Avatar Options')}</label>
                                
                                {/* AI Presets Row */}
                                <div style={{ 
                                    display: 'flex', 
                                    gap: '10px', 
                                    marginBottom: '1rem', 
                                    overflowX: 'auto', 
                                    paddingBottom: '8px',
                                    WebkitOverflowScrolling: 'touch',
                                    scrollbarWidth: 'none',
                                    msOverflowStyle: 'none'
                                }} className="hide-scrollbar">
                                    {[
                                        { id: 'preset1', url: '/avatars/preset1.png', label: 'AI Queen' },
                                        { id: 'emoji1', emoji: '👸', label: 'Classic' },
                                        { id: 'emoji2', emoji: '🦸‍♀️', label: 'Hero' },
                                        { id: 'emoji3', emoji: '🧝‍♀️', label: 'Mystic' },
                                        { id: 'emoji4', emoji: '💖', label: 'Glam' }
                                    ].map(preset => {
                                        const isSelected = editForm.avatar === (preset.url || preset.emoji);
                                        return (
                                            <div 
                                                key={preset.id}
                                                onClick={() => {
                                                    if (preset.url) {
                                                        setEditForm(prev => ({ ...prev, avatar: preset.url }));
                                                    } else {
                                                        setEditForm(prev => ({ ...prev, avatar: preset.emoji })); 
                                                    }
                                                }}
                                                style={{
                                                    width: '60px',
                                                    height: '60px',
                                                    borderRadius: '16px',
                                                    background: preset.url ? `url("${preset.url}") center/cover` : 'rgba(255,255,255,0.08)',
                                                    border: isSelected ? '3px solid var(--bratz-pink)' : '1.5px solid rgba(255,255,255,0.15)',
                                                    cursor: 'pointer',
                                                    flexShrink: 0,
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center',
                                                    fontSize: '1.8rem',
                                                    transition: 'all 0.3s cubic-bezier(0.175, 0.885, 32, 1.275)',
                                                    transform: isSelected ? 'scale(1.1)' : 'scale(1)',
                                                    boxShadow: isSelected ? '0 0 20px rgba(255,0,127,0.5)' : 'none',
                                                    position: 'relative',
                                                    overflow: 'visible'
                                                }}
                                                title={preset.label}
                                            >
                                                {!preset.url && preset.emoji}
                                                {isSelected && (
                                                    <div style={{
                                                        position: 'absolute',
                                                        top: '-8px',
                                                        right: '-8px',
                                                        background: 'var(--bratz-pink)',
                                                        borderRadius: '50%',
                                                        width: '20px',
                                                        height: '20px',
                                                        display: 'flex',
                                                        alignItems: 'center',
                                                        justifyContent: 'center',
                                                        fontSize: '0.7rem',
                                                        border: '2px solid #000',
                                                        boxShadow: '0 2px 5px rgba(0,0,0,0.5)'
                                                    }}>✓</div>
                                                )}
                                            </div>
                                        );
                                    })}
                                </div>

                                <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                                    <div style={{
                                        width: '60px',
                                        height: '60px',
                                        background: isUrlAvatar(editForm.avatar) 
                                            ? `url("${editForm.avatar}") center/cover` 
                                            : 'rgba(255,255,255,0.05)',
                                        borderRadius: '50%',
                                        border: '1px solid rgba(255,255,255,0.1)',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        fontSize: '1.5rem',
                                        overflow: 'hidden',
                                        color: '#fff'
                                    }}>
                                        {editForm.avatar && !isUrlAvatar(editForm.avatar) 
                                            ? editForm.avatar 
                                            : (!editForm.avatar ? '👤' : null)}
                                    </div>
                                    <div style={{ flex: 1 }}>
                                        <p style={{ margin: '0 0 5px 0', fontSize: '0.7rem', color: 'rgba(255,255,255,0.4)' }}>{t('uploadOwn', 'Or upload your own photo')}</p>
                                        <label htmlFor="pc-avatar-upload" className="bratz-btn ghost small" style={{ 
                                            display: 'inline-flex', 
                                            cursor: 'pointer', 
                                            padding: '8px 16px',
                                            fontSize: '0.75rem',
                                            border: '1px solid rgba(255,0,127,0.4)'
                                        }}>
                                            📸 {t('chooseFile', 'Choose Photo')}
                                        </label>
                                        <input
                                            id="pc-avatar-upload"
                                            type="file"
                                            accept="image/*"
                                            onChange={handleAvatarChange}
                                            style={{ display: 'none' }}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div style={{ gridColumn: 'span 2' }}>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('name', 'Name')}</label>
                                <input
                                    type="text"
                                    className="bratz-input"
                                    style={{ width: '100%', padding: '0.8rem' }}
                                    value={editForm.name}
                                    onChange={(e) => setEditForm({ ...editForm, name: e.target.value })}
                                />
                            </div>
                            <div>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('age', 'Age')}</label>
                                <input
                                    type="number"
                                    className="bratz-input"
                                    style={{ width: '100%', padding: '0.8rem' }}
                                    value={editForm.age}
                                    onChange={(e) => setEditForm({ ...editForm, age: e.target.value })}
                                />
                            </div>
                            <div>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('gender', 'Gender')}</label>
                                <select
                                    className="bratz-input"
                                    style={{ width: '100%', padding: '0.8rem', height: '45px' }}
                                    value={editForm.gender}
                                    onChange={(e) => setEditForm({ ...editForm, gender: e.target.value })}
                                >
                                    <option value="">{t('select', 'Select')}</option>
                                    <option value="FEMALE">{t('female', 'Female')}</option>
                                    <option value="MALE">{t('male', 'Male')}</option>
                                    <option value="OTHER">{t('other', 'Other')}</option>
                                </select>
                            </div>
                            <div>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('height', 'Height')} (cm)</label>
                                <input
                                    type="number"
                                    step="0.1"
                                    className="bratz-input"
                                    style={{ width: '100%', padding: '0.8rem' }}
                                    value={editForm.height}
                                    onChange={(e) => setEditForm({ ...editForm, height: e.target.value })}
                                />
                            </div>
                            <div>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('weight', 'Weight')} (kg)</label>
                                <input
                                    type="number"
                                    step="0.1"
                                    className="bratz-input"
                                    style={{ width: '100%', padding: '0.8rem' }}
                                    value={editForm.weight}
                                    onChange={(e) => setEditForm({ ...editForm, weight: e.target.value })}
                                />
                            </div>
                            <div>
                                <label style={{ display: 'block', fontSize: '0.75rem', color: 'rgba(255,255,255,0.5)', marginBottom: '0.5rem', textTransform: 'uppercase' }}>{t('clothingSize', 'Clothing Size')}</label>
                                <select
                                    className="bratz-input"
                                    style={{ width: '100%', padding: '0.8rem', height: '45px' }}
                                    value={editForm.clothingSize}
                                    onChange={(e) => setEditForm({ ...editForm, clothingSize: e.target.value })}
                                >
                                    <option value="">{t('selectSize', 'Select Size')}</option>
                                    {['XXS', 'XS', 'S', 'M', 'L', 'XL', 'XXL', 'XXXL'].map(size => (
                                        <option key={size} value={size}>{t(`sizes.${size}`, size)}</option>
                                    ))}
                                </select>
                            </div>
                            
                            <div style={{ gridColumn: 'span 2', marginTop: '1.5rem', display: 'flex', gap: '1rem' }}>
                                <button type="button" className="bratz-btn ghost" style={{ flex: 1 }} onClick={() => setShowEditModal(false)}>{t('cancel', 'Cancel')}</button>
                                <button type="submit" className="bratz-btn" style={{ flex: 1 }} disabled={isSaving}>
                                    {isSaving ? '...' : t('save', 'Save')}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
            {/* ── Delete Account Modal ── */}
            {showDeleteModal && (
                <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', backdropFilter: 'blur(8px)', zIndex: 1200, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
                    <div className="bratz-card" style={{ maxWidth: 400, textAlign: 'center' }}>
                        <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>⚠️</div>
                        <h3 className="brand-font" style={{ color: '#ef4444' }}>{t('delete Account Title', 'Delete Account?')}</h3>
                        <p style={{ color: 'var(--text-muted)', marginBottom: '2rem' }}>{t('delete Account Msg', 'This will permanently remove your profile and all test history. This cannot be undone.')}</p>
                        <div style={{ display: 'flex', gap: '1rem' }}>
                            <button className="bratz-btn ghost" style={{ flex: 1 }} onClick={() => setShowDeleteModal(false)}>{t('cancel')}</button>
                            <button className="bratz-btn" style={{ flex: 1, background: '#ef4444' }} onClick={async () => {
                                try {
                                    await userService.deleteAccount();
                                    localStorage.removeItem('token');
                                    navigate('/login');
                                } catch (e) {
                                    setErrorMsg("Failed to delete account");
                                }
                            }}>
                                {t('delete', 'Delete Forever')}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {/* ── Delete Result Confirmation Modal ── */}
            {showResultDeleteModal && (
                <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', backdropFilter: 'blur(8px)', zIndex: 1200, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
                    <div className="bratz-card" style={{ maxWidth: 400, textAlign: 'center' }}>
                        <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🗑️</div>
                        <h3 className="brand-font" style={{ color: '#ef4444' }}>
                            {resultToDelete ? t('delete Result Title', 'Delete Result?') : t('clear History Title', 'Clear All History?')}
                        </h3>
                        <p style={{ color: 'var(--text-muted)', marginBottom: '2rem' }}>
                            {resultToDelete 
                                ? t('delete Result Msg', 'Are you sure you want to delete this specific test result?') 
                                : t('clear History Msg', 'This will permanently remove all your test history. This cannot be undone.')}
                        </p>
                        <div style={{ display: 'flex', gap: '1rem' }}>
                            <button className="bratz-btn ghost" style={{ flex: 1 }} onClick={() => setShowResultDeleteModal(false)}>{t('cancel')}</button>
                            <button className="bratz-btn" style={{ flex: 1, background: '#ef4444' }} onClick={handleConfirmResultDelete}>
                                {t('delete', 'Delete')}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Profile;
