const RiskCard = ({ analysis }) => {
  if (!analysis) return null

  const prediction = analysis.prediction || 'UNKNOWN'
  const riskLevel = analysis.riskLevel || 'LOW'
  const score = analysis.phishingScore ?? 0

  const predictionClass = prediction.toLowerCase()
  const riskClass = riskLevel.toLowerCase()

  return (
    <div className="summary-item">
      <div className="summary-label">Prediction</div>
      <div className="summary-value">
        <span className={`status-badge ${predictionClass}`}>{prediction}</span>
      </div>

      <div className="summary-label" style={{ marginTop: '14px' }}>
        Phishing Score
      </div>
      <div className="summary-value">
        <span className="score-badge">{Number(score).toFixed(2)}%</span>
      </div>

      <div className="summary-label" style={{ marginTop: '14px' }}>
        Risk Level
      </div>
      <div className="summary-value">
        <span className={`status-badge ${riskClass}`}>{riskLevel}</span>
      </div>
    </div>
  )
}

export default RiskCard
