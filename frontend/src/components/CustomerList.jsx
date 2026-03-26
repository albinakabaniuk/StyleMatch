import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const CustomerList = () => {
    const [customers, setCustomers] = useState([]);
    const navigate = useNavigate();

    const handleLogout = React.useCallback(() => {
        localStorage.removeItem('token');
        navigate('/login');
    }, [navigate]);

    useEffect(() => {
        const fetchCustomers = async () => {
            try {
                const response = await api.get('/customers');
                setCustomers(response.data);
            } catch (err) {
                console.error('Failed to fetch customers', err);
                if (err.response && (err.response.status === 401 || err.response.status === 403)) {
                    handleLogout();
                }
            }
        };
        fetchCustomers();
    }, [navigate, handleLogout]);

    return (
        <div className="dashboard-container">
            <div className="dashboard-header">
                <h1>StyleMatch Customers</h1>
                <div>
                    <button
                        className="bratz-btn bratz-btn-small"
                        style={{ marginRight: '10px' }}
                        onClick={() => navigate('/customer-form')}
                    >
                        + Add New
                    </button>
                    <button className="bratz-btn bratz-btn-small" onClick={handleLogout}>
                        Logout
                    </button>
                </div>
            </div>

            <div className="customer-grid">
                {customers.length === 0 ? (
                    <p>No customers found! Time to style someone up.</p>
                ) : (
                    customers.map((c) => (
                        <div key={c.id} className="customer-card">
                            <h3>{c.name}</h3>
                            <p><span className="label">Email:</span> {c.email}</p>
                            <p><span className="label">Color Type:</span> {c.colorType}</p>
                            <p><span className="label">Body Shape:</span> {c.bodyShape}</p>
                            <p><span className="label">Contrast:</span> {c.contrastLevel}</p>
                            <p><span className="label">Undertone:</span> {c.undertone || 'N/A'}</p>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default CustomerList;
