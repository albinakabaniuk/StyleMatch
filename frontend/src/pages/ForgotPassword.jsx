import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import api from '../api';
import LanguageSwitcher from '../components/LanguageSwitcher';

const ForgotPassword = () => {
    const { t } = useTranslation();
    const [email, setEmail] = useState('');
    const [token, setToken] = useState('');
    const [success, setSuccess] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');
    const [loading, setLoading] = useState(false);
    const [registeredEmails, setRegisteredEmails] = useState([]);

    // Fetch registered emails for dev testing
    React.useEffect(() => {
        const fetchEmails = async () => {
            try {
                const res = await api.get('auth/debug/emails');
                setRegisteredEmails(res.data);
            } catch (err) {
                console.error("Failed to fetch debug emails:", err);
            }
        };
        fetchEmails();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setSuccess(false);
        setErrorMsg('');
        setToken(''); // Reset token on new attempt
        const normalizedEmail = email.toLowerCase().trim();
        try {
            const res = await api.post('auth/forgot-password', { email: normalizedEmail });
            setSuccess(true);
            if (res.data && res.data.token) {
                setToken(res.data.token);
            }
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
                        padding: '20px',
                        marginBottom: '20px',
                        color: '#86efac',
                        fontSize: '0.9rem',
                        lineHeight: 1.6,
                    }}>
                        <div style={{ marginBottom: '12px', fontWeight: 'bold' }}>
                            ✅ {t('auth.forgotSuccess', 'If this email is registered, a reset token has been generated.')}
                        </div>
                        
                        {token ? (
                            <div style={{ 
                                background: 'rgba(255,255,255,0.05)', 
                                padding: '20px', 
                                borderRadius: '12px',
                                border: '2px solid var(--bratz-pink)',
                                marginTop: '15px',
                                boxShadow: '0 0 20px rgba(192,132,252,0.3)'
                            }}>
                                <p style={{ margin: '0 0 12px 0', fontSize: '1rem', color: '#ff007f', fontWeight: 'bold' }}>
                                    🛑 STOP! ACTION REQUIRED:
                                </p>
                                <p style={{ margin: '0 0 12px 0', fontSize: '0.85rem', color: '#fff' }}>
                                    Since email delivery is currently disabled for this test, use this <b>Instant Token</b> instead:
                                </p>
                                <div style={{ position: 'relative', marginBottom: '15px' }}>
                                    <code style={{ 
                                        display: 'block', 
                                        background: '#000', 
                                        padding: '12px', 
                                        borderRadius: '6px',
                                        fontSize: '0.9rem',
                                        wordBreak: 'break-all',
                                        color: '#00ff00',
                                        border: '1px solid #444',
                                        textAlign: 'center'
                                    }}>{token}</code>
                                    <button 
                                        onClick={() => {
                                            navigator.clipboard.writeText(token);
                                            alert("Token copied to clipboard! ✨");
                                        }}
                                        style={{
                                            position: 'absolute',
                                            right: '8px',
                                            top: '50%',
                                            transform: 'translateY(-50%)',
                                            background: 'rgba(255,255,255,0.1)',
                                            border: 'none',
                                            borderRadius: '4px',
                                            color: '#fff',
                                            padding: '4px 8px',
                                            fontSize: '0.65rem',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        Copy
                                    </button>
                                </div>
                                
                                <Link 
                                    to={`/reset-password?token=${token}`}
                                    className="bratz-btn"
                                    style={{ 
                                        width: '100%', 
                                        textAlign: 'center', 
                                        display: 'block', 
                                        textDecoration: 'none',
                                        background: 'linear-gradient(45deg, #c084fc, #ff007f)',
                                        color: '#fff',
                                        fontWeight: 'bold',
                                        padding: '12px'
                                    }}
                                >
                                    🚀 CLICK HERE TO RESET NOW
                                </Link>
                            </div>
                        ) : (
                            <div style={{ marginTop: '12px', borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '12px' }}>
                                <div style={{ fontSize: '0.8rem', color: '#ffcc00', marginBottom: '8px', fontWeight: 'bold' }}>
                                    ⚠️ EMAIL NOT FOUND IN SYSTEM
                                </div>
                                <p style={{ fontSize: '0.75rem', color: 'rgba(255,255,255,0.6)', margin: '0 0 10px 0' }}>
                                    We couldn't generate a token because this email isn't registered. Try one of these test accounts:
                                </p>
                                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                                    {registeredEmails.map(emailAddr => (
                                        <button
                                            key={emailAddr}
                                            onClick={() => setEmail(emailAddr)}
                                            style={{
                                                background: 'rgba(192,132,252,0.1)',
                                                border: '1px solid rgba(192,132,252,0.3)',
                                                borderRadius: '6px',
                                                padding: '4px 8px',
                                                color: '#c084fc',
                                                fontSize: '0.7rem',
                                                cursor: 'pointer'
                                            }}
                                        >
                                            {emailAddr}
                                        </button>
                                    ))}
                                    {registeredEmails.length === 0 && (
                                        <span style={{ fontSize: '0.75rem', opacity: 0.5 }}>No users registered yet.</span>
                                    )}
                                </div>
                            </div>
                        )}
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
