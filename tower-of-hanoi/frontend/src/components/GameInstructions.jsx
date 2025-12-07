import { ArrowRight, Circle, XCircle, CheckCircle } from 'lucide-react';
import './GameInstructions.css';

function GameInstructions({ numberOfDisks, numberOfPegs, onContinue }) {
  return (
    <div className="instructions-screen">
      <div className="instructions-card">
        <div className="instructions-header">
          <div className="laurel-icon">🏆</div>
          <h2 className="instructions-title">Game Rules & Instructions</h2>
        </div>

        <div className="rules-grid">
          <div className="rule-item">
            <div className="rule-icon valid">
              <CheckCircle size={32} />
            </div>
            <div className="rule-content">
              <h3>Move One Disk at a Time</h3>
              <p>You can only move the top disk from any peg</p>
            </div>
          </div>

          <div className="rule-item">
            <div className="rule-icon invalid">
              <XCircle size={32} />
            </div>
            <div className="rule-content">
              <h3>No Larger on Smaller</h3>
              <p>A larger disk cannot be placed on top of a smaller disk</p>
            </div>
          </div>

          <div className="rule-item">
            <div className="rule-icon info">
              <Circle size={32} />
            </div>
            <div className="rule-content">
              <h3>Use Auxiliary Pegs</h3>
              <p>You may use the auxiliary peg(s) to help move disks</p>
            </div>
          </div>

          <div className="rule-item">
            <div className="rule-icon goal">
              <div className="goal-pegs">
                <span>A</span>
                <span>→</span>
                <span>D</span>
              </div>
            </div>
            <div className="rule-content">
              <h3>Your Goal</h3>
              <p>Move all {numberOfDisks} disks from Peg A to Peg D</p>
            </div>
          </div>
        </div>

        <div className="game-setup-info">
          <div className="setup-badge">
            <span className="badge-label">Disks:</span>
            <span className="badge-value">{numberOfDisks}</span>
          </div>
          <div className="setup-badge">
            <span className="badge-label">Pegs:</span>
            <span className="badge-value">{numberOfPegs}</span>
          </div>
        </div>

        <div className="play-modes">
          <h3>How to Play:</h3>
          <div className="modes-list">
            <div className="mode-option">
              <span className="mode-icon">🖱️</span>
              <div>
                <strong>Drag & Drop Mode:</strong>
                <p>Click and drag disks between pegs. Moves are counted automatically.</p>
              </div>
            </div>
            <div className="mode-option">
              <span className="mode-icon">⌨️</span>
              <div>
                <strong>Manual Entry Mode:</strong>
                <p>Type the number of moves and sequence manually.</p>
              </div>
            </div>
          </div>
        </div>

        <button className="continue-button-inst" onClick={onContinue}>
          Let's Play!
          <ArrowRight size={20} />
        </button>
      </div>
    </div>
  );
}

export default GameInstructions;