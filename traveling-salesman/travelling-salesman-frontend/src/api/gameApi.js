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
