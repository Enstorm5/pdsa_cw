import { useEffect, useState } from "react";
// Explicit extensions help Vite reliably resolve local component imports.
import GameForm from "./components/GameForm.jsx";
import HistoryTable from "./components/HistoryTable.jsx";
import api from "./api";

export default function App() {
  const [history, setHistory] = useState([]);
  const [loadingHistory, setLoadingHistory] = useState(false);
  const [historyError, setHistoryError] = useState("");

  const loadHistory = async () => {
    setLoadingHistory(true);
    setHistoryError("");
    try {
      const { data } = await api.get("/history");
      setHistory(Array.isArray(data) ? data : []);
    } catch (error) {
      const message = error.response?.data?.message || "Unable to fetch game history.";
      setHistoryError(message);
    } finally {
      setLoadingHistory(false);
    }
  };

  useEffect(() => {
    loadHistory();
  }, []);

  return (
    <div className="container">
      <header className="hero">
        <div>
          <p className="eyebrow">Snake &amp; Ladder Solver</p>
          <h1>Compare your guess against BFS and Dijkstra</h1>
          <p className="subtitle">
            Submit your answer for the minimum number of moves to finish the board and see how it compares to
            algorithmic solutions.
          </p>
        </div>
      </header>
      <GameForm onPlaySuccess={loadHistory} />
      <HistoryTable history={history} loading={loadingHistory} error={historyError} onRefresh={loadHistory} />
    </div>
  );
}
