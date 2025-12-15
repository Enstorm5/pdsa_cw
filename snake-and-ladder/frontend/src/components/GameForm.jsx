import { useState, useEffect } from "react";
import ThreeBoard from "./ThreeBoard";
import api from "../api";
import "./GameForm.css";

export default function GameForm({ onPlaySuccess }) {
  const [playerName, setPlayerName] = useState("");
  const [boardSize, setBoardSize] = useState(6);
  const [playerPosition, setPlayerPosition] = useState(1);
  const [playerAnswer, setPlayerAnswer] = useState(null);
  const [rolling, setRolling] = useState(false);
  const [result, setResult] = useState(null);

  const [snakes, setSnakes] = useState({});
  const [ladders, setLadders] = useState({});

  // Generate snakes & ladders whenever board size changes
  useEffect(() => {
    generateBoardObjects(boardSize);
    setPlayerPosition(1);
    setResult(null);
    setPlayerAnswer(null);
  }, [boardSize]);

  const generateBoardObjects = (size) => {
    const total = size * size;
    const count = size - 2;
    const s = {};
    const l = {};

    // Ladders
    while (Object.keys(l).length < count) {
      const start = Math.floor(Math.random() * (total - size)) + 2;
      const end = start + Math.floor(Math.random() * size) + 2;
      if (end < total && !l[start] && !s[start]) {
        l[start] = end;
      }
    }

    // Snakes
    while (Object.keys(s).length < count) {
      const start = Math.floor(Math.random() * (total - size)) + size;
      const end = start - (Math.floor(Math.random() * size) + 2);
      if (end > 1 && !l[start] && !s[start]) {
        s[start] = end;
      }
    }

    setSnakes(s);
    setLadders(l);
  };

  const rollDice = () => Math.floor(Math.random() * 6) + 1;

  const animateMove = async (target) => {
    setRolling(true);
    let pos = playerPosition;

    return new Promise((resolve) => {
      const interval = setInterval(() => {
        pos += 1;
        setPlayerPosition(pos);

        if (pos >= target) {
          clearInterval(interval);
          setRolling(false);
          resolve();
        }
      }, 250);
    });
  };

  const jumpToCell = async (to) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        setPlayerPosition(to);
        resolve();
      }, 700);
    });
  };

  const generateChoices = () => {
    const estimate = Math.floor((boardSize * boardSize) / 6);
    return [estimate - 1, estimate, estimate + 1].filter(v => v > 0);
  };

  const playGame = async () => {
    const dice = rollDice();
    const maxCell = boardSize * boardSize;
    const target = Math.min(playerPosition + dice, maxCell);

    await animateMove(target);

    if (ladders[target]) {
      await jumpToCell(ladders[target]);
    } else if (snakes[target]) {
      await jumpToCell(snakes[target]);
    }

    try {
      const res = await api.post("/play", {
        playerName,
        boardSize,
        playerAnswer
      });
      setResult(res.data);
      if (typeof onPlaySuccess === 'function') {
        onPlaySuccess();
      }
    } catch {
      alert("Backend error. Is Spring Boot running?");
    }
  };

  return (
    <div className="game-card">
      <h2>🐍 Snake & Ladder Game (3D)</h2>

      <input
        placeholder="Player Name"
        value={playerName}
        onChange={(e) => setPlayerName(e.target.value)}
      />

      <input
        type="number"
        min={6}
        max={12}
        value={boardSize}
        onChange={(e) => setBoardSize(Number(e.target.value))}
      />

      {/* 3D BOARD */}
      <ThreeBoard
        size={boardSize}
        snakes={snakes}
        ladders={ladders}
        playerPosition={playerPosition}
      />

      {/* ANSWER SELECTION */}
      <div className="answer-box">
        <p><strong>Select minimum dice throws:</strong></p>
        {generateChoices().map(v => (
          <label key={v}>
            <input
              type="radio"
              checked={playerAnswer === v}
              onChange={() => setPlayerAnswer(v)}
            />
            {v}
          </label>
        ))}
      </div>

      <button
        disabled={!playerName || playerAnswer === null || rolling}
        onClick={playGame}
      >
        {rolling ? "Rolling Dice..." : "Play"}
      </button>

      {result && (
        <div className="result-box">
          <h3 className={result.correct ? "win" : "lose"}>
            {result.correct ? "YOU WIN 🎉" : "YOU LOSE ❌"}
          </h3>
          <p>Correct Answer: {result.correctAnswer}</p>
          <p>Your Answer: {result.playerAnswer}</p>
        </div>
      )}
    </div>
  );
}
