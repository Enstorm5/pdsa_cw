export default function HistoryTable({ history, loading, error, onRefresh }) {
  return (
    <div className="card">
      <div className="card-header">
        <h2>Previous Games</h2>
        <button type="button" className="secondary-btn" onClick={onRefresh} disabled={loading}>
          {loading ? "Refreshing..." : "Refresh"}
        </button>
      </div>

      {error && <div className="status error">{error}</div>}

      {loading ? (
        <p>Loading history...</p>
      ) : history.length === 0 ? (
        <p>No games played yet.</p>
      ) : (
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Player</th>
                <th>Board Size</th>
                <th>Correct Answer</th>
                <th>Your Answer</th>
                <th>Result</th>
                <th>BFS Time (ns)</th>
                <th>Dijkstra Time (ns)</th>
                <th>Created At</th>
              </tr>
            </thead>
            <tbody>
              {history.map((row) => (
                <tr key={row.id}>
                  <td>{row.playerName}</td>
                  <td>{row.boardSize}</td>
                  <td>{row.correctAnswer}</td>
                  <td>{row.playerAnswer}</td>
                  <td className={row.correct ? "success" : "error"}>{row.correct ? "Correct" : "Incorrect"}</td>
                  <td>{row.bfsTime}</td>
                  <td>{row.dijkstraTime}</td>
                  <td>{row.createdAt}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
