import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from './LanguageSwitcher';

const Header = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="bratz-nav">
            <span 
                className="mobile-center"
                onClick={() => navigate('/analysis')}
                style={{
                    fontFamily: "'Playfair Display', serif",
                    fontSize: 'clamp(1.2rem, 4vw, 1.8rem)',
                    background: 'linear-gradient(135deg,#ff007f,#b026ff)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                    backgroundClip: 'text',
                    fontWeight: 900,
                    cursor: 'pointer'
                }}>
                StyleMatch ✨
            </span>

            <div className="mobile-center" style={{ 
                display: 'flex', 
                alignItems: 'center', 
                gap: '8px', 
                flexWrap: 'wrap',
                padding: '4px 0'
            }}>
                <LanguageSwitcher />
                
                <button
                    className="mobile-text-sm"
                    onClick={() => navigate('/analysis')}
                    style={{
                        background: 'rgba(255,255,255,0.08)',
                        border: '1.5px solid rgba(255,255,255,0.15)',
                        borderRadius: '12px',
                        padding: '8px 16px',
                        color: '#fff',
                        cursor: 'pointer',
                        fontFamily: 'Poppins, sans-serif',
                        fontSize: '0.85rem',
                        fontWeight: 600,
                        transition: 'all 0.3s',
                        whiteSpace: 'nowrap'
                    }}
                    onMouseEnter={e => e.currentTarget.style.background = 'rgba(255,255,255,0.15)'}
                    onMouseLeave={e => e.currentTarget.style.background = 'rgba(255,255,255,0.08)'}
                >
                    {t('nav.styleTest')}
                </button>

                <button
                    onClick={() => navigate('/profile')}
                    style={{
                        background: 'rgba(255,255,255,0.08)',
                        border: '1.5px solid rgba(255,255,255,0.15)',
                        borderRadius: '12px',
                        padding: '8px 16px',
                        color: '#fff',
                        cursor: 'pointer',
                        fontFamily: 'Poppins, sans-serif',
                        fontSize: '0.85rem',
                        fontWeight: 600,
                        transition: 'all 0.3s'
                    }}
                    onMouseEnter={e => e.currentTarget.style.background = 'rgba(255,255,255,0.15)'}
                    onMouseLeave={e => e.currentTarget.style.background = 'rgba(255,255,255,0.08)'}
                >
                    👤 {t('nav.profile')}
                </button>

                <button
                    onClick={handleLogout}
                    style={{
                        background: 'transparent',
                        border: '1.5px solid rgba(255,0,127,0.35)',
                        borderRadius: '12px',
                        padding: '8px 16px',
                        color: '#ff007f',
                        cursor: 'pointer',
                        fontFamily: 'Poppins, sans-serif',
                        fontSize: '0.85rem',
                        fontWeight: 600,
                        transition: 'all 0.3s'
                    }}
                    onMouseEnter={e => e.currentTarget.style.background = 'rgba(255,0,127,0.1)'}
                    onMouseLeave={e => e.currentTarget.style.background = 'transparent'}
                >
                    {t('nav.logout')}
                </button>
            </div>
        </div>
    );
};

export default Header;
