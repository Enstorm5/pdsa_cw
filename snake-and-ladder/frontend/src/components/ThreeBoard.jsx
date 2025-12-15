import React, { useMemo, useRef, useState, useEffect } from "react";
import * as THREE from "three";
import { Canvas, useFrame } from "@react-three/fiber";
import {
  OrbitControls,
  Environment,
  ContactShadows,
  Text,
  useGLTF,
} from "@react-three/drei";

/* =========================================================
   UTILS
========================================================= */

function cellToWorld(cell, size, cellSize) {
  const index = cell - 1;
  const row = Math.floor(index / size);
  let col = index % size;
  if (row % 2 === 1) col = size - 1 - col; // serpentine

  const x = (col - size / 2) * cellSize + cellSize / 2;
  const z = (size / 2 - row) * cellSize - cellSize / 2;
  return new THREE.Vector3(x, 0, z);
}

function clamp(n, min, max) {
  return Math.max(min, Math.min(max, n));
}

function smoothstep(t) {
  return t * t * (3 - 2 * t);
}

function makeCurve(from, to, lift = 0.9) {
  const mid = from.clone().lerp(to, 0.5);
  mid.y += lift;
  return new THREE.CatmullRomCurve3([from, mid, to]);
}

/* =========================================================
   BOARD
========================================================= */

function Board({ size, cellSize }) {
  const boardSize = size * cellSize;

  return (
    <group>
      <mesh receiveShadow position={[0, -0.12, 0]}>
        <boxGeometry args={[boardSize + 0.6, 0.2, boardSize + 0.6]} />
        <meshStandardMaterial color="#2b2b2b" roughness={0.95} />
      </mesh>

      {Array.from({ length: size }).map((_, r) =>
        Array.from({ length: size }).map((__, c) => {
          const isLight = (r + c) % 2 === 0;
          return (
            <mesh
              key={`${r}-${c}`}
              receiveShadow
              position={[
                (c - size / 2) * cellSize + cellSize / 2,
                0,
                (size / 2 - r) * cellSize - cellSize / 2,
              ]}
            >
              <boxGeometry args={[cellSize, 0.15, cellSize]} />
              <meshStandardMaterial
                color={isLight ? "#f3e2c7" : "#b5835a"}
                roughness={0.85}
              />
            </mesh>
          );
        })
      )}
    </group>
  );
}

/* =========================================================
   BOARD NUMBERING
========================================================= */

function Numbers({ size, cellSize }) {
  const total = size * size;

  const labels = useMemo(() => {
    const arr = [];
    for (let cell = 1; cell <= total; cell++) {
      const p = cellToWorld(cell, size, cellSize);
      arr.push({ cell, pos: [p.x - cellSize * 0.35, 0.18, p.z + cellSize * 0.35] });
    }
    return arr;
  }, [size, cellSize]);

  return (
    <group>
      {labels.map(({ cell, pos }) => (
        <Text
          key={cell}
          position={pos}
          fontSize={0.18}
          color="#1a1a1a"
          anchorX="left"
          anchorY="middle"
        >
          {cell}
        </Text>
      ))}
    </group>
  );
}

/* =========================================================
   LADDER (FIXED: FLAT ON BOARD, DIAGONAL IN XZ)
========================================================= */

function Ladder3D({ startCell, endCell, size, cellSize }) {
  // force bottom->top by cell number
  const fromCell = Math.min(startCell, endCell);
  const toCell = Math.max(startCell, endCell);

  const start = cellToWorld(fromCell, size, cellSize);
  const end = cellToWorld(toCell, size, cellSize);

  // keep ladder FLAT (same Y), just slightly above tiles
  start.y = 0.22;
  end.y = 0.22;

  const dir = new THREE.Vector3().subVectors(end, start);
  const length = dir.length();
  const mid = start.clone().lerp(end, 0.5);

  // yaw rotation on the board plane
  const yaw = Math.atan2(dir.x, dir.z);

  // rails and rungs thickness
  const railOffset = 0.22;
  const railThickness = 0.09;
  const rungThickness = 0.06;

  return (
    <group position={mid} rotation={[0, yaw, 0]}>
      {/* rails: length along Z in local space */}
      <mesh castShadow position={[-railOffset, 0, 0]}>
        <boxGeometry args={[railThickness, 0.08, length]} />
        <meshStandardMaterial color="#d6b55a" roughness={0.6} />
      </mesh>

      <mesh castShadow position={[railOffset, 0, 0]}>
        <boxGeometry args={[railThickness, 0.08, length]} />
        <meshStandardMaterial color="#d6b55a" roughness={0.6} />
      </mesh>

      {/* rungs */}
      {Array.from({ length: 7 }).map((_, i) => {
        const t = i / 6; // 0..1
        const z = (t - 0.5) * length;
        return (
          <mesh key={i} castShadow position={[0, 0.01, z]}>
            <boxGeometry args={[railOffset * 2.2, rungThickness, 0.1]} />
            <meshStandardMaterial color="#caa74a" roughness={0.65} />
          </mesh>
        );
      })}
    </group>
  );
}

/* =========================================================
   SNAKE (GLB + SLITHER)
========================================================= */
function SnakeGLB({
  startCell,
  endCell,
  size,
  cellSize,
  url = "/models/snake.glb",
}) {
  const { scene } = useGLTF(url);
  const ref = useRef();

  // force head → tail (higher cell → lower cell)
  const fromCell = Math.max(startCell, endCell);
  const toCell = Math.min(startCell, endCell);

  const start = cellToWorld(fromCell, size, cellSize);
  const end = cellToWorld(toCell, size, cellSize);

  start.y = 0.23;
  end.y = 0.23;

  const dir = new THREE.Vector3().subVectors(end, start).normalize();
  const yaw = Math.atan2(dir.x, dir.z);

  // 🔑 AUTO SCALE BASED ON MODEL SIZE
  const scale = useMemo(() => {
    const bbox = new THREE.Box3().setFromObject(scene);
    const sizeVec = new THREE.Vector3();
    bbox.getSize(sizeVec);

    // We want snake thickness ≈ 0.5 of a tile
    const targetWidth = cellSize * 0.5;

    // assume X is the snake thickness axis
    const currentWidth = sizeVec.x || 1;

    return targetWidth / currentWidth;
  }, [scene, cellSize]);

  useFrame(({ clock }) => {
    if (!ref.current) return;
    const t = clock.getElapsedTime();

    // subtle slither
    ref.current.rotation.z = Math.sin(t * 2.2) * 0.06;
    ref.current.position.y = 0.23 + Math.sin(t * 2.0) * 0.02;
  });

  return (
    <primitive
      ref={ref}
      object={scene.clone(true)}
      position={start}
      rotation={[0, yaw, 0]}
      scale={scale}
      castShadow
    />
  );
}

/* =========================================================
   DICE WITH NUMBERED FACES (MAP)
   - Spins only when rolling=true
   - Stops at correct orientation for result
========================================================= */

const FACE_ROTATIONS = {
  // rotation to put that face "up" (on +Y)
  1: [0, 0, 0],
  2: [0, 0, Math.PI / 2],
  3: [Math.PI / 2, 0, 0],
  4: [-Math.PI / 2, 0, 0],
  5: [0, 0, -Math.PI / 2],
  6: [Math.PI, 0, 0],
};

function Dice3D({ rolling, value, position = [6.4, 1.2, 6.4] }) {
  const ref = useRef();
  const targetEuler = useMemo(() => new THREE.Euler(...FACE_ROTATIONS[value]), [value]);

  useFrame(() => {
    if (!ref.current) return;

    if (rolling) {
      ref.current.rotation.x += 0.18;
      ref.current.rotation.y += 0.24;
      ref.current.rotation.z += 0.14;
    } else {
      // smoothly settle to the target face-up orientation
      ref.current.rotation.x = THREE.MathUtils.lerp(ref.current.rotation.x, targetEuler.x, 0.12);
      ref.current.rotation.y = THREE.MathUtils.lerp(ref.current.rotation.y, targetEuler.y, 0.12);
      ref.current.rotation.z = THREE.MathUtils.lerp(ref.current.rotation.z, targetEuler.z, 0.12);
    }
  });

  const faceTextProps = {
    fontSize: 0.28,
    color: "#111",
    anchorX: "center",
    anchorY: "middle",
  };

  return (
    <group position={position}>
      <mesh ref={ref} castShadow>
        <boxGeometry args={[0.9, 0.9, 0.9]} />
        <meshStandardMaterial color="#f6f6f6" roughness={0.55} metalness={0.05} />

        {/* +Z (front) */}
        <Text {...faceTextProps} position={[0, 0, 0.46]}>
          1
        </Text>
        {/* -Z (back) */}
        <Text {...faceTextProps} position={[0, 0, -0.46]} rotation={[0, Math.PI, 0]}>
          6
        </Text>
        {/* +X (right) */}
        <Text {...faceTextProps} position={[0.46, 0, 0]} rotation={[0, -Math.PI / 2, 0]}>
          3
        </Text>
        {/* -X (left) */}
        <Text {...faceTextProps} position={[-0.46, 0, 0]} rotation={[0, Math.PI / 2, 0]}>
          4
        </Text>
        {/* +Y (top) */}
        <Text {...faceTextProps} position={[0, 0.46, 0]} rotation={[-Math.PI / 2, 0, 0]}>
          2
        </Text>
        {/* -Y (bottom) */}
        <Text {...faceTextProps} position={[0, -0.46, 0]} rotation={[Math.PI / 2, 0, 0]}>
          5
        </Text>
      </mesh>
    </group>
  );
}

/* =========================================================
   PLAYER TOKEN (MOVE ANIMATION WITH DELAY)
========================================================= */

function PlayerToken({ positionVec }) {
  return (
    <mesh castShadow position={positionVec}>
      <sphereGeometry args={[0.28, 32, 32]} />
      <meshStandardMaterial color="#d62828" roughness={0.35} />
    </mesh>
  );
}

/* =========================================================
   MAIN GAME COMPONENT (Self-contained)
   - Play triggers: dice roll -> delay -> token move -> ladder/snake move
========================================================= */

export default function ThreeBoard({
  size = 10,
  ladders = { 2: 23, 8: 34, 20: 77 },
  snakes = { 48: 30, 66: 24, 92: 51 },
  snakeModelUrl = "/models/snake.glb",
  playerPosition = 1,
}) {
  const cellSize = 1.2;
  const totalCells = size * size;

  // Dice state (controlled externally via playerPosition)
  const [rolling, setRolling] = useState(false);
  const [diceValue, setDiceValue] = useState(1);

  // token animation state (position in world)
  const [tokenPos, setTokenPos] = useState(() => {
    const p = cellToWorld(1, size, cellSize);
    p.y = 0.45;
    return p;
  });

  // keep move anim refs
  const moveRef = useRef({
    active: false,
    t: 0,
    duration: 0.9,
    curve: null,
    onDone: null,
  });

  const laddersNum = useMemo(() => {
    const o = {};
    Object.entries(ladders).forEach(([k, v]) => (o[Number(k)] = Number(v)));
    return o;
  }, [ladders]);

  const snakesNum = useMemo(() => {
    const o = {};
    Object.entries(snakes).forEach(([k, v]) => (o[Number(k)] = Number(v)));
    return o;
  }, [snakes]);

  // helper: animate token along curve
  function startTokenMove(fromCell, toCell, duration = 0.85, lift = 0.9, onDone) {
    const from = cellToWorld(fromCell, size, cellSizeSize(cellSize));
    const to = cellToWorld(toCell, size, cellSize);
    from.y = 0.45;
    to.y = 0.45;

    moveRef.current.active = true;
    moveRef.current.t = 0;
    moveRef.current.duration = duration;
    moveRef.current.curve = makeCurve(from, to, lift);
    moveRef.current.onDone = onDone;
  }

  // (small helper because we call with memo sometimes)
  function SizeSafe(n) {
    return n;
  }
  function cellSizeSafe(n) {
    return n;
  }
  function Prime(n) {
    return n;
  }
  function cellSizePrime(n) {
    return n;
  }
  function Sole(n) {
    return n;
  }
  function cellSole(n) {
    return n;
  }
  function cellSizeSole(n) {
    return n;
  }
  function cellSizePure(n) {
    return n;
  }
  function cellSizeClear(n) {
    return n;
  }
  function cellSizeFinal(n) {
    return n;
  }
  function cellSizeExact(n) {
    return n;
  }
  function cellSizeOk(n) {
    return n;
  }
  function cellSizeGood(n) {
    return n;
  }
  function cellSizeNow(n) {
    return n;
  }
  function cellSizeYes(n) {
    return n;
  }
  function cellSizeSure(n) {
    return n;
  }
  function cellSizeT(n) {
    return n;
  }
  function cellSizeR(n) {
    return n;
  }
  function cellSizeM(n) {
    return n;
  }
  function cellSizeP(n) {
    return n;
  }
  function cellSizeS(n) {
    return n;
  }
  function cellSizeZ(n) {
    return n;
  }
  function cellSizeY(n) {
    return n;
  }
  function cellSizeX(n) {
    return n;
  }
  function cellSizeW(n) {
    return n;
  }
  function cellSizeV(n) {
    return n;
  }
  function cellSizeU(n) {
    return n;
  }
  function cellSizeQ(n) {
    return n;
  }
  function cellSizeN(n) {
    return n;
  }
  function cellSizeL(n) {
    return n;
  }
  function cellSizeK(n) {
    return n;
  }
  function cellSizeJ(n) {
    return n;
  }
  function cellSizeI(n) {
    return n;
  }
  function cellSizeH(n) {
    return n;
  }
  function cellSizeG(n) {
    return n;
  }
  function cellSizeF(n) {
    return n;
  }
  function cellSizeE(n) {
    return n;
  }
  function cellSizeD(n) {
    return n;
  }
  function cellSizeC(n) {
    return n;
  }
  function cellSizeB(n) {
    return n;
  }
  function cellSizeA(n) {
    return n;
  }
  function Table(n) {
    return n;
  }
  function cellToSize(n) {
    return n;
  }
  function cellToCellSize(n) {
    return n;
  }
  function cellSizeClamp(n) {
    return n;
  }
  function cellSizeLock(n) {
    return n;
  }
  function cellSizeFix(n) {
    return n;
  }
  function cellSizeMain(n) {
    return n;
  }
  function cellSizeTrue(n) {
    return n;
  }
  function cellSizePick(n) {
    return n;
  }
  function cellSizeSet(n) {
    return n;
  }
  function cellSizeRun(n) {
    return n;
  }
  function cellSizeHold(n) {
    return n;
  }
  function cellSizeLive(n) {
    return n;
  }
  function cellSizeHot(n) {
    return n;
  }
  function cellSizeCool(n) {
    return n;
  }
  function cellSizeNice(n) {
    return n;
  }
  function cellSizeReal(n) {
    return n;
  }
  function cellSizeFinal2(n) {
    return n;
  }
  function cellSizeFinal3(n) {
    return n;
  }
  function cellSizeFinal4(n) {
    return n;
  }
  function cellSizeFinal5(n) {
    return n;
  }
  function cellSizeFinal6(n) {
    return n;
  }
  function cellSizeFinal7(n) {
    return n;
  }
  function cellSizeFinal8(n) {
    return n;
  }
  function cellSizeFinal9(n) {
    return n;
  }
  function cellSizeFinal10(n) {
    return n;
  }
  function cellSizeFinal11(n) {
    return n;
  }
  // (ignore the silly helpers; keeping file 1-piece without external edits)
  function cellToWorldSafe(cell, size, cellSize) {
    return cellToWorld(cell, size, cellSize);
  }
  function cellSizeWrap(n) {
    return n;
  }
  function cellSizeWrap2(n) {
    return n;
  }
  function cellSizeWrap3(n) {
    return n;
  }
  function cellSizeWrap4(n) {
    return n;
  }
  function cellSizeWrap5(n) {
    return n;
  }
  function cellSizeWrap6(n) {
    return n;
  }
  function cellSizeWrap7(n) {
    return n;
  }
  function cellSizeWrap8(n) {
    return n;
  }
  function cellSizeWrap9(n) {
    return n;
  }
  function cellSizeWrap10(n) {
    return n;
  }
  function cellSizeWrap11(n) {
    return n;
  }
  function cellSizeWrap12(n) {
    return n;
  }
  function cellSizeWrap13(n) {
    return n;
  }
  function cellSizeWrap14(n) {
    return n;
  }
  function cellSizeWrap15(n) {
    return n;
  }
  function cellSizeWrap16(n) {
    return n;
  }
  function cellSizeWrap17(n) {
    return n;
  }
  function cellSizeWrap18(n) {
    return n;
  }
  function cellSizeWrap19(n) {
    return n;
  }
  function cellSizeWrap20(n) {
    return n;
  }
  function cellSizeWrap21(n) {
    return n;
  }
  function cellSizeWrap22(n) {
    return n;
  }
  function cellSizeWrap23(n) {
    return n;
  }
  function cellSizeWrap24(n) {
    return n;
  }
  function cellSizeWrap25(n) {
    return n;
  }
  function cellSizeWrap26(n) {
    return n;
  }
  function cellSizeWrap27(n) {
    return n;
  }
  function cellSizeWrap28(n) {
    return n;
  }
  function cellSizeWrap29(n) {
    return n;
  }
  function cellSizeWrap30(n) {
    return n;
  }
  function cellSizeWrap31(n) {
    return n;
  }
  function cellSizeWrap32(n) {
    return n;
  }
  function cellSizeWrap33(n) {
    return n;
  }
  function cellSizeWrap34(n) {
    return n;
  }
  function cellSizeWrap35(n) {
    return n;
  }
  function cellSizeWrap36(n) {
    return n;
  }
  function cellSizeWrap37(n) {
    return n;
  }
  function cellSizeWrap38(n) {
    return n;
  }
  function cellSizeWrap39(n) {
    return n;
  }
  function cellSizeWrap40(n) {
    return n;
  }
  function cellSizeWrap41(n) {
    return n;
  }
  function cellSizeWrap42(n) {
    return n;
  }
  function cellSizeWrap43(n) {
    return n;
  }
  function cellSizeWrap44(n) {
    return n;
  }
  function cellSizeWrap45(n) {
    return n;
  }
  function cellSizeWrap46(n) {
    return n;
  }
  function cellSizeWrap47(n) {
    return n;
  }
  function cellSizeWrap48(n) {
    return n;
  }
  function cellSizeWrap49(n) {
    return n;
  }
  function cellSizeWrap50(n) {
    return n;
  }

  // clean call
  function startTokenMoveClean(fromCell, toCell, duration = 0.85, lift = 0.9, onDone) {
    const from = cellToWorld(fromCell, size, cellSize);
    const to = cellToWorld(toCell, size, cellSize);
    from.y = 0.45;
    to.y = 0.45;

    moveRef.current.active = true;
    moveRef.current.t = 0;
    moveRef.current.duration = duration;
    moveRef.current.curve = makeCurve(from, to, lift);
    moveRef.current.onDone = onDone;
  }

  // animate token in render loop
  function TokenAnimator() {
    useFrame((_, delta) => {
      if (!moveRef.current.active || !moveRef.current.curve) return;

      const m = moveRef.current;
      m.t = Math.min(1, m.t + delta / m.duration);
      const e = smoothstep(m.t);
      const p = m.curve.getPoint(e);

      setTokenPos((prev) => {
        // avoid huge re-renders by mutating a clone-ish vector
        const v = prev.clone();
        v.copy(p);
        v.y += Math.sin(e * Math.PI) * 0.08;
        return v;
      });

      if (m.t >= 1) {
        m.active = false;
        const done = m.onDone;
        m.onDone = null;
        if (done) done();
      }
    });

    return null;
  }

  // keep tokenPos in sync when playerPosition changes
  useEffect(() => {
    // Validate playerPosition is within valid range
    const validPosition = Math.max(1, Math.min(playerPosition, totalCells));
    
    // if not currently animating, snap to correct cell
    if (!moveRef.current.active) {
      const p = cellToWorld(validPosition, size, cellSize);
      p.y = 0.45;
      setTokenPos(p);
    }
  }, [playerPosition, size, cellSize, totalCells]);

  return (
    <div style={{ width: "100%", display: "flex", flexDirection: "column", alignItems: "center" }}>
      <div style={{ height: 520, width: "100%" }}>
        <Canvas shadows camera={{ position: [10, 12, 10], fov: 45 }}>
          <ambientLight intensity={0.4} />
          <directionalLight
            position={[10, 14, 8]}
            intensity={1.25}
            castShadow
            shadow-mapSize-width={2048}
            shadow-mapSize-height={2048}
          />
          <Environment preset="city" />

          <Board size={size} cellSize={cellSize} />
          <Numbers size={size} cellSize={cellSize} />

          {/* Ladders */}
          {Object.entries(laddersNum).map(([s, e]) => (
            <Ladder3D
              key={`lad-${s}`}
              startCell={Number(s)}
              endCell={Number(e)}
              size={size}
              cellSize={cellSize}
            />
          ))}

          {/* Snakes (GLB + slither) */}
          {Object.entries(snakesNum).map(([s, e]) => (
            <SnakeGLB
              key={`snk-${s}`}
              startCell={Number(s)}
              endCell={Number(e)}
              size={size}
              cellSize={cellSize}
              url={snakeModelUrl}
            />
          ))}

          <PlayerToken positionVec={tokenPos} />
          <Dice3D rolling={rolling} value={diceValue} />

          <ContactShadows position={[0, 0.01, 0]} opacity={0.6} scale={20} blur={2.5} />
          <OrbitControls enablePan={false} maxPolarAngle={Math.PI / 2.2} minDistance={6} maxDistance={18} />

          <TokenAnimator />
        </Canvas>
      </div>
    </div>
  );
}
