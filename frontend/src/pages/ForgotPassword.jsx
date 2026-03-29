import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import api from '../api';
import LanguageSwitcher from '../components/LanguageSwitcher';

const ForgotPassword = () => {
    const { t } = useTranslation();
    const [email, setEmail] = useState('');
    const [success, setSuccess] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setSuccess(false);
        setErrorMsg('');
        const normalizedEmail = email.toLowerCase().trim();
        try {
            await api.post('auth/forgot-password', { email: normalizedEmail });
            setSuccess(true);
        } catch (err) {
            setErrorMsg(err.response?.data?.message || err.message || 'GENERIC_ERROR');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            {/* Language switcher top right */}
            <div style={{ position: 'fixed', top: '20px', right: '24px', zIndex: 100 }}>
                <LanguageSwitcher />
            </div>

            <div className="bratz-card mobile-padding-sm" style={{ maxWidth: '440px' }}>
                <div style={{ textAlign: 'center', marginBottom: '28px' }}>
                    <div style={{ fontSize: '3rem', marginBottom: '8px' }}>🔐</div>
                    <h2 className="brand-font" style={{ fontSize: '1.8rem', marginBottom: '8px' }}>
                        {t('auth.forgotTitle', 'Forgot Password')}
                    </h2>
                    <p style={{ color: '#c084fc', fontSize: '0.9rem', margin: 0 }}>
                        {t('auth.forgotSubtitle', 'Enter your email to receive a reset link')}
                    </p>
                </div>

                {success && (
                    <div style={{
                        background: 'rgba(34,197,94,0.15)',
                        border: '1px solid rgba(34,197,94,0.3)',
                        borderRadius: '12px',
                        padding: '14px 16px',
                        marginBottom: '20px',
                        color: '#86efac',
                        fontSize: '0.88rem',
                        lineHeight: 1.5,
                    }}>
                        {t('auth.forgotSuccess', 'If this email is registered, a reset token has been generated.')}
                        <div style={{ marginTop: '8px', fontSize: '0.75rem', opacity: 0.8 }}>
                            Tip: For development, the token is also displayed in the backend console logs.
                        </div>
                    </div>
                )}
                {errorMsg && (
                    <div className="error-message">
                        {errorMsg === 'GENERIC_ERROR' ? t('errors.generic') : errorMsg}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>{t('auth.email')}</label>
                        <input
                            type="email"
                            className="form-input"
                            value={email}
                            onChange={e => setEmail(e.target.value)}
                            placeholder="you@example.com"
                            required
                        />
                    </div>
                    <button type="submit" className="bratz-btn" disabled={loading} style={{ width: '100%' }}>
                        {loading ? '⏳ Sending...' : t('auth.sendReset', 'Send reset link ✨')}
                    </button>
                </form>

                <div className="toggle-auth" style={{ marginTop: '20px' }}>
                    <Link to="/login" style={{ color: '#c084fc' }}>← {t('auth.backToLogin', 'Back to login')}</Link>
                </div>
            </div>
        </div>
    );
};

export default ForgotPassword;
