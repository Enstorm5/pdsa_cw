import React, { useRef, useLayoutEffect } from 'react';
import { Html } from '@react-three/drei';
import * as THREE from 'three';

export default function ConnectionLine({ id, start, end, distance, isHovered, setHovered }) {
    const ref = useRef();

    useLayoutEffect(() => {
        if (ref.current) {
            ref.current.lookAt(new THREE.Vector3(...end));
        }
    }, [end]);

    const midPoint = [
        (start[0] + end[0]) / 2,
        (start[1] + end[1]) / 2,
        (start[2] + end[2]) / 2
    ];

    const handlePointerOver = (e) => {
        e.stopPropagation();
        setHovered(id);
    };

    const handlePointerOut = (e) => {
        // e.stopPropagation(); // Don't stop propagation here to avoid getting stuck if mouse moves fast
        setHovered(null);
    };

    // Increase line thickness/opacity when hovered. 
    // Native Line in ThreeJS has fixed width 1px on most browsers (limitation of WebGL implementation in some browsers).
    // For better visibility, we can color it bright neon when hovered.

    const color = isHovered ? "#00ffff" : "rgba(255, 255, 255, 0.1)";
    const opacity = isHovered ? 1 : 0.2;

    return (
        <group>
            {/* 
          We use a thicker invisible cylinder/tube or just a wider hit area if we want easier hovering on lines, 
          but for now let's try direct line interaction. Since lines are thin, they are hard to hover.
          Let's add a robust invisible mesh (cylinder) for hit testing.
      */}
            <mesh
                position={midPoint}
                quaternion={new THREE.Quaternion().setFromUnitVectors(new THREE.Vector3(0, 1, 0), new THREE.Vector3().subVectors(new THREE.Vector3(...end), new THREE.Vector3(...start)).normalize())}
                onPointerOver={handlePointerOver}
                onPointerOut={handlePointerOut}
                visible={false}
            >
                <cylinderGeometry args={[0.2, 0.2, new THREE.Vector3(...start).distanceTo(new THREE.Vector3(...end)), 8]} />
            </mesh>

            <line>
                <bufferGeometry>
                    <bufferAttribute
                        attach="attributes-position"
                        count={2}
                        array={new Float32Array([...start, ...end])}
                        itemSize={3}
                    />
                </bufferGeometry>
                <lineBasicMaterial color={color} transparent opacity={opacity} linewidth={isHovered ? 3 : 1} />
            </line>

            <Html position={midPoint} zIndexRange={[100, 0]} style={{ pointerEvents: 'none' }}>
                {/* 
            Wrapper div to capture mouse events on the label. 
            Note: Html with pointerEvents='none' makes standard dom events on children tricky if we don't enable them.
            Actually, R3F Html 'pointerEvents' prop controls the container. 
            We want interaction on the label to trigger the line highlight.
       */}
                <div
                    className={`distance-label ${isHovered ? 'active' : ''}`}
                    onMouseEnter={() => setHovered(id)}
                    onMouseLeave={() => setHovered(null)}
                    style={{ pointerEvents: 'auto', cursor: 'pointer' }}
                >
                    {distance} km
                </div>
            </Html>
        </group>
    );
}
