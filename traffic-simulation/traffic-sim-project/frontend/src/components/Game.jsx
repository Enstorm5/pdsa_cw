import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Confetti from 'react-confetti';

const names = ['A','B','C','D','E','F','G','H','T'];

function Game() {
  const [edges,setEdges] = useState([]);
  const [matrix,setMatrix] = useState(null);
  const [reported,setReported] = useState('');
  const [name,setName] = useState('');
  const [result,setResult] = useState(null);
  const [status,setStatus] = useState('');
  const [highlightEdges,setHighlightEdges] = useState([]); // edges in correct max flow path

  useEffect(() => { newGame() }, []);

  const newGame = () => {
    axios.get('http://localhost:9090/api/game/new')
      .then(res => {
        setEdges(res.data.edges);
        setMatrix(res.data.matrix);
        setResult(null);
        setReported('');
        setName('');
        setStatus('');
        setHighlightEdges([]);
      })
      .catch(e => { console.error(e); alert('Backend must be running on port 8080'); });
  }

  

   //  RETRY BUTTON (same graph, reset only inputs)
  const retryGame = () => {
    setReported('');
    setName('');
    setResult(null);
    setStatus('');
    setHighlightEdges([]);
  };


  const renderGraph = () => {
    if(!edges || edges.length === 0) return null;
    const pos = {
      A:[50,50], B:[200,20], C:[200,80], D:[200,140],
      E:[380,10], F:[380,120], G:[560,30], H:[560,120], T:[700,75]
    };
    return (
      <svg width="780" height="220" style={{border:'1px solid #ccc',borderRadius:8,background:'#fafafa'}}>
        {/* edges */}
        {edges.map((e,idx) => {
          const [x1,y1] = pos[e.from];
          const [x2,y2] = pos[e.to];
          const isHighlighted = highlightEdges.some(h => h.from === e.from && h.to === e.to);
          return (
            <g key={idx}>
              <line
                x1={x1+20} y1={y1+20} x2={x2+20} y2={y2+20}
                stroke={isHighlighted?'#28a745':'#555'}
                strokeWidth={isHighlighted?4:2}
                markerEnd="url(#arrow)"
              />
              <text x={(x1+x2)/2+20} y={(y1+y2)/2+15} fontSize="12" fill={isHighlighted?'#28a745':'#333'}>
                {e.cap}
              </text>
            </g>
          )
        })}
    

        {/* nodes */}
        {Object.keys(pos).map(k => {
          const [x,y] = pos[k];
          return (
            <g key={k}>
              <circle cx={x+20} cy={y+20} r="18" fill="#4da6ff" stroke="#0073e6" strokeWidth="2" />
              <text x={x+12} y={y+25} fontSize="14" fill="#fff" fontWeight="bold">{k}</text>
            </g>
          )
        })}

        {/* arrow marker */}
        <defs>
          <marker id="arrow" markerWidth="10" markerHeight="10" refX="5" refY="3" orient="auto">
            <path d="M0,0 L0,6 L9,3 z" fill="#555"/>
          </marker>
        </defs>
      </svg>
    )
  }

  const submitAnswer = () => {
    if(!matrix) return;
    if(!reported){ alert('Please enter your max flow guess!'); return; }

    const body = { matrix, reported: Number(reported), name };
    axios.post('http://localhost:9090/api/game/solve', body)
      .then(res => {
        setResult(res.data);
        setStatus(res.data.correct ? 'win' : 'lose');

        // Highlight only correct path edges
        if(res.data.flowEdges){
          setHighlightEdges(res.data.flowEdges);
        }
      })
      .catch(e => { console.error(e); alert('Error contacting backend: ' + e); });
  }

  return (
    
    <div style={{fontFamily:'Arial, sans-serif',padding:20,maxWidth:800,margin:'0 auto'}}>
        {status === 'win' && (
        <Confetti numberOfPieces={300} recycle={false} />
      )}
      <h2 style={{color:'#0073e6'}}>Traffic Simulation Game</h2>
      <button onClick={newGame} style={{background:'#0073e6',color:'#fff',border:'none',padding:'8px 16px',borderRadius:6,cursor:'pointer'}}>
        🎲 New Game
      </button>

      <button onClick={retryGame} style={{marginLeft:10,background:'#ffc107',color:'#000',border:'none',padding:'8px 16px',borderRadius:6,cursor:'pointer'}}> 
        🔁 Try Again </button>

      <div style={{marginTop:20}}>{renderGraph()}</div>

      <div style={{marginTop:16}}>
        <label>Your guess for max flow A→T: </label>
        <input value={reported} onChange={e => setReported(e.target.value)} type="number"
          style={{padding:6,marginLeft:10,width:80,borderRadius:4,border:'1px solid #ccc'}} />
      </div>
      <div style={{marginTop:10}}>
        <label>Your name: </label>
        <input value={name} onChange={e => setName(e.target.value)}
          style={{padding:6,marginLeft:10,width:200,borderRadius:4,border:'1px solid #ccc'}} />
      </div>
      <div style={{marginTop:12}}>
        <button onClick={submitAnswer} style={{background:'#28a745',color:'#fff',border:'none',padding:'8px 16px',borderRadius:6,cursor:'pointer'}}>
           Submit Answer
        </button>
      </div>

      {result && (
        <div style={{marginTop:20,padding:12,borderRadius:6,background:'#f1f1f1'}}>
          <h3>Game Result</h3>
          <p><strong>Correct Maximum Flow (A→T):</strong> <span style={{color:'#0073e6',fontWeight:'bold'}}>{result.edmondsKarp}</span></p>
          <p><strong>Edmonds-Karp Computed Flow:</strong> {result.edmondsKarp} (time {result.ekTimeMs} ms)</p>
          <p><strong>Dinic Computed Flow:</strong> {result.dinic} (time {result.dinicTimeMs} ms)</p>
          <p><strong>Your answer:</strong> {result.reported} — {result.correct?'Correct 🎉':'Incorrect ❌'}</p>
          {/* Instructions to the user */}
          <div style={{marginTop:10,padding:8,background:'#e9ecef',borderRadius:6}}>
            <strong>Tips:</strong>
            <ul style={{marginTop:5,marginLeft:20}}>
              <li>Check all paths from <strong>A → T</strong>.</li>
              <li>For each path, find the minimum capacity (bottleneck).</li>
              <li>Sum flows of independent paths without exceeding capacities.</li>
              <li>The sum is the <strong>maximum flow</strong>.</li>
            </ul>
          </div>
        </div>
      )}

      {status==='win' && <div style={{marginTop:12,padding:10,borderRadius:6,background:'#d4edda',color:'#155724'}}>
        🎉 Congratulations. You won!!
      </div>}
      {status==='lose' && <div style={{marginTop:12,padding:10,borderRadius:6,background:'#f8d7da',color:'#721c24'}}>
        ❌ Wrong answer. Try again!
      </div>}
    </div>
  )
}

export default Game;
