import React from 'react';

export default function CitySelectionPanel({ cities, selectedCities, onToggleCity, gameData, onReset }) {
    return (
        <div className="city-panel">
            <div className="panel-info">
                <h3 className="panel-title">Game Info</h3>
                <div className="info-item">Player: <span>{gameData.playerName}</span></div>
                <div className="info-item">Home City: <span>{gameData.homeCity}</span></div>
                <div className="info-item">Session: <span>#{gameData.sessionId}</span></div>
            </div>

            <div className="panel-divider"></div>

            <h3 className="panel-title">Select Route</h3>
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

            <button className="reset-button" onClick={onReset} title="Clear Selection">
                Reset Route
            </button>
        </div>
    );
}
