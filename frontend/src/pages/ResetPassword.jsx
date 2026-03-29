import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api';
import LanguageSwitcher from '../components/LanguageSwitcher';

const ResetPassword = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [token, setToken] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrorMsg('');
        setSuccess(false);
        try {
            await api.post('auth/reset-password', { token: token.trim(), newPassword });
            setSuccess(true);
            setTimeout(() => navigate('/login'), 2500);
        } catch (err) {
            setErrorMsg(err.response?.data?.message || err.message || 'GENERIC_ERROR');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div style={{ position: 'fixed', top: '20px', right: '24px', zIndex: 100 }}>
                <LanguageSwitcher />
            </div>

            <div className="bratz-card mobile-padding-sm" style={{ maxWidth: '440px' }}>
                <div style={{ textAlign: 'center', marginBottom: '28px' }}>
                    <div style={{ fontSize: '3rem', marginBottom: '8px' }}>🔑</div>
                    <h2 className="brand-font" style={{ fontSize: '1.8rem' }}>{t('auth.resetTitle', 'Reset Password')}</h2>
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
                    }}>{t('auth.resetSuccess')}</div>
                )}
                {errorMsg && (
                    <div className="error-message">
                        {errorMsg === 'GENERIC_ERROR' ? t('errors.generic') : errorMsg}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>{t('auth.resetToken', 'Reset Token')}</label>
                        <input
                            type="text"
                            className="form-input"
                            value={token}
                            onChange={e => setToken(e.target.value)}
                            placeholder="Paste token from server logs"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.newPassword', 'New Password')}</label>
                        <input
                            type="password"
                            className="form-input"
                            value={newPassword}
                            onChange={e => setNewPassword(e.target.value)}
                            placeholder="••••••••"
                            minLength={6}
                            required
                        />
                    </div>
                    <button type="submit" className="bratz-btn" disabled={loading} style={{ width: '100%' }}>
                        {loading ? '⏳ Resetting...' : t('auth.resetBtn', 'Update password ✨')}
                    </button>
                </form>

                <div className="toggle-auth" style={{ marginTop: '20px' }}>
                    <Link to="/login" style={{ color: '#c084fc' }}>← {t('auth.backToLogin', 'Back to login')}</Link>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
