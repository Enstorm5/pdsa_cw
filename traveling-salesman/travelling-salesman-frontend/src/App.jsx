import React, { useState } from 'react';
import { Canvas } from '@react-three/fiber';
import Interface from './components/Interface';
import Experience from './components/Experience';
import CitySelectionPanel from './components/CitySelectionPanel';

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const [gameData, setGameData] = useState(null);
  const [selectedCities, setSelectedCities] = useState([]);

  const handleGameStart = (data) => {
    setGameData(data);
    setGameStarted(true);
  };

  const handleToggleCity = (city) => {
    setSelectedCities(prev => {
      if (prev.includes(city)) {
        return prev.filter(c => c !== city);
      } else {
        return [...prev, city];
      }
    });
  };

  const handleReset = () => {
    setSelectedCities([]);
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
          cities={gameData.cityLabels}
          selectedCities={selectedCities}
          onToggleCity={handleToggleCity}
          gameData={gameData}
          onReset={handleReset}
        />
      )}
    </>
  );
}

export default App;
