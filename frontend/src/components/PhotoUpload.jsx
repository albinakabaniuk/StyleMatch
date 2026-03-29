import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { analyzePhoto } from '../services/analysisService';
import ResultCard from './ResultCard';

const PhotoUpload = () => {
    const { t, i18n } = useTranslation();
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [statusMsg, setStatusMsg] = useState('');
    const [preview, setPreview] = useState(null);
    const [result, setResult] = useState(null);

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            if (selectedFile.size > 15 * 1024 * 1024) {
                setError('analysis.errorSize');
                return;
            }
            setFile(selectedFile);
            setPreview(URL.createObjectURL(selectedFile));
            setError('');
        }
    };

    const handleUpload = async () => {
        if (!file) {
            setError('analysis.errorNoPhoto');
            return;
        }
        setLoading(true);
        setStatusMsg('analysis.uploading');
        
        try {
            const statusSequence = [
                { msg: 'analysis.analyzingColors', delay: 1500 },
                { msg: 'analysis.identifyingUndertone', delay: 3000 },
                { msg: 'analysis.matchingPalette', delay: 4500 }
            ];

            statusSequence.forEach(({ msg, delay }) => {
                setTimeout(() => {
                    if (loading) setStatusMsg(msg);
                }, delay);
            });

            const data = await analyzePhoto(file, i18n.language);
            setResult(data);
        } catch (err) {
            console.error('Photo analysis failed:', err);
            setError('analysis.errorFail');
        } finally {
            setLoading(false);
            setStatusMsg('');
        }
    };

    return (
        <div style={{ animation: 'slideUp 0.6s cubic-bezier(0.16, 1, 0.3, 1)' }}>
            {!result ? (
                <div className="bratz-card glass-card" style={{ 
                    textAlign: 'center', 
                    border: '1.5px solid var(--bratz-pink)',
                    background: 'rgba(20, 5, 40, 0.7)',
                    boxShadow: '0 20px 50px rgba(255, 0, 127, 0.2)'
                }}>
                    <h2 className="brand-font" style={{ fontSize: '2rem', marginBottom: '1.5rem' }}>
                        {t('analysis.photoScanTitle', 'AI Photo Scan')}
                    </h2>
                    
                    <p style={{ color: '#fff', opacity: 0.8, marginBottom: '2rem', fontSize: '0.95rem', lineHeight: '1.6' }}>
                        {t('analysis.photoScanDesc', 'Upload a well-lit portrait photo for instant AI analysis of your seasonal color type.')}
                    </p>

                    {error && <p className="error-message" style={{ marginBottom: '1.5rem' }}>{t(error, error)}</p>}

                    <div 
                        className="neon-border"
                        style={{
                            border: '2px dashed rgba(176, 38, 255, 0.4)',
                            borderRadius: '20px',
                            padding: '30px',
                            cursor: 'pointer',
                            transition: 'all 0.3s',
                            background: preview ? 'transparent' : 'rgba(255, 255, 255, 0.03)',
                            position: 'relative',
                            overflow: 'hidden'
                        }}
                        onDragOver={(e) => e.preventDefault()}
                        onDrop={(e) => {
                            e.preventDefault();
                            const droppedFile = e.dataTransfer.files[0];
                            if (droppedFile) {
                                if (droppedFile.size > 15 * 1024 * 1024) {
                                    setError('analysis.errorSize');
                                    return;
                                }
                                setFile(droppedFile);
                                setPreview(URL.createObjectURL(droppedFile));
                                setError('');
                            }
                        }}
                        onClick={() => !loading && document.getElementById('photo-input').click()}
                    >
                        <input 
                            id="photo-input"
                            type="file" 
                            accept="image/*" 
                            onChange={handleFileChange} 
                            style={{ display: 'none' }} 
                        />
                        
                        {preview ? (
                            <img 
                                src={preview} 
                                alt="Preview" 
                                style={{ width: '100%', maxHeight: '300px', objectFit: 'contain', borderRadius: '12px', boxShadow: '0 10px 30px rgba(0,0,0,0.5)' }} 
                            />
                        ) : (
                            <div style={{ padding: '20px 0' }}>
                                <div style={{ fontSize: '3rem', marginBottom: '10px' }}>📸</div>
                                <p style={{ color: 'var(--bratz-pink)', fontWeight: 600 }}>
                                    {t('analysis.clickToUpload', 'Click or Drag Photo')}
                                </p>
                                <p style={{ fontSize: '0.8rem', color: 'rgba(255,255,255,0.4)', marginTop: '8px' }}>
                                    {t('analysis.maxSize', 'Max size: 15MB')}
                                </p>
                            </div>
                        )}
                        
                        {loading && (
                            <div style={{ 
                                position: 'absolute', 
                                inset: 0, 
                                background: 'rgba(10, 0, 21, 0.8)', 
                                display: 'flex', 
                                flexDirection: 'column',
                                alignItems: 'center', 
                                justifyContent: 'center',
                                borderRadius: '18px',
                                zIndex: 10
                            }}>
                                <div className="loading-spinner" style={{ 
                                    width: '50px', 
                                    height: '50px', 
                                    border: '4px solid rgba(176, 38, 255, 0.1)',
                                    borderTop: '4px solid var(--bratz-pink)',
                                    borderRadius: '50%',
                                    animation: 'spin 1s linear infinite',
                                    marginBottom: '15px'
                                }} />
                                <p className="brand-font" style={{ fontSize: '1.1rem', letterSpacing: '1px' }}>
                                    {t(statusMsg || 'analysis.loading')}
                                </p>
                            </div>
                        )}
                    </div>

                    {preview && !loading && (
                        <div style={{ marginTop: '20px', display: 'flex', gap: '12px', justifyContent: 'center' }}>
                           <button 
                                onClick={handleUpload} 
                                className="bratz-btn"
                                style={{ flex: 1 }}
                            >
                                ✨ {t('analysis.startScan', 'Analyze Photo')}
                            </button>
                            <button 
                                onClick={() => { setFile(null); setPreview(null); }}
                                className="bratz-btn ghost"
                                style={{ flex: 0.5 }}
                            >
                                {t('analysis.retake', 'Changed Mind')}
                            </button>
                        </div>
                    )}
                </div>
            ) : (
                <ResultCard 
                    result={{ ...result, uploadedPhoto: preview }} 
                    onReset={() => { setResult(null); setFile(null); setPreview(null); }} 
                />
            )}

            <style>{`
                @keyframes spin {
                    0% { transform: rotate(0deg); }
                    100% { transform: rotate(360deg); }
                }
                .loading-spinner {
                    box-shadow: 0 0 15px rgba(255, 0, 127, 0.3);
                }
            `}</style>
        </div>
    );
};

export default PhotoUpload;
