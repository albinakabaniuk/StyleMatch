import React, { useState, useRef, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { ChevronDown } from 'lucide-react';
import userService from '../services/userService';

export const LANGUAGES = [
    { code: 'en', name: 'English', flag: '🇬🇧' },
    { code: 'uk', name: 'Українська', flag: '🇺🇦' },
    { code: 'de', name: 'Deutsch', flag: '🇩🇪' },
    { code: 'fr', name: 'Français', flag: '🇫🇷' },
    { code: 'es', name: 'Español', flag: '🇪🇸' },
    { code: 'ko', name: '한국어', flag: '🇰🇷' }
];

const LanguageSwitcher = ({ value, style, buttonStyle, onLanguageChange }) => {
    const { i18n } = useTranslation();
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef(null);

    const currentLang = LANGUAGES.find(l => l.code === (value || i18n.language)) || LANGUAGES[0];
    const displayLang = LANGUAGES.find(l => l.code === i18n.language) || LANGUAGES[0];

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleLanguageChange = async (code) => {
        if (onLanguageChange) {
            onLanguageChange(code);
            setIsOpen(false);
            return;
        }
        
        i18n.changeLanguage(code);
        localStorage.setItem('i18nextLng', code);
        setIsOpen(false);
        try {
            await userService.updateLanguage(code);
        } catch (err) {
            console.error('Failed to update language on backend:', err);
        }
    };

    return (
        <div ref={dropdownRef} style={{ position: 'relative', display: 'inline-block', zIndex: 1000, ...style }}>
            <button
                onClick={() => setIsOpen(!isOpen)}
                style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '10px',
                    padding: '8px 16px',
                    background: 'rgba(255, 255, 255, 0.05)',
                    backdropFilter: 'blur(10px)',
                    border: '1px solid rgba(255, 0, 127, 0.3)',
                    borderRadius: '50px',
                    color: '#fff',
                    cursor: 'pointer',
                    transition: 'all 0.3s ease',
                    boxShadow: '0 4px 15px rgba(255, 0, 127, 0.15)',
                    fontWeight: '600',
                    fontSize: '0.9rem',
                    ...buttonStyle
                }}
                onMouseEnter={e => {
                    e.currentTarget.style.background = 'rgba(255, 255, 255, 0.1)';
                    e.currentTarget.style.borderColor = 'rgba(255, 0, 127, 0.6)';
                    e.currentTarget.style.boxShadow = '0 6px 20px rgba(255, 0, 127, 0.3)';
                }}
                onMouseLeave={e => {
                    e.currentTarget.style.background = 'rgba(255, 255, 255, 0.05)';
                    e.currentTarget.style.borderColor = 'rgba(255, 0, 127, 0.3)';
                    e.currentTarget.style.boxShadow = '0 4px 15px rgba(255, 0, 127, 0.15)';
                }}
            >
                <span style={{ fontSize: '1.2rem' }}>{currentLang.flag}</span>
                <span style={{ minWidth: '80px', textAlign: 'left' }}>{currentLang.name}</span>
                <ChevronDown size={14} style={{ 
                    transition: 'transform 0.3s ease',
                    transform: isOpen ? 'rotate(180deg)' : 'rotate(0)'
                }} />
            </button>

            {/* Dropdown Menu */}
            <div style={{
                position: 'absolute',
                top: 'calc(100% + 10px)',
                right: 0,
                width: '180px',
                background: 'rgba(15, 5, 25, 0.9)',
                backdropFilter: 'blur(20px)',
                borderRadius: '16px',
                border: '1px solid rgba(255, 255, 255, 0.1)',
                overflow: 'hidden',
                transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                opacity: isOpen ? 1 : 0,
                visibility: isOpen ? 'visible' : 'hidden',
                transform: isOpen ? 'translateY(0) scale(1)' : 'translateY(-10px) scale(0.95)',
                boxShadow: '0 15px 40px rgba(0, 0, 0, 0.6), 0 0 20px rgba(176, 38, 255, 0.2)',
                padding: '8px'
            }}>
                {LANGUAGES.map((lang) => (
                    <div
                        key={lang.code}
                        onClick={() => handleLanguageChange(lang.code)}
                        style={{
                            display: 'flex',
                            alignItems: 'center',
                            gap: '12px',
                            padding: '10px 14px',
                            borderRadius: '10px',
                            cursor: 'pointer',
                            transition: 'all 0.2s ease',
                            color: i18n.language === lang.code ? '#ff007f' : '#fff',
                            background: i18n.language === lang.code ? 'rgba(255, 0, 127, 0.1)' : 'transparent',
                        }}
                        onMouseEnter={e => {
                            if (i18n.language !== lang.code) {
                                e.currentTarget.style.background = 'rgba(255, 255, 255, 0.05)';
                            }
                        }}
                        onMouseLeave={e => {
                            if (i18n.language !== lang.code) {
                                e.currentTarget.style.background = 'transparent';
                            }
                        }}
                    >
                        <span style={{ fontSize: '1.1rem' }}>{lang.flag}</span>
                        <span style={{ fontSize: '0.9rem', fontWeight: i18n.language === lang.code ? '700' : '400' }}>
                            {lang.name}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default LanguageSwitcher;
