import { useState } from 'react';
import { ArrowRight, Info } from 'lucide-react';
import './WelcomeScreen.css';

function WelcomeScreen({ onStart }) {
  const [name, setName] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (name.trim().length < 2) {
      setError('Please enter a valid name (at least 2 characters)');
      return;
    }
    onStart(name.trim());
  };

  return (
    <div className="welcome-screen">
      <div className="welcome-card">
        {/* Animated Tower of Hanoi */}
        <div className="welcome-tower-container">
          <div className="welcome-peg-structure">
            <div className="welcome-peg-rod"></div>
            <div className="welcome-disk-stack">
              {[5, 4, 3, 2, 1].map((disk) => (
                <div
                  key={disk}
                  className="welcome-disk"
                  style={{
                    width: `${40 + disk * 20}px`,
                    backgroundColor: `hsl(${(disk * 40) % 360}, 65%, 65%)`,
                    animationDelay: `${(5 - disk) * 0.15}s`
                  }}
                >
                  {disk}
                </div>
              ))}
            </div>
            <div className="welcome-peg-base">A</div>
          </div>
        </div>

        <h1 className="welcome-title">Tower of Hanoi</h1>
        <p className="welcome-subtitle">
          A classic mathematical puzzle game
        </p>

        <div className="quick-rules">
          <div className="rules-header">
            <Info size={20} />
            <span>Quick Rules Summary</span>
          </div>
          <ul className="rules-list">
            <li>Move all disks from source to destination peg</li>
            <li>Only one disk can be moved at a time</li>
            <li>A larger disk cannot be placed on a smaller disk</li>
            <li>You can use auxiliary pegs to help</li>
          </ul>
        </div>

        <form onSubmit={handleSubmit} className="welcome-form">
          <div className="form-group">
            <label htmlFor="playerName">Enter Your Name</label>
            <input
              id="playerName"
              type="text"
              value={name}
              onChange={(e) => {
                setName(e.target.value);
                setError('');
              }}
              placeholder="Your name..."
              className={error ? 'input-error' : ''}
            />
            {error && <span className="error-text">{error}</span>}
          </div>

          <button type="submit" className="start-button">
            Start Game
            <ArrowRight size={20} />
          </button>
        </form>
      </div>
    </div>
  );
}

export default WelcomeScreen;