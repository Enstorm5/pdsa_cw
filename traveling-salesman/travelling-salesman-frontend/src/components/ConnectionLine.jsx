import React, { useRef, useLayoutEffect } from 'react';
import { Html } from '@react-three/drei';
import * as THREE from 'three';

export default function ConnectionLine({ start, end, distance }) {
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

    const length = new THREE.Vector3(...start).distanceTo(new THREE.Vector3(...end));

    return (
        <group>
            <line>
                <bufferGeometry>
                    <bufferAttribute
                        attach="attributes-position"
                        count={2}
                        array={new Float32Array([...start, ...end])}
                        itemSize={3}
                    />
                </bufferGeometry>
                <lineBasicMaterial color="rgba(255, 255, 255, 0.1)" transparent opacity={0.2} />
            </line>
            {/* Only show label if close or handling sparse graphs, but for K10 maybe too much text. 
           User asked for "add km after values". I will add it but maybe small. 
       */}
            <Html position={midPoint} zIndexRange={[100, 0]}>
                <div className="distance-label">
                    {distance} km
                </div>
            </Html>
        </group>
    );
}
