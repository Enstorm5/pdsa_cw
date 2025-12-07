import { Trophy, X, TrendingUp, RefreshCw, Home } from 'lucide-react';
import './GameResult.css';

function GameResult({ resultData, onPlayAgain, onBackToMenu }) {
  const isWin = resultData.result === 'WIN';
  const isDraw = resultData.result === 'DRAW';

  return (
    <div className="game-result">
      <div className={`result-card ${resultData.result.toLowerCase()}`}>
        <div className="result-header">
          <div className="result-icon">
            {isWin && <Trophy size={80} />}
            {isDraw && <TrendingUp size={80} />}
            {!isWin && !isDraw && <X size={80} />}
          </div>

          <h2 className="result-title">
            {isWin && '🎉 Congratulations!'}
            {isDraw && '📊 Almost There!'}
            {!isWin && !isDraw && '💪 Try Again!'}
          </h2>

          <p className="result-message">{resultData.message}</p>
        </div>

        <div className="result-details">
          <div className="result-section">
            <h3>Your Answer</h3>
            <div className="result-grid">
              <div className="result-item">
                <span className="item-label">Player:</span>
                <span className="item-value">{resultData.playerName}</span>
              </div>
              <div className="result-item">
                <span className="item-label">Your Moves:</span>
                <span className={`item-value ${isWin ? 'correct' : 'incorrect'}`}>
                  {resultData.playerMinimumMoves}
                </span>
              </div>
            </div>
          </div>

          <div className="result-divider"></div>

          <div className="result-section">
            <h3>Correct Answer</h3>
            <div className="result-grid">
              <div className="result-item">
                <span className="item-label">Minimum Moves:</span>
                <span className="item-value correct">
                  {resultData.correctMinimumMoves}
                </span>
              </div>
            </div>
          </div>

          {!isWin && resultData.correctMoveSequence && (
            <div className="result-sequence">
              <h4>Correct Sequence:</h4>
              <div className="sequence-box">
                {resultData.correctMoveSequence}
              </div>
            </div>
          )}
        </div>

        <div className="result-buttons">
          <button className="menu-button" onClick={onBackToMenu}>
            <Home size={20} />
            Main Menu
          </button>
          <button className="play-again-button" onClick={onPlayAgain}>
            <RefreshCw size={20} />
            Play Again
          </button>
        </div>
      </div>
    </div>
  );
}

export default GameResult;