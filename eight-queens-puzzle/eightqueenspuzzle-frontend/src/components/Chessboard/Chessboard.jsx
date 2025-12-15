import React from 'react';
import './Chessboard.css';

const Chessboard = ({ selectedCells, onCellClick }) => {
  const board = Array(8).fill(null).map(() => Array(8).fill(null));

  const isSelected = (row, col) => {
    return selectedCells.some(cell => cell.row === row && cell.col === col);
  };

  return (
    <div className="chessboard-container">
      <div className="chessboard">
        {board.map((row, rowIndex) => (
          <div key={rowIndex} className="board-row">
            {row.map((_, colIndex) => {
              const isLight = (rowIndex + colIndex) % 2 === 0;
              const selected = isSelected(rowIndex, colIndex);

              return (
                <button
                  key={`${rowIndex}-${colIndex}`}
                  onClick={() => onCellClick(rowIndex, colIndex)}
                  className={`chess-cell ${isLight ? 'light' : 'dark'} ${selected ? 'selected' : ''}`}
                >
                  {selected && <span className="queen">♛</span>}
                </button>
              );
            })}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Chessboard;