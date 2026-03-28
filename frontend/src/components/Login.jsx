import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import api from '../api';
import LanguageSwitcher from './LanguageSwitcher';

const Login = () => {
    const { t } = useTranslation();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        const normalizedEmail = email.toLowerCase().trim();
        try {
            const response = await api.post('auth/login', { email: normalizedEmail, password });
            localStorage.setItem('token', response.data.token);
            navigate('/analysis');
        } catch (err) {
            const msg = err.response?.data?.message || err.message || t('errors.invalidCredentials');
            setError(msg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div style={{ position: 'fixed', top: '20px', right: '24px', zIndex: 100 }}>
                <LanguageSwitcher />
            </div>

            <div className="bratz-card" style={{ maxWidth: '440px' }}>
                <div style={{ textAlign: 'center', marginBottom: '32px' }}>
                    <div style={{ fontSize: '3rem', marginBottom: '8px' }}>💄</div>
                    <h2 className="brand-font" style={{ fontSize: '2rem', marginBottom: '6px' }}>
                        {t('auth.signInTitle', 'Sign In')}
                    </h2>
                    <p style={{ color: '#c084fc', margin: 0, fontSize: '0.9rem' }}>StyleMatch</p>
                </div>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>{t('auth.email')}</label>
                        <input type="email" className="form-input" value={email}
                            onChange={e => setEmail(e.target.value)} placeholder="chloe@bratz.com" required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.password')}</label>
                        <input type="password" className="form-input" value={password}
                            onChange={e => setPassword(e.target.value)} placeholder="••••••••" required />
                    </div>

                    <div style={{ textAlign: 'right', marginBottom: '20px' }}>
                        <Link to="/forgot-password" style={{ color: '#c084fc', fontSize: '0.85rem', textDecoration: 'none' }}>
                            {t('auth.forgotPassword')}
                        </Link>
                    </div>

                    <button type="submit" className="bratz-btn" disabled={loading} style={{ width: '100%' }}>
                        {loading ? '⏳...' : t('auth.signInBtn', 'Sign In ✨')}
                    </button>
                </form>

                <div className="toggle-auth" style={{ marginTop: '24px' }}>
                    {t('auth.noAccount')} <Link to="/register">{t('nav.signUpLink', 'Sign up')}</Link>
                </div>
            </div>
        </div>
    );
};

export default Login;
