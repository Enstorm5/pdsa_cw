import React, { useRef, useLayoutEffect } from 'react';
import { Html, Line } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
import * as THREE from 'three';

export default function ConnectionLine({ id, start, end, distance, isHovered, setHovered, isActive }) {
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

    const color = (isHovered || isActive) ? "#00ffff" : "rgba(255, 255, 255, 0.1)";
    const opacity = (isHovered || isActive) ? 1 : 0.2;

    const lineRef = useRef();

    useFrame((state, delta) => {
        if (lineRef.current) {
            const targetWidth = (isHovered || isActive) ? 5 : 1;
            const targetOpacity = (isHovered || isActive) ? 1 : 0.2;

            // Color logic: White for generic hover, Cyan for active path
            const targetColorHex = isActive ? "#00ffff" : (isHovered ? "#ffffff" : "#ffffff");
            const targetColor = new THREE.Color(targetColorHex);

            // Smoothly interpolate line width
            lineRef.current.material.linewidth = THREE.MathUtils.lerp(
                lineRef.current.material.linewidth,
                targetWidth,
                delta * 10
            );

            // Smoothly interpolate opacity
            lineRef.current.material.opacity = THREE.MathUtils.lerp(
                lineRef.current.material.opacity,
                targetOpacity,
                delta * 10
            );

            // Smoothly interpolate color
            lineRef.current.material.color.lerp(targetColor, delta * 10);
        }
    });

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

            {/* 
                Using Drei's Line component which supports thickness (fat lines).
                "White neon" style: Bright white, toneMapped=false for maximum brightness (if bloom is added later),
                and increased thickness.
            */}
            <Line
                ref={lineRef}
                points={[start, end]}       // Array of points
                color="#ffffff"
                transparent
                // Initial values
                opacity={0.2}
                lineWidth={1}
            />

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
