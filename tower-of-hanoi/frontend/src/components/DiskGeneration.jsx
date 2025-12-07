import { ArrowRight, Layers } from 'lucide-react';
import './DiskGeneration.css';

function DiskGeneration({ numberOfDisks, onContinue }) {
  return (
    <div className="disk-generation">
      <div className="diskgen-card">
        <div className="diskgen-icon">
          <Layers size={80} />
        </div>
        
        <h2 className="diskgen-title">Game Round Generated!</h2>
        
        <div className="disk-announcement">
          <p className="announcement-text">This round will use</p>
          <div className="disk-count-badge">{numberOfDisks}</div>
          <p className="announcement-text">disks</p>
        </div>

        <div className="disk-preview">
          {[...Array(Math.min(numberOfDisks, 7))].map((_, i) => {
            const diskNum = numberOfDisks - i;
            const width = 40 + (diskNum * 12);
            return (
              <div
                key={i}
                className="preview-disk"
                style={{
                  width: `${width}px`,
                  backgroundColor: `hsl(${(diskNum * 35) % 360}, 65%, 70%)`,
                  animationDelay: `${i * 0.1}s`
                }}
              />
            );
          })}
        </div>

        <button className="continue-button" onClick={onContinue}>
          Continue
          <ArrowRight size={20} />
        </button>
      </div>
    </div>
  );
}

export default DiskGeneration;