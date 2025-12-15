import React, { useState, useEffect } from 'react';
import { Crown, Sparkles, Trophy, RotateCcw, Send, Zap, AlertCircle } from 'lucide-react';
import { queensService } from './services/queensService';
import './App.css';

function App() {
  const [playerName, setPlayerName] = useState('');
  const [algorithm, setAlgorithm] = useState('sequential');
  const [selectedCells, setSelectedCells] = useState([]);
  const [stats, setStats] = useState({
    totalSolutions: 92,
    foundSolutions: 0,
    remainingSolutions: 92,
    totalPlayers: 0,
    lastSequentialTime: null,
    lastThreadedTime: null,
  });
  const [message, setMessage] = useState({ text: '', type: '' });
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const data = await queensService.getStats();
      setStats(data);
    } catch (error) {
      console.error('Error loading stats:', error);
    }
  };

  const handleCellClick = (row, col) => {
    const cellExists = selectedCells.some(
      (cell) => cell.row === row && cell.col === col
    );

    if (cellExists) {
      setSelectedCells(selectedCells.filter(
        (cell) => !(cell.row === row && cell.col === col)
      ));
    } else {
      if (selectedCells.length < 8) {
        setSelectedCells([...selectedCells, { row, col }]);
      } else {
        showMessage('⚠️ Maximum 8 queens allowed!', 'warning');
      }
    }
  };

  const handleRunAlgorithm = async () => {
    setIsLoading(true);
    setMessage({ text: '', type: '' });

    try {
      let result;
      if (algorithm === 'sequential') {
        result = await queensService.runSequential();
        showMessage(`⚡ Sequential: ${result.executionTimeMs}ms | Found 92 solutions`, 'success');
      } else {
        result = await queensService.runThreaded();
        showMessage(`🚀 Threaded: ${result.executionTimeMs}ms | Found 92 solutions`, 'success');
      }
      await loadStats();
    } catch (error) {
      showMessage('❌ Error running algorithm: ' + error.message, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmitSolution = async () => {
    if (!playerName.trim()) {
      showMessage('❌ Please enter your name first!', 'error');
      return;
    }

    if (selectedCells.length !== 8) {
      showMessage('❌ Place exactly 8 queens on the board!', 'error');
      return;
    }

    setIsLoading(true);
    setMessage({ text: '', type: '' });

    try {
      const positions = Array(8).fill(-1);
      selectedCells.forEach((cell) => {
        positions[cell.col] = cell.row;
      });

      const result = await queensService.submitSolution(playerName, positions);

      if (result.accepted) {
        showMessage(`✅ ${result.message} 🎉`, 'success');
        setSelectedCells([]);
      } else {
        showMessage(`⚠️ ${result.message}`, 'warning');
      }

      await loadStats();
    } catch (error) {
      const errorMsg = error.response?.data?.message || error.message;
      showMessage('❌ ' + errorMsg, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleResetBoard = () => {
    setSelectedCells([]);
    setMessage({ text: '', type: '' });
  };

  const handleResetGame = async () => {
    if (!window.confirm('Reset the entire game? This will clear all found solutions.')) {
      return;
    }

    setIsLoading(true);
    try {
      await queensService.resetGame();
      showMessage('✅ Game reset successfully!', 'success');
      await loadStats();
      setSelectedCells([]);
    } catch (error) {
      showMessage('❌ Error resetting game: ' + error.message, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const showMessage = (text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: '', type: '' }), 5000);
  };

  const isSelected = (row, col) => {
    return selectedCells.some((cell) => cell.row === row && cell.col === col);
  };

  return (
    <div className="app-container">
      {/* Header */}
      <div className="header">
        <div className="header-content">
          <Crown className="crown-icon crown-left" size={40} />
          <h1 className="title">Eight Queens</h1>
          <Crown className="crown-icon crown-right" size={40} />
        </div>
        <p className="subtitle">
          <Sparkles size={16} />
          Place 8 queens without any attacks!
          <Sparkles size={16} />
        </p>
      </div>

      <div className="main-grid">
        {/* Left Panel */}
        <div className="left-panel">
          {/* Player Card */}
          <div className="card player-card">
            <h3 className="card-title">
              <Trophy size={24} />
              Player Info
            </h3>
            <input
              type="text"
              value={playerName}
              onChange={(e) => setPlayerName(e.target.value)}
              placeholder="Your Name"
              className="input-field"
            />
            
            <div className="input-group">
              <label className="input-label">Algorithm</label>
              <select
                value={algorithm}
                onChange={(e) => setAlgorithm(e.target.value)}
                className="select-field"
              >
                <option value="sequential">⚡ Sequential</option>
                <option value="threaded">🚀 Multi-threaded</option>
              </select>
            </div>
          </div>

          {/* Stats Card */}
          <div className="card stats-card">
            <h3 className="card-title-white">
              <Sparkles size={24} />
              Game Stats
            </h3>
            <div className="stats-grid">
              <div className="stat-item">
                <span>Total Solutions:</span>
                <span className="stat-value">{stats.totalSolutions}</span>
              </div>
              <div className="stat-item">
                <span>Found:</span>
                <span className="stat-value-highlight">{stats.foundSolutions}</span>
              </div>
              {stats.lastSequentialTime && (
                <div className="stat-item">
                  <span>Sequential:</span>
                  <span className="stat-time">{stats.lastSequentialTime}ms</span>
                </div>
              )}
              {stats.lastThreadedTime && (
                <div className="stat-item">
                  <span>Threaded:</span>
                  <span className="stat-time">{stats.lastThreadedTime}ms</span>
                </div>
              )}
            </div>
          </div>

          {/* Controls */}
          <div className="card controls-card">
            <button
              onClick={handleRunAlgorithm}
              disabled={isLoading}
              className="btn btn-primary"
            >
              <Zap size={20} />
              Run Algorithm
            </button>
            
            <button
              onClick={handleSubmitSolution}
              disabled={isLoading}
              className="btn btn-success"
            >
              <Send size={20} />
              Submit Solution
            </button>
            
            <button
              onClick={handleResetBoard}
              className="btn btn-secondary"
            >
              <RotateCcw size={20} />
              Reset Board
            </button>

            <button
              onClick={handleResetGame}
              disabled={isLoading}
              className="btn btn-danger"
            >
              <RotateCcw size={20} />
              Reset Game
            </button>
          </div>
        </div>

        {/* Right Panel - Board */}
        <div className="right-panel">
          <div className="board-card">
            <div className="board-container">
              <div className="chessboard">
                {Array(8).fill(null).map((_, rowIndex) => (
                  <div key={rowIndex} className="board-row">
                    {Array(8).fill(null).map((_, colIndex) => {
                      const isLight = (rowIndex + colIndex) % 2 === 0;
                      const selected = isSelected(rowIndex, colIndex);
                      
                      return (
                        <button
                          key={`${rowIndex}-${colIndex}`}
                          onClick={() => handleCellClick(rowIndex, colIndex)}
                          className={`chess-cell ${isLight ? 'light' : 'dark'} ${selected ? 'selected' : ''}`}
                        >
                          {selected && <span className="queen-emoji">👑</span>}
                        </button>
                      );
                    })}
                  </div>
                ))}
              </div>
            </div>

            {message.text && (
              <div className={`message message-${message.type}`}>
                <AlertCircle size={20} />
                <span>{message.text}</span>
              </div>
            )}

            <div className="tips-box">
              <h4 className="tips-title">
                <Sparkles size={18} />
                Quick Tips:
              </h4>
              <ul className="tips-list">
                <li>🎯 Click cells to place queens</li>
                <li>👑 Place exactly 8 queens</li>
                <li>⚔️ No queen should attack another</li>
                <li>✨ Click Submit when ready!</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;