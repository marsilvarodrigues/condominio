import React from 'react';
import './Popup.css'; // Arquivo CSS para a estilização

const Popup = ({ title, message, onClose }) => {
    return (
        <div className="popup">
            <div className="popup-content">
                <h2>{title}</h2>
                <p>{message}</p>
                <button onClick={onClose}>Fechar</button>
            </div>
        </div>
    );
};

export default Popup;