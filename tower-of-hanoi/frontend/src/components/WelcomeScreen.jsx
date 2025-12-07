import { useState } from 'react';
import { Play, ArrowLeft } from 'lucide-react';
import './WelcomeScreen.css';

function WelcomeScreen({ onStart }) {
  const [playerName, setPlayerName] = useState('');
  const [error, setError] = useState('');

  const handleStart = () => {
    if (playerName.trim().length < 2) {
      setError('Please enter a valid name (at least 2 characters)');
      return;
    }
    onStart(playerName.trim());
  };

  const handleBackToMenu = () => {
    // This will be connected to your main menu router
    alert('This will navigate back to the main game menu');
  };

  return (
    <div className="welcome-screen">
      <div className="welcome-card">
        <div className="welcome-icon">🗼</div>
        
        <h2 className="welcome-title">Welcome to Tower of Hanoi!</h2>
        
        <div className="welcome-intro">
          <div className="intro-section">
            <h3>📖 About the Game</h3>
            <p>
              Tower of Hanoi is a classic mathematical puzzle invented by French 
              mathematician Édouard Lucas in 1883. Your mission is to move a tower 
              of disks from one peg to another following specific rules.
            </p>
          </div>

          <div className="intro-section">
            <h3>🎯 Quick Rules Summary</h3>
            <ul>
              <li>Move one disk at a time</li>
              <li>A larger disk cannot be placed on a smaller disk</li>
              <li>You can use auxiliary pegs to help move disks</li>
              <li>Goal: Move all disks from Peg A to Peg D</li>
            </ul>
          </div>
        </div>

        <div className="name-input-section">
          <label htmlFor="playerName" className="name-label">
            👤 Enter Your Name
          </label>
          <input
            type="text"
            id="playerName"
            value={playerName}
            onChange={(e) => {
              setPlayerName(e.target.value);
              setError('');
            }}
            placeholder="Your name here..."
            maxLength={50}
            className="name-input"
            onKeyPress={(e) => e.key === 'Enter' && handleStart()}
          />
          {error && <p className="error-text">{error}</p>}
        </div>

        <div className="welcome-buttons">
          <button className="back-button" onClick={handleBackToMenu}>
            <ArrowLeft size={20} />
            Back to Main Menu
          </button>
          <button className="start-button" onClick={handleStart}>
            <Play size={20} />
            Start Game
          </button>
        </div>
      </div>
    </div>
  );
}

export default WelcomeScreen;