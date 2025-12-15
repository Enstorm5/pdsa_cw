import React, { useState } from 'react';
import { Canvas } from '@react-three/fiber';
import { startGame, selectCities, submitSolution } from './api/gameApi';
import Interface from './components/Interface';
import Experience from './components/Experience';
import CitySelectionPanel from './components/CitySelectionPanel';

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const [gameData, setGameData] = useState(null);
  const [selectedCities, setSelectedCities] = useState([]);
  const [gamePhase, setGamePhase] = useState('SELECTION'); // 'SELECTION', 'ROUTING', 'RESULTS'
  const [startTime, setStartTime] = useState(null);
  const [solutionResult, setSolutionResult] = useState(null);
  const [activeGameCities, setActiveGameCities] = useState([]); // Subset of cities for the routing phase

  const handleGameStart = (data) => {
    setGameData(data);
    setGameStarted(true);
  };

  const handleToggleCity = (city) => {
    if (gamePhase === 'ROUTING') {
      // In Routing phase, we just build the path path sequence
      setSelectedCities(prev => [...prev, city]);
    } else {
      // In Selection phase, we toggle inclusion
      setSelectedCities(prev => {
        if (prev.includes(city)) {
          return prev.filter(c => c !== city);
        } else {
          return [...prev, city];
        }
      });
    }
  };


  const handleReset = () => {
    setSelectedCities([]);
  };

  const handleConfirmCities = async () => {
    if (selectedCities.length === 0) return;
    try {
      // 1. Send selected cities to backend
      await selectCities(gameData.sessionId, selectedCities);

      // 2. Lock in these cities for the routing phase
      // Ensure home city is included in the available set if not already selected
      const routingCities = [...selectedCities];
      if (!routingCities.includes(gameData.homeCity)) {
        routingCities.push(gameData.homeCity);
      }
      setActiveGameCities(routingCities);

      // 3. Reset selection so user can build the path
      setSelectedCities([]);

      // 4. Start Timer and switch phase
      setStartTime(Date.now());
      setGamePhase('ROUTING');

    } catch (error) {
      console.error('Failed to confirm cities:', error);
      alert('Failed to start routing phase.');
    }
  };

  const handleSubmitSolution = async () => {
    if (selectedCities.length === 0) return;

    // Calculate time taken
    // Calculate time taken
    const endTime = Date.now();
    const timeTaken = endTime - startTime;

    try {
      const result = await submitSolution(gameData.sessionId, selectedCities, timeTaken);
      setSolutionResult(result);
      setGamePhase('RESULTS');
    } catch (error) {
      console.error('Failed to submit solution:', error);
      alert('Failed to submit solution.');
    }
  };

  const handleRestart = () => {
    setGamePhase('SELECTION');
    setSelectedCities([]);
    setSolutionResult(null);
    setActiveGameCities([]);
    setStartTime(null);
    // Might need to re-fetch initial game data or just reset state
  };

  return (
    <>
      <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%' }}>
        <Canvas camera={{ position: [0, 15, 20], fov: 45 }}>
          {gameStarted && gameData && (
            <Experience
              gameData={gameData}
              selectedCities={selectedCities}
            />
          )}
        </Canvas>
      </div>

      {!gameStarted && (
        <Interface onGameStart={handleGameStart} />
      )}

      {gameStarted && gameData && (
        <CitySelectionPanel
          cities={gamePhase === 'ROUTING' ? activeGameCities : gameData.cityLabels}
          selectedCities={selectedCities}
          onToggleCity={handleToggleCity}
          gameData={gameData}
          onReset={handleReset}
          onConfirm={handleConfirmCities}
          onSubmit={handleSubmitSolution}
          onRestart={handleRestart}
          gamePhase={gamePhase}
          solutionResult={solutionResult}
        />
      )}
    </>
  );
}

export default App;
