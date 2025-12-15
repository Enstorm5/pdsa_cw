import React, { useState, useEffect } from 'react';

export default function CitySelectionPanel({
    cities,
    selectedCities,
    onToggleCity,
    gameData,
    onReset,
    onConfirm,
    onSubmit,
    onRestart,
    onRetry,
    gamePhase,
    solutionResult
}) {
    const [timer, setTimer] = useState(0);
    const [showAnswer, setShowAnswer] = useState(false);

    useEffect(() => {
        if (solutionResult === null) {
            setShowAnswer(false);
        }
    }, [solutionResult]);

    useEffect(() => {
        let interval;
        if (gamePhase === 'ROUTING') {
            const startTime = Date.now();
            interval = setInterval(() => {
                setTimer(Date.now() - startTime);
            }, 100);
        } else {
            setTimer(0);
        }
        return () => clearInterval(interval);
    }, [gamePhase]);

    const formatTime = (ms) => {
        const seconds = Math.floor(ms / 1000);
        const milliseconds = Math.floor((ms % 1000) / 10);
        return `${seconds}.${milliseconds.toString().padStart(2, '0')}s`;
    };

    return (
        <div className="city-panel">
            <div className="panel-info">
                <h3 className="panel-title">Game Info</h3>
                <div className="info-item">Player: <span>{gameData.playerName}</span></div>
                <div className="info-item">Home City: <span className="home-city-highlight">{gameData.homeCity}</span></div>
                <div className="info-item">Session: <span>#{gameData.sessionId}</span></div>
                {gamePhase === 'ROUTING' && (
                    <div className="info-item" style={{ marginTop: '10px', color: '#0ff', fontSize: '1.2rem' }}>
                        Time: <span>{formatTime(timer)}</span>
                    </div>
                )}
            </div>

            <div className="panel-divider"></div>

            {gamePhase === 'RESULTS' && solutionResult ? (
                <div className="results-container">
                    <h3 className={`result-title ${solutionResult.correct ? 'success' : 'failure'}`}>
                        {solutionResult.correct ? 'Success!' : 'Route Failed'}
                    </h3>
                    <div className="result-details">
                        <div className="result-row">Distance: <span>{solutionResult.submittedDistance}km</span></div>
                        <div className="result-row">Optimal: <span>{solutionResult.optimalDistance}km</span></div>
                        <div className="result-message">{solutionResult.message}</div>
                    </div>

                    {!solutionResult.correct && (
                        <>
                            {showAnswer ? (
                                <div className="optimal-path-hint">
                                    <p>Correct Path:</p>
                                    <div className="path-display">
                                        {solutionResult.optimalPath.join(' → ')}
                                    </div>
                                </div>
                            ) : null}

                            <div className="button-group">
                                <button className="start-button" onClick={onRetry}>
                                    Try Again
                                </button>
                                <button
                                    className="reset-button"
                                    onClick={() => setShowAnswer(!showAnswer)}
                                >
                                    {showAnswer ? 'Hide Answer' : 'Show Answer'}
                                </button>
                            </div>
                        </>
                    )}

                    <button className="reset-button" onClick={onRestart} style={{ marginTop: '20px', width: '100%' }}>
                        Play Again
                    </button>
                </div>
            ) : (
                <>
                    <h3 className="panel-title">
                        {gamePhase === 'SELECTION' ? 'Select Cities' : 'Build Route'}
                    </h3>

                    <div className="city-list">
                        {cities.map((city) => {
                            const index = selectedCities.indexOf(city);
                            const isSelected = index !== -1;

                            return (
                                <button
                                    key={city}
                                    className={`city-option ${isSelected ? 'selected' : ''}`}
                                    onClick={() => onToggleCity(city)}
                                >
                                    <span className="city-name">{city}</span>
                                    {isSelected && <span className="selection-badge">{index + 1}</span>}
                                </button>
                            );
                        })}
                    </div>
                    {selectedCities.length > 0 && (
                        <div className="selection-summary">
                            Path: {selectedCities.join(' → ')}
                        </div>
                    )}

                    <div className="button-group">
                        {gamePhase === 'SELECTION' ? (
                            <button
                                className="start-button"
                                onClick={onConfirm}
                                disabled={selectedCities.length === 0}
                            >
                                Confirm Selection
                            </button>
                        ) : (
                            <button
                                className="start-button"
                                onClick={onSubmit}
                                disabled={selectedCities.length === 0}
                            >
                                Submit Route
                            </button>
                        )}

                        <button className="reset-button" onClick={onReset} title="Clear Selection">
                            Reset
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}
