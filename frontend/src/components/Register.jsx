import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import api from '../api';
import LanguageSwitcher from './LanguageSwitcher';

const Register = () => {
    const { t } = useTranslation();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const response = await api.post('/auth/register', { email, password });
            localStorage.setItem('token', response.data.token);
            navigate('/analysis');
        } catch {
            setError(t('errors.registrationFailed'));
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
                    <div style={{ fontSize: '3rem', marginBottom: '8px' }}>👠</div>
                    <h2 className="brand-font" style={{ fontSize: '2rem', marginBottom: '6px' }}>
                        {t('auth.createAccountTitle', 'Create Account')}
                    </h2>
                    <p style={{ color: '#c084fc', margin: 0, fontSize: '0.9rem' }}>StyleMatch</p>
                </div>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleRegister}>
                    <div className="form-group">
                        <label>{t('auth.email')}</label>
                        <input type="email" className="form-input" value={email}
                            onChange={e => setEmail(e.target.value)} placeholder="yasmin@bratz.com" required />
                    </div>
                    <div className="form-group">
                        <label>{t('auth.password')}</label>
                        <input type="password" className="form-input" value={password}
                            onChange={e => setPassword(e.target.value)} placeholder="••••••••" minLength={6} required />
                    </div>
                    <button type="submit" className="bratz-btn" disabled={loading} style={{ width: '100%', marginTop: '8px' }}>
                        {loading ? '⏳...' : t('auth.signUpBtn', 'Sign Up')}
                    </button>
                </form>

                <div className="toggle-auth" style={{ marginTop: '24px' }}>
                    {t('auth.hasAccount')} <Link to="/login">{t('nav.login')}</Link>
                </div>
            </div>
        </div>
    );
};

export default Register;
