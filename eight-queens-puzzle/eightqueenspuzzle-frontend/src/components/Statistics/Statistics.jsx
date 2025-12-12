import React from 'react';
import './Statistics.css';

const Statistics = ({ stats }) => {
  return (
    <div className="statistics-container">
      <h3 className="stats-title">Game Statistics</h3>
      
      <div className="stats-grid">
        <div className="stat-item">
          <span className="stat-label">Total Solutions:</span>
          <span className="stat-value">{stats.totalSolutions}</span>
        </div>
        
        <div className="stat-item">
          <span className="stat-label">Found by Players:</span>
          <span className="stat-value success">{stats.foundSolutions}</span>
        </div>
        
        <div className="stat-item">
          <span className="stat-label">Remaining:</span>
          <span className="stat-value">{stats.remainingSolutions}</span>
        </div>
        
        <div className="stat-item">
          <span className="stat-label">Total Players:</span>
          <span className="stat-value">{stats.totalPlayers}</span>
        </div>
        
        {stats.lastSequentialTime !== null && (
          <div className="stat-item">
            <span className="stat-label">Sequential Time:</span>
            <span className="stat-value">{stats.lastSequentialTime}ms</span>
          </div>
        )}
        
        {stats.lastThreadedTime !== null && (
          <div className="stat-item">
            <span className="stat-label">Threaded Time:</span>
            <span className="stat-value">{stats.lastThreadedTime}ms</span>
          </div>
        )}
      </div>
    </div>
  );
};

export default Statistics;