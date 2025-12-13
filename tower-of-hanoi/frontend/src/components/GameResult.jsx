import { Trophy, XCircle, Home, RotateCcw } from 'lucide-react';
import './GameResult.css';

function GameResult({ resultData, onPlayAgain, onBackToMenu }) {
  const isWin = resultData?.result === 'WIN';
  const isDraw = resultData?.result === 'DRAW';

  return (
    <div className="result-screen">
      <div className="result-card">
        <div className="result-icon">
          {isWin ? '🏆' : isDraw ? '🤔' : '❌'}
        </div>

        <h2 className="result-title">
          {isWin ? '🎉 Congratulations!' : isDraw ? '💪 Try Again!' : '💪 Try Again!'}
        </h2>

        <p className="result-message">
          {resultData?.message || 'Game completed!'}
        </p>

        <div className="result-sections">
          {/* Your Answer */}
          <div className="result-section">
            <h3>Your Answer</h3>
            <div className="result-detail">
              <span className="result-label">Player:</span>
              <span className="result-value neutral">{resultData?.playerName || 'Unknown'}</span>
            </div>
            <div className="result-detail">
              <span className="result-label">Your Moves:</span>
              <span className={`result-value ${isWin ? 'correct' : 'incorrect'}`}>
                {resultData?.playerMinimumMoves || 0}
              </span>
            </div>
          </div>

          {/* Correct Answer */}
          <div className="result-section">
            <h3>Correct Answer</h3>
            <div className="result-detail">
              <span className="result-label">Minimum Moves:</span>
              <span className="result-value correct">{resultData?.correctMinimumMoves || 0}</span>
            </div>
            {resultData?.correctMoveSequence && (
              <div className="move-sequence-display">
                <strong>Correct Sequence:</strong>
                <p>{resultData.correctMoveSequence}</p>
              </div>
            )}
          </div>
        </div>

        {/* Action Buttons */}
        <div className="result-actions">
          <button className="result-button main-menu" onClick={onBackToMenu}>
            <Home size={20} />
            Main Menu
          </button>
          <button className="result-button play-again" onClick={onPlayAgain}>
            <RotateCcw size={20} />
            Play Again
          </button>
        </div>
      </div>
    </div>
  );
}

export default GameResult;