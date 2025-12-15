export default function SnakesLaddersSVG({ size, snakes, ladders, cellSize }) {
  const boardPx = size * cellSize;

  // Convert cell number to x,y coordinates
  const cellToXY = (cell) => {
    const row = Math.floor((cell - 1) / size);
    const col = (cell - 1) % size;

    return {
      x: col * cellSize + cellSize / 2,
      y: boardPx - (row * cellSize + cellSize / 2),
    };
  };

  // Draw a realistic ladder (rails + rungs)
  const drawLadder = (start, end, key) => {
    const offset = 6;
    const steps = 6;

    return (
      <g key={key}>
        {/* Rails */}
        <line
          x1={start.x - offset}
          y1={start.y}
          x2={end.x - offset}
          y2={end.y}
          stroke="#c9a227"
          strokeWidth="4"
        />
        <line
          x1={start.x + offset}
          y1={start.y}
          x2={end.x + offset}
          y2={end.y}
          stroke="#c9a227"
          strokeWidth="4"
        />

        {/* Rungs */}
        {Array.from({ length: steps }).map((_, i) => {
          const t = i / steps;
          const x = start.x + (end.x - start.x) * t;
          const y = start.y + (end.y - start.y) * t;

          return (
            <line
              key={i}
              x1={x - offset}
              y1={y}
              x2={x + offset}
              y2={y}
              stroke="#f4d03f"
              strokeWidth="3"
            />
          );
        })}
      </g>
    );
  };

  // Draw a realistic snake (curved body + head)
  const drawSnake = (start, end, key) => {
    const midX = (start.x + end.x) / 2;
    const midY = Math.min(start.y, end.y) - 40;

    return (
      <g key={key}>
        {/* Snake body */}
        <path
          d={`M ${start.x} ${start.y} Q ${midX} ${midY} ${end.x} ${end.y}`}
          stroke="#2e7d32"
          strokeWidth="10"
          fill="none"
          strokeLinecap="round"
        />

        {/* Snake head */}
        <circle
          cx={start.x}
          cy={start.y}
          r="6"
          fill="#1b5e20"
        />
      </g>
    );
  };

  return (
    <svg
      width={boardPx}
      height={boardPx}
      viewBox={`0 0 ${boardPx} ${boardPx}`}
      style={{
        position: "absolute",
        top: 0,
        left: 0,
        zIndex: 2,
        pointerEvents: "none",
      }}
    >
      {/* Ladders */}
      {Object.entries(ladders).map(([s, e]) =>
        drawLadder(cellToXY(+s), cellToXY(+e), `ladder-${s}`)
      )}

      {/* Snakes */}
      {Object.entries(snakes).map(([s, e]) =>
        drawSnake(cellToXY(+s), cellToXY(+e), `snake-${s}`)
      )}
    </svg>
  );
}
