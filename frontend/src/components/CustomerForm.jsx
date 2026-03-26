import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const CustomerForm = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        colorType: 'WINTER',
        bodyShape: 'A',
        contrastLevel: 'HIGH',
        undertone: 'COOL'
    });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await api.post('/customers', formData);
            navigate('/customers');
        } catch (err) {
            console.error('Failed to create customer:', err);
            setError('Failed to style this customer. Check the inputs!');
        }
    };

    return (
        <div className="auth-container">
            <div className="bratz-card" style={{ maxWidth: '600px' }}>
                <h2 className="brand-font">New Client Profile</h2>
                {error && <div className="error-message">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Name</label>
                        <input type="text" className="form-input" name="name" value={formData.name} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Email</label>
                        <input type="email" className="form-input" name="email" value={formData.email} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label>Color Type</label>
                        <select className="form-input" name="colorType" value={formData.colorType} onChange={handleChange}>
                            <option value="WINTER">Winter</option>
                            <option value="SPRING">Spring</option>
                            <option value="SUMMER">Summer</option>
                            <option value="AUTUMN">Autumn</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Body Shape</label>
                        <select className="form-input" name="bodyShape" value={formData.bodyShape} onChange={handleChange}>
                            <option value="A">A (Triangle)</option>
                            <option value="H">H (Rectangle)</option>
                            <option value="V">V (Inverted Triangle)</option>
                            <option value="O">O (Apple)</option>
                            <option value="X">X (Hourglass)</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Contrast Level</label>
                        <select className="form-input" name="contrastLevel" value={formData.contrastLevel} onChange={handleChange}>
                            <option value="LOW">Low</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Undertone</label>
                        <select className="form-input" name="undertone" value={formData.undertone} onChange={handleChange}>
                            <option value="COOL">Cool</option>
                            <option value="WARM">Warm</option>
                        </select>
                    </div>
                    <div style={{ display: 'flex', gap: '15px' }}>
                        <button type="submit" className="bratz-btn">Save Style</button>
                        <button type="button" className="bratz-btn" style={{ background: 'var(--bratz-black)' }} onClick={() => navigate('/customers')}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CustomerForm;
