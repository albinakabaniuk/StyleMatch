import React from 'react';

const AvatarCard = ({ src, name, palette, active, onClick, size = 'md' }) => {
    const sizes = {
        sm: { card: 120, img: 90, font: '0.7rem' },
        md: { card: 160, img: 120, font: '0.82rem' },
        lg: { card: 220, img: 170, font: '0.9rem' },
    };
    const s = sizes[size] || sizes.md;

    return (
        <div
            onClick={onClick}
            style={{
                width: s.card,
                cursor: onClick ? 'pointer' : 'default',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                gap: '10px',
                padding: '16px 12px',
                borderRadius: '24px',
                background: active
                    ? 'linear-gradient(135deg, rgba(255,0,127,0.18), rgba(176,38,255,0.18))'
                    : 'rgba(255,255,255,0.04)',
                border: active
                    ? '2px solid rgba(255,0,127,0.5)'
                    : '2px solid rgba(255,255,255,0.08)',
                boxShadow: active
                    ? '0 0 30px rgba(255,0,127,0.3), 0 12px 40px rgba(0,0,0,0.3)'
                    : '0 8px 24px rgba(0,0,0,0.2)',
                transform: active ? 'scale(1.04)' : 'scale(1)',
                transition: 'all 0.3s cubic-bezier(0.34,1.56,0.64,1)',
                backdropFilter: 'blur(12px)',
            }}
        >
            {/* Glow ring */}
            <div style={{
                width: s.img + 16,
                height: s.img + 16,
                borderRadius: '50%',
                background: active
                    ? 'linear-gradient(135deg, #ff007f, #b026ff)'
                    : 'linear-gradient(135deg, rgba(255,0,127,0.3), rgba(176,38,255,0.3))',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: active ? '0 0 40px rgba(255,0,127,0.4)' : 'none',
                transition: 'all 0.3s ease',
            }}>
                <img
                    src={src}
                    alt={name}
                    style={{
                        width: s.img,
                        height: s.img,
                        borderRadius: '50%',
                        objectFit: 'cover',
                        objectPosition: 'top',
                        border: '3px solid rgba(0,0,0,0.3)',
                    }}
                />
            </div>

            {name && (
                <div style={{ textAlign: 'center' }}>
                    <p style={{
                        margin: 0,
                        fontFamily: 'Poppins, sans-serif',
                        fontWeight: 700,
                        fontSize: s.font,
                        color: active ? '#ff9ed2' : '#e0c4ff',
                    }}>{name}</p>
                    {palette && (
                        <p style={{
                            margin: '4px 0 0',
                            fontSize: '0.72rem',
                            color: '#9d7ab8',
                            fontFamily: 'Poppins, sans-serif',
                        }}>{palette}</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default AvatarCard;
