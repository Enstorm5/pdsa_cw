import { Play, Info, Target, Hand, Trophy, X } from 'lucide-react';
import './GameInstructions.css';

function GameInstructions({ numberOfDisks, numberOfPegs, onContinue, onBack }) {
  return (
    <div className="instructions-container">
      <div className="instructions-card">
        <button className="quit-button-instructions" onClick={onBack}>
          <X size={20} />
          Quit Game
        </button>

        <h2 className="instructions-title">Game Rules & Instructions</h2>

        <div className="rules-grid">
          <div className="rule-card">
            <div className="rule-icon" style={{ backgroundColor: '#3B82F6' }}>
              <Target size={32} />
            </div>
            <h3>Objective</h3>
            <p>Move all {numberOfDisks} disks from peg A to peg {numberOfPegs === 3 ? 'C' : 'D'}</p>
          </div>

          <div className="rule-card">
            <div className="rule-icon" style={{ backgroundColor: '#EAB308' }}>
              <Hand size={32} />
            </div>
            <h3>One at a Time</h3>
            <p>Only move one disk at a time from the top of any stack</p>
          </div>

          <div className="rule-card">
            <div className="rule-icon" style={{ backgroundColor: '#22C55E' }}>
              <Info size={32} />
            </div>
            <h3>Size Rule</h3>
            <p>Never place a larger disk on top of a smaller disk</p>
          </div>

          <div className="rule-card">
            <div className="rule-icon" style={{ backgroundColor: '#F97316' }}>
              <Trophy size={32} />
            </div>
            <h3>Win Condition</h3>
            <p>Complete the puzzle in the minimum number of moves</p>
          </div>
        </div>

        <div className="game-setup-info">
          <h3>Your Game Setup</h3>
          <div className="setup-details">
            <div className="setup-item">
              <span className="setup-label">Number of Disks:</span>
              <span className="setup-value">{numberOfDisks}</span>
            </div>
            <div className="setup-item">
              <span className="setup-label">Number of Pegs:</span>
              <span className="setup-value">{numberOfPegs}</span>
            </div>
            <div className="setup-item">
              <span className="setup-label">Difficulty:</span>
              <span className="setup-value">
                {numberOfDisks <= 5 ? 'Easy' : numberOfDisks <= 7 ? 'Medium' : 'Hard'}
              </span>
            </div>
          </div>
        </div>

        <div className="how-to-play">
          <h3>How to Play</h3>
          <div className="play-modes">
            <div className="mode-option">
              <strong>🖱️ Drag & Drop Mode:</strong>
              <p>Click and drag disks from one peg to another. The game will validate your moves automatically.</p>
            </div>
            <div className="mode-option">
              <strong>⌨️ Manual Entry Mode:</strong>
              <p>Enter the total number of moves and the complete sequence (e.g., A→C, A→B, C→B). Submit when ready!</p>
            </div>
          </div>
        </div>

        <button className="lets-play-button" onClick={onContinue}>
          <Play size={24} />
          Let's Play!
        </button>
      </div>
    </div>
  );
}

export default GameInstructions;