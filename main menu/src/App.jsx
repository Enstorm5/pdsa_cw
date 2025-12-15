import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import GameMenu from './pages/GameMenu';
import SnakeLadder from './pages/SnakeLadder';
import TravelingSalesman from './pages/TravelingSalesman';
import TowerOfHanoi from './pages/TowerOfHanoi';
import EightQueens from './pages/EightQueens';
import TrafficSimulation from './pages/TrafficSimulation';

function App() {
    return (
        <Router>
            <div className="min-h-screen bg-gray-900 text-white font-sans antialiased selection:bg-purple-500 selection:text-white">
                <Routes>
                    <Route path="/" element={<GameMenu />} />
                    <Route path="/snake-ladder" element={<SnakeLadder />} />
                    <Route path="/traveling-salesman" element={<TravelingSalesman />} />
                    <Route path="/tower-of-hanoi" element={<TowerOfHanoi />} />
                    <Route path="/eight-queens" element={<EightQueens />} />
                    <Route path="/traffic-simulation" element={<TrafficSimulation />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
