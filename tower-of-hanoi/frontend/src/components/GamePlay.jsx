import { useState, useEffect } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../config';
import { Send, Loader, X, RotateCcw, Clock } from 'lucide-react';
import './GamePlay.css';

function GamePlay({ playerName, numberOfDisks, numberOfPegs, gameData, onSubmit, onBack }) {
  const [showReferenceSolutions, setShowReferenceSolutions] = useState(false);
  const [mode, setMode] = useState('drag'); // 'drag' or 'manual'
  const [pegs, setPegs] = useState([]);
  const [moveCount, setMoveCount] = useState(0);
  const [moveHistory, setMoveHistory] = useState([]);
  const [draggedDisk, setDraggedDisk] = useState(null);
  const [error, setError] = useState('');
  const [submitLoading, setSubmitLoading] = useState(false);
  const [timer, setTimer] = useState(0);
  const [timerInterval, setTimerInterval] = useState(null);
  
  // Manual entry state
  const [manualMoves, setManualMoves] = useState('');
  const [manualSequence, setManualSequence] = useState('');
  const [manualPegs, setManualPegs] = useState([]); // For manual mode visualization
  const [animatingManual, setAnimatingManual] = useState(false);

  // Initialize game
  useEffect(() => {
    initializeGame();
  }, [numberOfDisks, numberOfPegs]);

  // Timer
  useEffect(() => {
    const interval = setInterval(() => {
      setTimer(t => t + 1);
    }, 1000);
    setTimerInterval(interval);
    return () => clearInterval(interval);
  }, []);

  const initializeGame = () => {
    // Initialize pegs with disks on first peg
    const initialPegs = Array(numberOfPegs).fill(null).map(() => []);
    for (let i = numberOfDisks; i >= 1; i--) {
      initialPegs[0].push(i);
    }
    setPegs(initialPegs);
    setManualPegs(initialPegs.map(p => [...p])); // Copy for manual mode
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

    // Check if game is won (all disks on last peg)
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

  // Animate manual entry moves
  const animateManualMoves = async (movesArray) => {
    setAnimatingManual(true);
    const pegLabels = ['A', 'B', 'C', 'D'];
    let currentPegs = Array(numberOfPegs).fill(null).map(() => []);
    
    // Initialize with all disks on first peg
    for (let i = numberOfDisks; i >= 1; i--) {
      currentPegs[0].push(i);
    }
    setManualPegs(currentPegs.map(p => [...p]));
    
    // Animate each move
    for (let move of movesArray) {
      await new Promise(resolve => setTimeout(resolve, 500)); // Delay between moves
      
      const [from, to] = move.split('->');
      const fromIndex = pegLabels.indexOf(from);
      const toIndex = pegLabels.indexOf(to);
      
      if (fromIndex !== -1 && toIndex !== -1 && currentPegs[fromIndex].length > 0) {
        const disk = currentPegs[fromIndex].pop();
        currentPegs[toIndex].push(disk);
        setManualPegs(currentPegs.map(p => [...p]));
      }
    }
    
    setAnimatingManual(false);
    // Wait a bit then submit
    await new Promise(resolve => setTimeout(resolve, 1000));
  };

  const handleManualSubmit = async (e) => {
    e.preventDefault();
    clearInterval(timerInterval);
    setSubmitLoading(true);
    setError('');

    try {
      // Parse and animate moves
      const movesArray = manualSequence.split(',').map(m => m.trim());
      await animateManualMoves(movesArray);
      
      // Submit answer
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
      setAnimatingManual(false);
    }
  };

  const handleReset = () => {
    const initialPegs = Array(numberOfPegs).fill(null).map(() => []);
    for (let i = numberOfDisks; i >= 1; i--) {
      initialPegs[0].push(i);
    }
    setPegs(initialPegs);
    setManualPegs(initialPegs.map(p => [...p]));
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

  // Show algorithm results panel (collapsible)
  const renderAlgorithmResults = () => {
    if (!gameData) return null;

    return (
      <div className={`algorithm-results-panel ${showReferenceSolutions ? 'visible' : 'hidden'}`}>
        <button 
          className="toggle-solutions-btn"
          onClick={() => setShowReferenceSolutions(!showReferenceSolutions)}
        >
          {showReferenceSolutions ? '❌ Hide' : '💡 Solutions'}
        </button>
      
        {showReferenceSolutions && (
          <>
            <h3>Reference Solutions</h3>
            {gameData.algorithm1Result && (
              <div className="algo-result">
                <h4>{gameData.algorithm1Result.algorithmName}</h4>
                <p>Minimum Moves: {gameData.algorithm1Result.minimumMoves}</p>
                <details>
                  <summary>Show Sequence</summary>
                  <p className="move-sequence">{gameData.algorithm1Result.moveSequence}</p>
                </details>
              </div>
            )}
            {gameData.algorithm2Result && (
              <div className="algo-result">
                <h4>{gameData.algorithm2Result.algorithmName}</h4>
                <p>Minimum Moves: {gameData.algorithm2Result.minimumMoves}</p>
                <details>
                  <summary>Show Sequence</summary>
                  <p className="move-sequence">{gameData.algorithm2Result.moveSequence}</p>
                </details>
              </div>
            )}
          </>
        )}
      </div>
    );
  };

  return (
    <div className="gameplay-container">
      {/* Header with stats */}
      <div className="gameplay-header">
        <button className="quit-button-game" onClick={onBack}>
          <X size={20} />
          Quit Game
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

      <div className="gameplay-content">
        <div className="game-area">
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
                    {/* Destination indicator */}
                    {pegIndex === numberOfPegs - 1 && (
                      <div className="destination-badge">
                        🎯 Destination
                      </div>
                    )}
                    
                    <div className="peg-structure">
                      <div className="peg-rod"></div>
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
                      <div className="peg-base">{getPegLabel(pegIndex)}</div>
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
              {/* Visual representation for manual mode */}
              <div className="manual-pegs-visual">
                <h3>Tower Preview</h3>
                <div className="pegs-container-small">
                  {manualPegs.map((peg, pegIndex) => (
                    <div key={pegIndex} className="peg-column-small">
                      <div className="peg-structure-small">
                        <div className="peg-rod-small"></div>
                        <div className="disk-stack-small">
                          {peg.map((disk, diskIndex) => (
                            <div
                              key={`${pegIndex}-${disk}-${diskIndex}`}
                              className="disk-item-small"
                              style={{
                                width: `${30 + disk * 12}px`,
                                backgroundColor: getDiskColor(disk),
                                animation: animatingManual ? 'diskMove 0.5s ease' : 'none'
                              }}
                            >
                              {disk}
                            </div>
                          ))}
                        </div>
                        <div className="peg-base-small">{getPegLabel(pegIndex)}</div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <form onSubmit={handleManualSubmit} className="manual-form">
  <div className="form-group">
    <label>Number of Moves</label>
    <input
      type="number"
      value={manualMoves}
      onChange={(e) => setManualMoves(e.target.value)}
      onBlur={(e) => {
        // Ensure value stays stable after blur
        if (e.target.value) {
          setManualMoves(e.target.value);
        }
      }}
      placeholder="e.g., 31"
      required
      min="1"
      disabled={animatingManual}
      autoComplete="off"
    />
  </div>

  <div className="form-group">
    <label>Move Sequence</label>
    <textarea
      value={manualSequence}
      onChange={(e) => {
        e.preventDefault();
        setManualSequence(e.target.value);
      }}
      onPaste={(e) => {
        e.preventDefault();
        const pastedText = e.clipboardData.getData('text');
        setManualSequence(pastedText);
      }}
      placeholder="e.g., A->C, A->B, C->B, ..."
      required
      rows="4"
      disabled={animatingManual}
      autoComplete="off"
    />
    <small>Format: A→C, A→B, C→B (comma-separated)</small>
  </div>

  <button 
    type="submit" 
    className="submit-button" 
    disabled={submitLoading || animatingManual}
  >
    {submitLoading || animatingManual ? (
      <>
        <Loader className="spinner" />
        {animatingManual ? 'Animating...' : 'Submitting...'}
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

        {/* Algorithm Results Panel */}
        {renderAlgorithmResults()}
      </div>
    </div>
  );
}

export default GamePlay;