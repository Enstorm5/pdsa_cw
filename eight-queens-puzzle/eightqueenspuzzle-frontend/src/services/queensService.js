import api from './api';

export const queensService = {
  // Run sequential algorithm
  runSequential: async () => {
    const response = await api.post('/queens/solve/sequential');
    return response.data;
  },

  // Run threaded algorithm
  runThreaded: async () => {
    const response = await api.post('/queens/solve/threaded');
    return response.data;
  },

  // Submit solution
  submitSolution: async (playerName, queenPositions) => {
    const response = await api.post('/queens/submit', {
      playerName,
      queenPositions,
    });
    return response.data;
  },

  // Get game statistics
  getStats: async () => {
    const response = await api.get('/queens/stats');
    return response.data;
  },

  // Reset game
  resetGame: async () => {
    const response = await api.post('/queens/reset');
    return response.data;
  },
};