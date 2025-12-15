import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { Gamepad2, TrendingUp, TowerControl as Tower, Crown, Car } from 'lucide-react';

const games = [
    {
        id: 'snake-ladder',
        title: 'Snake & Ladder',
        description: 'Classic board game of luck and strategy.',
        icon: Gamepad2,
        color: 'from-green-400 to-emerald-600',
        path: '/snake-ladder'
    },
    {
        id: 'traveling-salesman',
        title: 'Traveling Salesman',
        description: 'Optimize the route visiting every city once.',
        icon: TrendingUp,
        color: 'from-blue-400 to-indigo-600',
        path: 'http://localhost:8089/'
    },
    {
        id: 'tower-of-hanoi',
        title: 'Tower of Hanoi',
        description: 'Solve the mathematical puzzle of disks.',
        icon: Tower,
        color: 'from-purple-400 to-fuchsia-600',
        path: '/tower-of-hanoi'
    },
    {
        id: 'eight-queens',
        title: 'Eight Queens Puzzle',
        description: 'Place 8 queens on a chessboard safely.',
        icon: Crown,
        color: 'from-amber-400 to-orange-600',
        path: '/eight-queens'
    },
    {
        id: 'traffic-simulation',
        title: 'Traffic Simulation',
        description: 'Manage flow and prevent gridlocks.',
        icon: Car,
        color: 'from-red-400 to-rose-600',
        path: '/traffic-simulation'
    }
];

const container = {
    hidden: { opacity: 0 },
    show: {
        opacity: 1,
        transition: {
            staggerChildren: 0.1
        }
    }
};

const item = {
    hidden: { opacity: 0, y: 20 },
    show: { opacity: 1, y: 0 }
};

export default function GameMenu() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center p-8 bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-gray-800 via-gray-900 to-black">
            {/* Title removed */}

            <motion.div
                variants={container}
                initial="hidden"
                animate="show"
                className="flex flex-row justify-center items-stretch gap-6 w-full max-w-[95vw] overflow-x-auto px-4 py-8"
            >
                {games.map((game) => (
                    <motion.div key={game.id} variants={item} className="w-80 flex-shrink-0">
                        {game.path.startsWith('http') ? (
                            <a href={game.path} className="block group h-full">
                                <div className={`relative h-full overflow-hidden rounded-3xl bg-gray-800/40 border border-white/10 backdrop-blur-md p-8 transition-all duration-300 hover:scale-[1.02] hover:shadow-[0_0_40px_-10px_rgba(0,0,0,0.5)] hover:border-white/20`}>
                                    <div className={`absolute inset-0 opacity-0 group-hover:opacity-10 transition-opacity duration-500 bg-gradient-to-br ${game.color}`} />

                                    <div className="relative z-10 flex flex-col items-start h-full">
                                        <div className={`p-4 rounded-2xl bg-gradient-to-br ${game.color} mb-6 shadow-lg group-hover:shadow-2xl transition-all duration-300 group-hover:-translate-y-1`}>
                                            <game.icon className="w-8 h-8 text-white" />
                                        </div>

                                        <h3 className="text-2xl font-bold text-white mb-2 group-hover:text-transparent group-hover:bg-clip-text group-hover:bg-gradient-to-r group-hover:from-white group-hover:to-gray-300 transition-colors">
                                            {game.title}
                                        </h3>

                                        <p className="text-gray-400 text-sm leading-relaxed group-hover:text-gray-300 transition-colors">
                                            {game.description}
                                        </p>

                                        <div className="mt-auto pt-8 flex items-center text-sm font-medium text-gray-500 group-hover:text-white transition-colors">
                                            <span>Play Now</span>
                                            <svg className="w-4 h-4 ml-2 transform group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
                                            </svg>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        ) : (
                            <Link to={game.path} className="block group h-full">
                                <div className={`relative h-full overflow-hidden rounded-3xl bg-gray-800/40 border border-white/10 backdrop-blur-md p-8 transition-all duration-300 hover:scale-[1.02] hover:shadow-[0_0_40px_-10px_rgba(0,0,0,0.5)] hover:border-white/20`}>
                                    <div className={`absolute inset-0 opacity-0 group-hover:opacity-10 transition-opacity duration-500 bg-gradient-to-br ${game.color}`} />

                                    <div className="relative z-10 flex flex-col items-start h-full">
                                        <div className={`p-4 rounded-2xl bg-gradient-to-br ${game.color} mb-6 shadow-lg group-hover:shadow-2xl transition-all duration-300 group-hover:-translate-y-1`}>
                                            <game.icon className="w-8 h-8 text-white" />
                                        </div>

                                        <h3 className="text-2xl font-bold text-white mb-2 group-hover:text-transparent group-hover:bg-clip-text group-hover:bg-gradient-to-r group-hover:from-white group-hover:to-gray-300 transition-colors">
                                            {game.title}
                                        </h3>

                                        <p className="text-gray-400 text-sm leading-relaxed group-hover:text-gray-300 transition-colors">
                                            {game.description}
                                        </p>

                                        <div className="mt-auto pt-8 flex items-center text-sm font-medium text-gray-500 group-hover:text-white transition-colors">
                                            <span>Play Now</span>
                                            <svg className="w-4 h-4 ml-2 transform group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
                                            </svg>
                                        </div>
                                    </div>
                                </div>
                            </Link>
                        )}
                    </motion.div>
                ))}
            </motion.div>
        </div>
    );
}
