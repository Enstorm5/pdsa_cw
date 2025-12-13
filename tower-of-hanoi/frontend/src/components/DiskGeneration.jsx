import { useEffect, useState } from 'react';
import { ArrowRight, Layers, Loader } from 'lucide-react';
import './DiskGeneration.css';

const API_BASE_URL = 'http://localhost:8084/api/tower'; 

function DiskGeneration({ numberOfPegs, onGameDataReady, onContinue }) {
  const [loading, setLoading] = useState(true);
  const [numberOfDisks, setNumberOfDisks] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const startGame = async () => {
      try {
        setLoading(true);
        console.log('Starting game with', numberOfPegs, 'pegs');
        
        const response = await fetch(`${API_BASE_URL}/start`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            numberOfPegs: numberOfPegs
          })
        });

        if (!response.ok) {
          throw new Error('Failed to start game');
        }

        const data = await response.json();
        console.log('Game started successfully:', data);
        
        setNumberOfDisks(data.numberOfDisks);
        onGameDataReady(data);
        setLoading(false);
      } catch (err) {
        console.error('Error starting game:', err);
        setError(err.message);
        setLoading(false);
      }
    };

    if (numberOfPegs) {
      startGame();
    }
  }, [numberOfPegs, onGameDataReady]);

  if (loading) {
    return (
      <div className="disk-generation">
        <div className="diskgen-card">
          <Loader size={80} className="spinner" />
          <h2 className="diskgen-title">Generating Game Round...</h2>
          <p className="announcement-text">Randomly selecting disks for this round</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="disk-generation">
        <div className="diskgen-card">
          <h2 className="diskgen-title">Error</h2>
          <p className="announcement-text" style={{ color: 'red' }}>{error}</p>
          <button className="continue-button" onClick={() => window.location.reload()}>
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="disk-generation">
      <div className="diskgen-card">
        <div className="diskgen-icon">
          <Layers size={80} />
        </div>
        
        <h2 className="diskgen-title">Game Round Generated!</h2>
        
        <div className="disk-announcement">
          <p className="announcement-text">This round will use</p>
          <div className="disk-count-badge">{numberOfDisks}</div>
          <p className="announcement-text">disks</p>
        </div>

        <div className="disk-preview">
          {numberOfDisks && [...Array(Math.min(numberOfDisks, 7))].map((_, i) => {
            const diskNum = numberOfDisks - i;
            const width = 40 + (diskNum * 12);
            return (
              <div
                key={i}
                className="preview-disk"
                style={{
                  width: `${width}px`,
                  backgroundColor: `hsl(${(diskNum * 35) % 360}, 65%, 70%)`,
                  animationDelay: `${i * 0.1}s`
                }}
              />
            );
          })}
        </div>

        <button className="continue-button" onClick={onContinue}>
          Continue
          <ArrowRight size={20} />
        </button>
      </div>
    </div>
  );
}

export default DiskGeneration;