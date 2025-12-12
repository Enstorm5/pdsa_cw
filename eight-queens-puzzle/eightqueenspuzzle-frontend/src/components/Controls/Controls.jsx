import React from 'react';
import './Controls.css';

const Controls = ({
  onRunAlgorithm,
  onSubmitSolution,
  onResetBoard,
  onResetGame,
  isLoading,
}) => {
  return (
    <div className="controls-container">
      <div className="button-group">
        <button
          onClick={onRunAlgorithm}
          disabled={isLoading}
          className="btn btn-primary"
        >
          {isLoading ? 'Running...' : 'Run Algorithm'}
        </button>

        <button
          onClick={onSubmitSolution}
          disabled={isLoading}
          className="btn btn-success"
        >
          Submit Solution
        </button>
      </div>

      <div className="button-group">
        <button
          onClick={onResetBoard}
          className="btn btn-secondary"
        >
          Reset Board
        </button>

        <button
          onClick={onResetGame}
          className="btn btn-danger"
        >
          Reset Game
        </button>
      </div>
    </div>
  );
};

export default Controls;