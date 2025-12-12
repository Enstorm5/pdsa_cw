import React from 'react';
import './PlayerInput.css';

const PlayerInput = ({ playerName, onPlayerNameChange, algorithm, onAlgorithmChange }) => {
  return (
    <div className="player-input-container">
      <div className="input-group">
        <label htmlFor="playerName">Player Name:</label>
        <input
          id="playerName"
          type="text"
          value={playerName}
          onChange={(e) => onPlayerNameChange(e.target.value)}
          placeholder="Enter your name"
          className="input-field"
        />
      </div>

      <div className="input-group">
        <label htmlFor="algorithm">Algorithm Type:</label>
        <select
          id="algorithm"
          value={algorithm}
          onChange={(e) => onAlgorithmChange(e.target.value)}
          className="select-field"
        >
          <option value="sequential">Sequential</option>
          <option value="threaded">Multi-threaded</option>
        </select>
      </div>
    </div>
  );
};

export default PlayerInput;