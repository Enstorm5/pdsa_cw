import axios from 'axios';

const API_BASE_URL = 'http://localhost:8086/api/game';

export const startGame = async (playerName) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/start`, { playerName });
        return response.data;
    } catch (error) {
        console.error('Error starting game:', error);
        throw error;
    }
};

export const selectCities = async (sessionId, cities) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/select-cities`, { sessionId, cities });
        return response.data;
    } catch (error) {
        console.error('Error selecting cities:', error);
        throw error;
    }
};

export const submitSolution = async (sessionId, proposedPath, timeTakenByUserMs) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/solve`, {
            sessionId,
            proposedPath,
            timeTakenByUserMs
        });
        return response.data;
    } catch (error) {
        console.error('Error submitting solution:', error);
        throw error;
    }
};
