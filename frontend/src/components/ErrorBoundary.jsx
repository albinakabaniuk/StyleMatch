import React from 'react';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("React ErrorBoundary caught an error:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div style={{
          padding: '2rem',
          textAlign: 'center',
          background: 'rgba(255,0,127,0.05)',
          borderRadius: '20px',
          border: '2px dashed #ff007f',
          margin: '2rem',
          color: '#fff'
        }}>
          <h2 style={{ fontFamily: "'Playfair Display', serif" }}>Oops! Something went wrong ✨</h2>
          <p style={{ color: 'rgba(255,255,255,0.6)' }}>
            The component crashed. This usually happens due to unexpected data format.
          </p>
          <pre style={{ 
            textAlign: 'left', 
            background: 'rgba(0,0,0,0.3)', 
            padding: '1rem', 
            borderRadius: '10px',
            fontSize: '0.8rem',
            overflow: 'auto',
            maxHeight: '200px'
          }}>
            {this.state.error?.toString()}
          </pre>
          <button 
            onClick={() => window.location.reload()} 
            style={{
              background: 'linear-gradient(135deg, #ff007f, #7000ff)',
              color: 'white',
              border: 'none',
              padding: '10px 20px',
              borderRadius: '10px',
              fontWeight: 'bold',
              cursor: 'pointer',
              marginTop: '1rem'
            }}
          >
            Reload Page
          </button>
        </div>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
