import React, { useState } from 'react';
import { startGame } from '../api/gameApi';

export default function Interface({ onGameStart }) {
    const [playerName, setPlayerName] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!playerName.trim()) return;

        setLoading(true);
        setError(null);
        try {
            const data = await startGame(playerName);
            onGameStart(data);
        } catch (err) {
            setError('Failed to start game. Check backend connection.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="interface-container">
            <div className="glass-panel">
                <h1 className="title">Traveling Salesman</h1>
                <p className="subtitle">Galactic Edition</p>

                <form onSubmit={handleSubmit} className="start-form">
                    <input
                        type="text"
                        placeholder="Enter Player Name"
                        value={playerName}
                        onChange={(e) => setPlayerName(e.target.value)}
                        className="name-input"
                        disabled={loading}
                    />
                    <button type="submit" className="start-button" disabled={loading}>
                        {loading ? 'Initializing...' : 'Start Journey'}
                    </button>
                </form>
                {error && <div className="error-message">{error}</div>}
            </div>
        </div>
    );
}
