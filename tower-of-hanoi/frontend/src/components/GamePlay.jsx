import { useState, useEffect } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../config';
import { Send, Loader, ArrowLeft, RotateCcw, Clock } from 'lucide-react';
import './GamePlay.css';

function GamePlay({ playerName, numberOfDisks, numberOfPegs, onGameDataReady, onSubmit, onBack }) {
  const [mode, setMode] = useState('drag'); // 'drag' or 'manual'
  const [pegs, setPegs] = useState([]);
  const [moveCount, setMoveCount] = useState(0);
  const [moveHistory, setMoveHistory] = useState([]);
  const [draggedDisk, setDraggedDisk] = useState(null);
  const [error, setError] = useState('');
  const [gameData, setGameData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitLoading, setSubmitLoading] = useState(false);
  const [timer, setTimer] = useState(0);
  const [timerInterval, setTimerInterval] = useState(null);
  
  // Manual entry state
  const [manualMoves, setManualMoves] = useState('');
  const [manualSequence, setManualSequence] = useState('');

  // Initialize game
  useEffect(() => {
    initializeGame();
  }, []);

  // Timer
  useEffect(() => {
    const interval = setInterval(() => {
      setTimer(t => t + 1);
    }, 1000);
    setTimerInterval(interval);
    return () => clearInterval(interval);
  }, []);

  const initializeGame = async () => {
    try {
      const response = await axios.post(`${API_BASE_URL}/start`, {
        numberOfPegs: numberOfPegs
      });
      setGameData(response.data);
      onGameDataReady(response.data);
      
      // Initialize pegs with disks on first peg
      const initialPegs = Array(numberOfPegs).fill(null).map(() => []);
      for (let i = numberOfDisks; i >= 1; i--) {
        initialPegs[0].push(i);
      }
      setPegs(initialPegs);
      setLoading(false);
    } catch (err) {
      setError('Failed to start game. Please try again.');
      setLoading(false);
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const handleDragStart = (pegIndex, diskIndex) => {
    const disk = pegs[pegIndex][diskIndex];
    // Only allow dragging the top disk
    if (diskIndex === pegs[pegIndex].length - 1) {
      setDraggedDisk({ pegIndex, disk });
      setError('');
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleDrop = (targetPegIndex) => {
    if (!draggedDisk) return;

    const { pegIndex: sourcePegIndex, disk } = draggedDisk;
    
    // Can't drop on the same peg
    if (sourcePegIndex === targetPegIndex) {
      setDraggedDisk(null);
      return;
    }

    // Check if move is valid
    const targetPeg = pegs[targetPegIndex];
    const topDisk = targetPeg[targetPeg.length - 1];
    
    if (topDisk && disk > topDisk) {
      setError('❌ Invalid move! Cannot place a larger disk on a smaller disk.');
      setDraggedDisk(null);
      return;
    }

    // Valid move - update pegs
    const newPegs = pegs.map(peg => [...peg]);
    newPegs[sourcePegIndex].pop();
    newPegs[targetPegIndex].push(disk);
    
    setPegs(newPegs);
    setMoveCount(moveCount + 1);
    
    // Record move
    const pegLabels = ['A', 'B', 'C', 'D'];
    const move = `${pegLabels[sourcePegIndex]}->${pegLabels[targetPegIndex]}`;
    setMoveHistory([...moveHistory, move]);
    
    setDraggedDisk(null);
    setError('');

    // Check if game is won
    const lastPegIndex = numberOfPegs - 1;
    if (newPegs[lastPegIndex].length === numberOfDisks) {
      setTimeout(() => {
        handleAutoSubmit(moveCount + 1, [...moveHistory, move]);
      }, 500);
    }
  };

  const handleAutoSubmit = async (moves, history) => {
    clearInterval(timerInterval);
    setSubmitLoading(true);
    
    try {
      const response = await axios.post(`${API_BASE_URL}/submit-answer`, {
        gameRoundId: gameData.gameRoundId,
        playerName: playerName,
        playerMinimumMoves: moves,
        playerMoveSequence: history.join(', ')
      });
      onSubmit(response.data);
    } catch (err) {
      setError('Failed to submit answer. Please try again.');
      setSubmitLoading(false);
    }
  };

  const handleManualSubmit = async (e) => {
    e.preventDefault();
    clearInterval(timerInterval);
    setSubmitLoading(true);
    setError('');

    try {
      const response = await axios.post(`${API_BASE_URL}/submit-answer`, {
        gameRoundId: gameData.gameRoundId,
        playerName: playerName,
        playerMinimumMoves: parseInt(manualMoves),
        playerMoveSequence: manualSequence.trim()
      });
      onSubmit(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit answer.');
      setSubmitLoading(false);
    }
  };

  const handleReset = () => {
    const initialPegs = Array(numberOfPegs).fill(null).map(() => []);
    for (let i = numberOfDisks; i >= 1; i--) {
      initialPegs[0].push(i);
    }
    setPegs(initialPegs);
    setMoveCount(0);
    setMoveHistory([]);
    setError('');
    setTimer(0);
  };

  const getDiskColor = (diskNumber) => {
    const colors = [
      '#3B82F6', // Blue
      '#EAB308', // Yellow
      '#22C55E', // Green
      '#F97316', // Orange
      '#06B6D4', // Cyan
      '#84CC16', // Lime
      '#A855F7', // Purple
      '#EF4444', // Red
      '#C026D3', // Violet
      '#8B5CF6', // Indigo
    ];
    return colors[(diskNumber - 1) % colors.length];
  };

  const getPegLabel = (index) => {
    return ['A', 'B', 'C', 'D'][index];
  };

  if (loading) {
    return (
      <div className="game-loading">
        <Loader className="spinner" size={48} />
        <p>Preparing your game...</p>
      </div>
    );
  }

  return (
    <div className="gameplay-container">
      {/* Header with stats */}
      <div className="gameplay-header">
        <button className="icon-button" onClick={onBack}>
          <ArrowLeft size={20} />
        </button>
        
        <div className="game-stats-header">
          <div className="stat-item">
            <Clock size={20} />
            <span>{formatTime(timer)}</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">Moves:</span>
            <span className="stat-value">{moveCount}</span>
          </div>
        </div>

        <button className="icon-button" onClick={handleReset}>
          <RotateCcw size={20} />
        </button>
      </div>

      {/* Mode Toggle */}
      <div className="mode-toggle">
        <button
          className={`mode-btn ${mode === 'drag' ? 'active' : ''}`}
          onClick={() => setMode('drag')}
        >
          🖱️ Drag & Drop
        </button>
        <button
          className={`mode-btn ${mode === 'manual' ? 'active' : ''}`}
          onClick={() => setMode('manual')}
        >
          ⌨️ Manual Entry
        </button>
      </div>

      {error && (
        <div className="error-banner">
          {error}
        </div>
      )}

      {mode === 'drag' ? (
        <div className="drag-mode">
          <div className="pegs-container">
            {pegs.map((peg, pegIndex) => (
              <div
                key={pegIndex}
                className="peg-column"
                onDragOver={handleDragOver}
                onDrop={() => handleDrop(pegIndex)}
              >
                <div className="peg-structure">
                  <div className="peg-rod"></div>
                  <div className="peg-base">{getPegLabel(pegIndex)}</div>
                </div>
                <div className="disk-stack">
                  {peg.map((disk, diskIndex) => (
                    <div
                      key={`${pegIndex}-${disk}-${diskIndex}`}
                      className="disk-item"
                      draggable={diskIndex === peg.length - 1}
                      onDragStart={() => handleDragStart(pegIndex, diskIndex)}
                      style={{
                        width: `${50 + disk * 20}px`,
                        backgroundColor: getDiskColor(disk),
                        cursor: diskIndex === peg.length - 1 ? 'grab' : 'not-allowed',
                        opacity: diskIndex === peg.length - 1 ? 1 : 0.7
                      }}
                    >
                      {disk}
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>

          {moveHistory.length > 0 && (
            <div className="move-history">
              <h3>Move History:</h3>
              <p>{moveHistory.join(', ')}</p>
            </div>
          )}
        </div>
      ) : (
        <div className="manual-mode">
          <form onSubmit={handleManualSubmit} className="manual-form">
            <div className="form-group">
              <label>Number of Moves</label>
              <input
                type="number"
                value={manualMoves}
                onChange={(e) => setManualMoves(e.target.value)}
                placeholder="e.g., 127"
                required
                min="1"
              />
            </div>

            <div className="form-group">
              <label>Move Sequence</label>
              <textarea
                value={manualSequence}
                onChange={(e) => setManualSequence(e.target.value)}
                placeholder="e.g., A->D, A->B, D->B, ..."
                required
                rows="4"
              />
              <small>Format: A→D, A→B, D→B (comma-separated)</small>
            </div>

            <button type="submit" className="submit-button" disabled={submitLoading}>
              {submitLoading ? (
                <>
                  <Loader className="spinner" />
                  Submitting...
                </>
              ) : (
                <>
                  <Send size={20} />
                  Submit Answer
                </>
              )}
            </button>
          </form>
        </div>
      )}
    </div>
  );
}

export default GamePlay;