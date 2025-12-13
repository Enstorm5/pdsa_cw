import { useState } from 'react';
import WelcomeScreen from './components/WelcomeScreen';
import DiskGeneration from './components/DiskGeneration';
import PegSelection from './components/PegSelection';
import GameInstructions from './components/GameInstructions';
import GamePlay from './components/GamePlay';
import GameResult from './components/GameResult';
import './App.css';

function App() {
  const [gameState, setGameState] = useState('welcome'); // welcome, pegSelect, diskGen, instructions, playing, result
  const [playerName, setPlayerName] = useState('');
  const [numberOfDisks, setNumberOfDisks] = useState(null);
  const [numberOfPegs, setNumberOfPegs] = useState(null);
  const [gameData, setGameData] = useState(null);
  const [resultData, setResultData] = useState(null);

  const handleWelcomeComplete = (name) => {
    setPlayerName(name);
    setGameState('pegSelect');
  };

  const handlePegSelection = (pegs) => {
    setNumberOfPegs(pegs);
    setGameState('diskGen');
  };

  const handleDiskGenComplete = () => {
    setGameState('instructions');
  };

  const handleInstructionsComplete = () => {
    setGameState('playing');
  };

  const handleGameDataReady = (data) => {
    setNumberOfDisks(data.numberOfDisks);
    setGameData(data);
    setGameState('instructions');
  };

  const handleGameSubmit = (result) => {
    setResultData(result);
    setGameState('result');
  };

  const handleBackToMenu = () => {
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
        {gameState === 'pegSelect' && (
          <PegSelection onSelect={handlePegSelection} onBack={handleBackToMenu} />
        )}
        {gameState === 'diskGen' && (
          <DiskGeneration
            numberOfDisks={numberOfDisks}
            numberOfPegs={numberOfPegs}
            onGameDataReady={handleGameDataReady}
            onContinue={handleDiskGenComplete}
          />
        )}
        {gameState === 'instructions' && (
          <GameInstructions
            numberOfDisks={numberOfDisks}
            numberOfPegs={numberOfPegs}
            onContinue={handleInstructionsComplete}
            onBack={handleBackToMenu}
          />
        )}
        {gameState === 'playing' && (
          <GamePlay
            playerName={playerName}
            numberOfDisks={numberOfDisks}
            numberOfPegs={numberOfPegs}
            gameData={gameData}
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