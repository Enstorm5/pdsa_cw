import React, { useState, useEffect } from 'react';
import { AlertCircle, CheckCircle, XCircle } from 'lucide-react';
import Chessboard from './components/Chessboard/Chessboard';
import PlayerInput from './components/PlayerInput/PlayerInput';
import Statistics from './components/Statistics/Statistics';
import Controls from './components/Controls/Controls';
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

  // Load stats on mount
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
        showMessage('You can only place 8 queens!', 'error');
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
      } else {
        result = await queensService.runThreaded();
      }

      showMessage(
        `${result.algorithmType} completed in ${result.executionTimeMs}ms. Found ${result.totalSolutions} solutions.`,
        'success'
      );
      await loadStats();
    } catch (error) {
      showMessage('Error running algorithm: ' + error.message, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmitSolution = async () => {
    if (!playerName.trim()) {
      showMessage('Please enter your name!', 'error');
      return;
    }

    if (selectedCells.length !== 8) {
      showMessage('Please place exactly 8 queens on the board!', 'error');
      return;
    }

    setIsLoading(true);
    setMessage({ text: '', type: '' });

    try {
      // Convert selected cells to position array
      const positions = Array(8).fill(-1);
      selectedCells.forEach((cell) => {
        positions[cell.col] = cell.row;
      });

      const result = await queensService.submitSolution(playerName, positions);

      if (result.accepted) {
        showMessage(result.message, 'success');
        setSelectedCells([]);
      } else {
        showMessage(result.message, 'warning');
      }

      await loadStats();
    } catch (error) {
      const errorMsg = error.response?.data?.message || error.message;
      showMessage('Error: ' + errorMsg, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleResetBoard = () => {
    setSelectedCells([]);
    setMessage({ text: '', type: '' });
  };

  const handleResetGame = async () => {
    if (!window.confirm('Are you sure you want to reset the entire game? This will clear all found solutions.')) {
      return;
    }

    setIsLoading(true);
    try {
      await queensService.resetGame();
      showMessage('Game reset successfully!', 'success');
      await loadStats();
      setSelectedCells([]);
    } catch (error) {
      showMessage('Error resetting game: ' + error.message, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const showMessage = (text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: '', type: '' }), 5000);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Eight Queens Puzzle</h1>
        <p>Place 8 queens on the board so no queen attacks another</p>
      </header>

      <div className="app-container">
        <aside className="sidebar">
          <PlayerInput
            playerName={playerName}
            onPlayerNameChange={setPlayerName}
            algorithm={algorithm}
            onAlgorithmChange={setAlgorithm}
          />

          <Statistics stats={stats} />

          <Controls
            onRunAlgorithm={handleRunAlgorithm}
            onSubmitSolution={handleSubmitSolution}
            onResetBoard={handleResetBoard}
            onResetGame={handleResetGame}
            isLoading={isLoading}
          />

          <div className="instructions">
            <h4>Instructions:</h4>
            <ul>
              <li>Click cells to place/remove queens</li>
              <li>Place exactly 8 queens</li>
              <li>No two queens should attack each other</li>
              <li>Submit when ready!</li>
            </ul>
          </div>
        </aside>

        <main className="main-content">
          <Chessboard
            selectedCells={selectedCells}
            onCellClick={handleCellClick}
          />

          {message.text && (
            <div className={`message message-${message.type}`}>
              {message.type === 'success' && <CheckCircle size={20} />}
              {message.type === 'error' && <XCircle size={20} />}
              {message.type === 'warning' && <AlertCircle size={20} />}
              <span>{message.text}</span>
            </div>
          )}
        </main>
      </div>
    </div>
  );
}

export default App;