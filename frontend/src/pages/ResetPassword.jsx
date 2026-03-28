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
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        try {
            await api.post('/auth/reset-password', { token, newPassword });
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.response?.data || t('errors.generic'));
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

                {error && <div className="error-message">{error}</div>}

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
