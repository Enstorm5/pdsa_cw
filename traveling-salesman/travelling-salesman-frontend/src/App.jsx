import React, { useState } from 'react';
import { Canvas } from '@react-three/fiber';
import Interface from './components/Interface';
import Experience from './components/Experience';

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const [gameData, setGameData] = useState(null);

  const handleGameStart = (data) => {
    setGameData(data);
    setGameStarted(true);
  };

  return (
    <>
      <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%' }}>
        <Canvas camera={{ position: [0, 15, 20], fov: 45 }}>
          {gameStarted && gameData && <Experience gameData={gameData} />}
        </Canvas>
      </div>

      {!gameStarted && (
        <Interface onGameStart={handleGameStart} />
      )}

      {/* Simple overlay to show player info if game is started */}
      {gameStarted && gameData && (
        <div style={{
          position: 'absolute',
          top: '20px',
          left: '20px',
          color: 'white',
          background: 'rgba(0,0,0,0.5)',
          padding: '20px',
          borderRadius: '10px',
          pointerEvents: 'none',
          fontFamily: 'monospace'
        }}>
          <h3>Mission Control</h3>
          <p>Pilot: {gameData.playerName}</p>
          <p>Home Base: {gameData.homeCity}</p>
          <p>Session: #{gameData.sessionId}</p>
        </div>
      )}
    </>
  );
}

export default App;
