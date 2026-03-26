import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { analyzePhoto } from '../services/analysisService';
import ResultCard from './ResultCard';

const PhotoUpload = () => {
    const { i18n } = useTranslation();
    const [file, setFile] = useState(null);
    const [preview, setPreview] = useState(null);
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);
    const [error, setError] = useState('');

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            setFile(selectedFile);
            setPreview(URL.createObjectURL(selectedFile));
        }
    };

    const handleUpload = async () => {
        if (!file) {
            setError('Please select a photo first!');
            return;
        }
        setError('');
        setLoading(true);
        try {
            const data = await analyzePhoto(file, i18n.language);
            setResult(data);
        } catch (err) {
            console.error('Photo analysis failed:', err);
            setError('Failed to analyze photo. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form-container" style={{ textAlign: 'center' }}>
            <h2 style={{ color: '#ff007f', textShadow: '1px 1px 4px rgba(255,0,127,0.3)' }}>Upload Your Best Selfie! 📸</h2>
            <p style={{ color: '#b026ff' }}>Let our AI mock engine analyze your gorgeous features.</p>

            {!result ? (
                <>
                    <div style={{ margin: '20px 0' }}>
                        {preview ? (
                            <img src={preview} alt="Preview" style={{ width: '200px', height: '200px', objectFit: 'cover', borderRadius: '20px', border: '3px solid #ff007f', boxShadow: '0 0 15px rgba(255,0,127,0.5)' }} />
                        ) : (
                            <div style={{ width: '200px', height: '200px', margin: '0 auto', display: 'flex', alignItems: 'center', justifyContent: 'center', backgroundColor: '#fdf0fb', border: '2px dashed #ff007f', borderRadius: '20px', color: '#ff007f' }}>
                                No Image Selected
                            </div>
                        )}
                    </div>

                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleFileChange}
                        id="file-upload"
                        style={{ display: 'none' }}
                    />
                    <label htmlFor="file-upload" className="submit-button" style={{ display: 'inline-block', cursor: 'pointer', margin: '10px', backgroundColor: '#b026ff' }}>
                        Choose File 📂
                    </label>

                    <button onClick={handleUpload} className="submit-button" disabled={loading} style={{ margin: '10px' }}>
                        {loading ? 'Analyzing...' : 'Analyze Photo ✨'}
                    </button>

                    {error && <p className="error-message">{error}</p>}
                </>
            ) : (
                <ResultCard result={result} onReset={() => { setResult(null); setFile(null); setPreview(null); }} />
            )}
        </div>
    );
};

export default PhotoUpload;
