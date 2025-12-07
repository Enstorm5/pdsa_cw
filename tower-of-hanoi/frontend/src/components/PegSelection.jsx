import { useState } from 'react';
import { ArrowLeft } from 'lucide-react';
import './PegSelection.css';

function PegSelection({ onSelect, onBack }) {
  const [selectedPegs, setSelectedPegs] = useState(null);

  const handleConfirm = () => {
    if (selectedPegs) {
      onSelect(selectedPegs);
    }
  };

  return (
    <div className="peg-selection-screen">
      <div className="pegselect-card">
        <h2 className="pegselect-title">Choose Number of Pegs</h2>
        <p className="pegselect-subtitle">
          Select how many pegs you want to use in this game
        </p>

        <div className="peg-options">
          <div
            className={`peg-option ${selectedPegs === 3 ? 'selected' : ''}`}
            onClick={() => setSelectedPegs(3)}
          >
            <div className="peg-visual">
              {[0, 1, 2].map((i) => (
                <div key={i} className="visual-peg">
                  <div className="visual-rod"></div>
                  <div className="visual-base"></div>
                </div>
              ))}
            </div>
            <div className="peg-info">
              <span className="peg-number">3</span>
              <span className="peg-label">Pegs</span>
              <span className="peg-difficulty">Classic</span>
            </div>
          </div>

          <div
            className={`peg-option ${selectedPegs === 4 ? 'selected' : ''}`}
            onClick={() => setSelectedPegs(4)}
          >
            <div className="peg-visual">
              {[0, 1, 2, 3].map((i) => (
                <div key={i} className="visual-peg">
                  <div className="visual-rod"></div>
                  <div className="visual-base"></div>
                </div>
              ))}
            </div>
            <div className="peg-info">
              <span className="peg-number">4</span>
              <span className="peg-label">Pegs</span>
              <span className="peg-difficulty">Advanced</span>
            </div>
          </div>
        </div>

        <div className="pegselect-buttons">
          <button className="back-button" onClick={onBack}>
            <ArrowLeft size={20} />
            Back
          </button>
          <button
            className="confirm-button"
            onClick={handleConfirm}
            disabled={!selectedPegs}
          >
            Confirm Selection
          </button>
        </div>
      </div>
    </div>
  );
}

export default PegSelection;