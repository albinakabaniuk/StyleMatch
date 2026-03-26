import React from 'react';

const ImageOptionCard = ({ imageUrl, label, badge, selected, onClick }) => (
    <div
        onClick={onClick}
        style={{
            cursor: 'pointer',
            borderRadius: '20px',
            overflow: 'hidden',
            border: selected ? '3px solid #ff007f' : '3px solid rgba(255,255,255,0.07)',
            boxShadow: selected
                ? '0 0 0 3px rgba(255,0,127,0.3), 0 12px 40px rgba(255,0,127,0.25)'
                : '0 8px 30px rgba(0,0,0,0.3)',
            transform: selected ? 'scale(1.04)' : 'scale(1)',
            transition: 'all 0.25s cubic-bezier(0.34,1.56,0.64,1)',
            background: 'rgba(30,10,50,0.8)',
            position: 'relative',
        }}
        onMouseEnter={e => { if (!selected) e.currentTarget.style.transform = 'scale(1.02)'; }}
        onMouseLeave={e => { if (!selected) e.currentTarget.style.transform = 'scale(1)'; }}
    >
        {imageUrl && (
            <div style={{ width: '100%', paddingTop: '75%', position: 'relative', overflow: 'hidden' }}>
                <img
                    src={imageUrl}
                    alt={label}
                    style={{
                        position: 'absolute',
                        inset: 0,
                        width: '100%',
                        height: '100%',
                        objectFit: 'cover',
                        transition: 'transform 0.4s ease',
                    }}
                />
                {/* Answer letter badge — top-left */}
                {badge && (
                    <div style={{
                        position: 'absolute',
                        top: '8px',
                        left: '8px',
                        background: selected
                            ? 'linear-gradient(135deg, #ff007f, #b026ff)'
                            : 'rgba(0,0,0,0.6)',
                        backdropFilter: 'blur(6px)',
                        borderRadius: '8px',
                        width: '26px',
                        height: '26px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '11px',
                        fontWeight: 800,
                        fontFamily: 'Poppins, sans-serif',
                        color: '#fff',
                        boxShadow: selected ? '0 4px 12px rgba(255,0,127,0.4)' : 'none',
                        transition: 'all 0.25s ease',
                    }}>{badge}</div>
                )}
                {/* Selected checkmark — top-right */}
                {selected && (
                    <div style={{
                        position: 'absolute',
                        top: '8px',
                        right: '8px',
                        background: '#ff007f',
                        borderRadius: '50%',
                        width: '28px',
                        height: '28px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '14px',
                        boxShadow: '0 4px 15px rgba(255,0,127,0.5)',
                    }}>✓</div>
                )}
                {/* Glow overlay when selected */}
                {selected && (
                    <div style={{
                        position: 'absolute',
                        inset: 0,
                        background: 'linear-gradient(135deg, rgba(255,0,127,0.08), rgba(176,38,255,0.08))',
                        pointerEvents: 'none',
                    }} />
                )}
            </div>
        )}
        <div style={{
            padding: '12px 14px',
            background: selected
                ? 'linear-gradient(135deg, rgba(255,0,127,0.2), rgba(176,38,255,0.2))'
                : 'rgba(255,255,255,0.03)',
            borderTop: '1px solid rgba(255,255,255,0.08)',
        }}>
            <p style={{
                margin: 0,
                fontSize: '0.82rem',
                color: selected ? '#ff9ed2' : '#cbb0e0',
                fontFamily: 'Poppins, sans-serif',
                fontWeight: selected ? 600 : 400,
                lineHeight: 1.4,
                textAlign: 'center',
            }}>{label}</p>
        </div>
    </div>
);

export default ImageOptionCard;
