import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { ArrowLeft } from 'lucide-react';

export default function TowerOfHanoi() {
    return (
        <div className="min-h-screen bg-gray-900 flex flex-col items-center justify-center p-8 text-white">
            <Link to="/" className="absolute top-8 left-8 flex items-center text-gray-400 hover:text-white transition-colors">
                <ArrowLeft className="w-6 h-6 mr-2" />
                Back to Menu
            </Link>
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="text-center">
                <h1 className="text-5xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-purple-400 to-fuchsia-600 mb-6">
                    Tower of Hanoi
                </h1>
                <p className="text-xl text-gray-500">Game implementation coming soon.</p>
            </motion.div>
        </div>
    );
}
