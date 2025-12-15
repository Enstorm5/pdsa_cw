import "./BoardGrid.css";

export default function BoardGrid({ size, playerPosition, cellSize }) {
  const total = size * size;
  const cells = Array.from({ length: total }, (_, i) => total - i);

  return (
    <div
      className="board"
      style={{
        gridTemplateColumns: `repeat(${size}, ${cellSize}px)`,
        gridTemplateRows: `repeat(${size}, ${cellSize}px)`
      }}
    >
      {cells.map(cell => (
        <div key={cell} className="cell">
          <span className="cell-number">{cell}</span>
          {playerPosition === cell && <div className="player-token" />}
        </div>
      ))}
    </div>
  );
}
