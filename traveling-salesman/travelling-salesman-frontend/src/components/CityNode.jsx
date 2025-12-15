import React, { useRef, useState } from 'react';
import { Html } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function CityNode({ position, name, color = "#4f46e5", isSelected, selectionOrder }) {
    const meshRef = useRef();
    const [hovered, setHover] = useState(false);

    useFrame((state) => {
        const t = state.clock.getElapsedTime();
        if (meshRef.current) {
            meshRef.current.position.y = position[1] + Math.sin(t * 2 + position[0]) * 0.1;
        }
    });

    // Determine appearance based on selection state
    const activeColor = isSelected ? "#00ffff" : (hovered ? "#ec4899" : color);
    const activeEmissive = isSelected ? "#00ffff" : (hovered ? "#ec4899" : color);
    const activeIntensity = isSelected ? 2 : 0.5;
    const activeScale = isSelected || hovered ? 1.2 : 1;

    return (
        <group position={position}>
            <mesh
                ref={meshRef}
                onPointerOver={() => setHover(true)}
                onPointerOut={() => setHover(false)}
                scale={activeScale}
            >
                <sphereGeometry args={[0.5, 32, 32]} />
                <meshStandardMaterial
                    color={activeColor}
                    emissive={activeEmissive}
                    emissiveIntensity={activeIntensity}
                    roughness={0.2}
                    metalness={0.8}
                />
            </mesh>
            <Html distanceFactor={10} zIndexRange={[100, 0]}>
                <div className={`city-label ${isSelected ? 'selected' : ''}`}>
                    {name}
                </div>
                {isSelected && (
                    <div
                        style={{
                            position: 'absolute',
                            top: '-60px',
                            left: '50%',
                            transform: 'translateX(-50%)',
                            background: '#0ff',
                            color: '#000',
                            width: '30px',
                            height: '30px',
                            borderRadius: '50%',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            fontWeight: 'bold',
                            boxShadow: '0 0 10px #0ff',
                            pointerEvents: 'none'
                        }}
                    >
                        {selectionOrder}
                    </div>
                )}
            </Html>
        </group>
    );
}
