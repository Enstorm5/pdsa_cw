import React, { useMemo } from 'react';
import { OrbitControls, Stars } from '@react-three/drei';
import CityNode from './CityNode';
import ConnectionLine from './ConnectionLine';
import * as THREE from 'three';

export default function Experience({ gameData, selectedCities = [] }) {
    const { cityLabels, distanceMatrix } = gameData;

    // Calculate positions in a circle
    const cityPositions = useMemo(() => {
        const positions = [];
        const radius = 8;
        const count = cityLabels.length;

        for (let i = 0; i < count; i++) {
            const angle = (i / count) * Math.PI * 2;
            const x = Math.cos(angle) * radius;
            const z = Math.sin(angle) * radius;
            positions.push([x, 0, z]);
        }
        return positions;
    }, [cityLabels]);

    const connections = useMemo(() => {
        const lines = [];
        for (let i = 0; i < distanceMatrix.length; i++) {
            for (let j = i + 1; j < distanceMatrix[i].length; j++) {
                // Only add if there is a distance (assuming 0 means no direct connection, though here 0 is diagonal)
                const dist = distanceMatrix[i][j];
                if (dist > 0) {
                    lines.push({
                        start: cityPositions[i],
                        end: cityPositions[j],
                        distance: dist,
                        key: `${i}-${j}`
                    });
                }
            }
        }
        return lines;
    }, [distanceMatrix, cityPositions]);

    const [hoveredConnection, setHoveredConnection] = React.useState(null);

    return (
        <>
            <color attach="background" args={['#050511']} />
            <fog attach="fog" args={['#050511', 10, 40]} />

            <ambientLight intensity={0.5} />
            <pointLight position={[10, 10, 10]} intensity={1} />

            <OrbitControls makeDefault autoRotate={false} />

            <group>
                {cityLabels.map((name, index) => {
                    const selectionIndex = selectedCities ? selectedCities.indexOf(name) : -1;
                    const isSelected = selectionIndex !== -1;

                    return (
                        <CityNode
                            key={name}
                            position={cityPositions[index]}
                            name={name}
                            isSelected={isSelected}
                            selectionOrder={selectionIndex + 1}
                        />
                    );
                })}

                {connections.map((conn) => (
                    <ConnectionLine
                        key={conn.key}
                        id={conn.key}
                        start={conn.start}
                        end={conn.end}
                        distance={conn.distance}
                        isHovered={hoveredConnection === conn.key}
                        setHovered={setHoveredConnection}
                    />
                ))}
            </group>

            <gridHelper args={[30, 30, 0x222222, 0x111111]} position={[0, -2, 0]} />
        </>
    );
}
