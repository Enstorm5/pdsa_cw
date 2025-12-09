import React, { useRef, useState } from 'react';
import { Html } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function CityNode({ position, name, color = "#4f46e5" }) {
    const meshRef = useRef();
    const [hovered, setHover] = useState(false);

    useFrame((state) => {
        const t = state.clock.getElapsedTime();
        if (meshRef.current) {
            meshRef.current.position.y = position[1] + Math.sin(t * 2 + position[0]) * 0.1;
        }
    });

    return (
        <group position={position}>
            <mesh
                ref={meshRef}
                onPointerOver={() => setHover(true)}
                onPointerOut={() => setHover(false)}
                scale={hovered ? 1.2 : 1}
            >
                <sphereGeometry args={[0.5, 32, 32]} />
                <meshStandardMaterial
                    color={hovered ? "#ec4899" : color}
                    emissive={hovered ? "#ec4899" : color}
                    emissiveIntensity={0.5}
                    roughness={0.2}
                    metalness={0.8}
                />
            </mesh>
            <Html distanceFactor={10}>
                <div className="city-label">
                    {name}
                </div>
            </Html>
        </group>
    );
}
