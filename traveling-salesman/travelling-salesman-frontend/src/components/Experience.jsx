import React, { useMemo } from 'react';
import { OrbitControls, Stars } from '@react-three/drei';
import CityNode from './CityNode';
import ConnectionLine from './ConnectionLine';
import * as THREE from 'three';

export default function Experience({ gameData, selectedCities = [], gamePhase }) {
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



                {connections.map((conn) => {
                    // Check if this connection is part of the selected path
                    let isActive = false;
                    const canHighlight = gamePhase === 'ROUTING' || gamePhase === 'RESULTS';

                    if (canHighlight && selectedCities && selectedCities.length > 1) {
                        // selectedCities is an ordered array e.g. ["A", "B", "C"]
                        // We need to check if conn.start/end matches any segment A->B, B->C

                        // conn.key is `${i}-${j}` where i < j indices in cityLabels
                        const connKey = conn.key;

                        for (let k = 0; k < selectedCities.length - 1; k++) {
                            const city1 = selectedCities[k];
                            const city2 = selectedCities[k + 1];
                            const idx1 = cityLabels.indexOf(city1);
                            const idx2 = cityLabels.indexOf(city2);

                            const minIdx = Math.min(idx1, idx2);
                            const maxIdx = Math.max(idx1, idx2);

                            if (`${minIdx}-${maxIdx}` === connKey) {
                                isActive = true;
                                break;
                            }
                        }
                    }

                    return (
                        <ConnectionLine
                            key={conn.key}
                            id={conn.key}
                            start={conn.start}
                            end={conn.end}
                            distance={conn.distance}
                            isHovered={hoveredConnection === conn.key}
                            setHovered={setHoveredConnection}
                            isActive={isActive}
                        />
                    );
                })}
            </group>

            <gridHelper args={[30, 30, 0x222222, 0x111111]} position={[0, -2, 0]} />
        </>
    );
}
