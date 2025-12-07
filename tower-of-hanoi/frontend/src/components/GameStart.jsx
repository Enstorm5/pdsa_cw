import { useState } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../config';
import { Play, Loader } from 'lucide-react';
import './GameStart.css';

function GameStart({ onStart }) {
  const [numberOfPegs, setNumberOfPegs] = useState(3);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleStart = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await axios.post(`${API_BASE_URL}/start`, {
        numberOfPegs: numberOfPegs
      });
      onStart(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to start game. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="game-start">
      <div className="start-card">
        <div className="start-icon">🗼</div>
        <h2 className="start-title">Ready to Play?</h2>
        <p className="start-description">
          Select the number of pegs and let the challenge begin!
        </p>

        <div className="peg-selection">
          <label className="peg-label">Number of Pegs</label>
          <div className="peg-options">
            <button
              className={`peg-button ${numberOfPegs === 3 ? 'active' : ''}`}
              onClick={() => setNumberOfPegs(3)}
            >
              <span className="peg-number">3</span>
              <span className="peg-text">Pegs</span>
            </button>
            <button
              className={`peg-button ${numberOfPegs === 4 ? 'active' : ''}`}
              onClick={() => setNumberOfPegs(4)}
            >
              <span className="peg-number">4</span>
              <span className="peg-text">Pegs</span>
            </button>
          </div>
        </div>

        {error && (
          <div className="error-message">
            <span>⚠️</span>
            <p>{error}</p>
          </div>
        )}

        <button
          className="start-button"
          onClick={handleStart}
          disabled={loading}
        >
          {loading ? (
            <>
              <Loader className="spinner" />
              Starting Game...
            </>
          ) : (
            <>
              <Play size={20} />
              Start Game
            </>
          )}
        </button>
      </div>
    </div>
  );
}

export default GameStart;