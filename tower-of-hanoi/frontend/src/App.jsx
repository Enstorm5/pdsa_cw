import { useState } from 'react';
import WelcomeScreen from './components/WelcomeScreen';
import DiskGeneration from './components/DiskGeneration';
import PegSelection from './components/PegSelection';
import GameInstructions from './components/GameInstructions';
import GamePlay from './components/GamePlay';
import GameResult from './components/GameResult';
import './App.css';

function App() {
  const [gameState, setGameState] = useState('welcome'); // welcome, diskGen, pegSelect, instructions, playing, result
  const [playerName, setPlayerName] = useState('');
  const [numberOfDisks, setNumberOfDisks] = useState(null);
  const [numberOfPegs, setNumberOfPegs] = useState(null);
  const [gameData, setGameData] = useState(null);
  const [resultData, setResultData] = useState(null);

  const handleWelcomeComplete = (name) => {
    setPlayerName(name);
    // System randomly generates disk count
    const randomDisks = Math.floor(Math.random() * 6) + 5; // 5-10
    setNumberOfDisks(randomDisks);
    setGameState('diskGen');
  };

  const handleDiskGenComplete = () => {
    setGameState('pegSelect');
  };

  const handlePegSelection = (pegs) => {
    setNumberOfPegs(pegs);
    setGameState('instructions');
  };

  const handleInstructionsComplete = () => {
    setGameState('playing');
  };

  const handleGameDataReady = (data) => {
    setGameData(data);
  };

  const handleGameSubmit = (result) => {
    setResultData(result);
    setGameState('result');
  };

  const handleBackToMenu = () => {
    // In your main app, this would navigate back to the main menu
    // For now, we'll reset to welcome
    setGameState('welcome');
    setPlayerName('');
    setNumberOfDisks(null);
    setNumberOfPegs(null);
    setGameData(null);
    setResultData(null);
  };

  const handlePlayAgain = () => {
    setGameState('welcome');
    setPlayerName('');
    setNumberOfDisks(null);
    setNumberOfPegs(null);
    setGameData(null);
    setResultData(null);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1 className="app-title">Tower of Hanoi</h1>
        <p className="app-subtitle">A Classic Mathematical Puzzle</p>
      </header>

      <main className="app-main">
        {gameState === 'welcome' && (
          <WelcomeScreen onStart={handleWelcomeComplete} />
        )}
        {gameState === 'diskGen' && (
          <DiskGeneration
            numberOfDisks={numberOfDisks}
            onContinue={handleDiskGenComplete}
          />
        )}
        {gameState === 'pegSelect' && (
          <PegSelection onSelect={handlePegSelection} onBack={handleBackToMenu} />
        )}
        {gameState === 'instructions' && (
          <GameInstructions
            numberOfDisks={numberOfDisks}
            numberOfPegs={numberOfPegs}
            onContinue={handleInstructionsComplete}
          />
        )}
        {gameState === 'playing' && (
          <GamePlay
            playerName={playerName}
            numberOfDisks={numberOfDisks}
            numberOfPegs={numberOfPegs}
            onGameDataReady={handleGameDataReady}
            onSubmit={handleGameSubmit}
            onBack={handleBackToMenu}
          />
        )}
        {gameState === 'result' && (
          <GameResult
            resultData={resultData}
            onPlayAgain={handlePlayAgain}
            onBackToMenu={handleBackToMenu}
          />
        )}
      </main>

      <footer className="app-footer">
        <p>© 2025 Tower of Hanoi Game | PDSA Course Work</p>
      </footer>
    </div>
  );
}

export default App;