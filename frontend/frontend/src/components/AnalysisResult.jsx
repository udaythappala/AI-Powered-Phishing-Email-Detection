import RiskCard from './RiskCard'
import SecurityIndicators from './SecurityIndicators'
import MitreAttack from './MitreAttack'
import SocRecommendation from './SocRecommendation'

const AnalysisResult = ({ analysis }) => {
  if (!analysis) {
    return (
      <section className="panel result-panel">
        <div className="panel-header">
          <h2>Analysis Result</h2>
        </div>
        <div className="empty-state">
          No analysis has been run yet. Submit an email to begin threat triage.
        </div>
      </section>
    )
  }

  return (
    <section className="panel result-panel">
      <div className="panel-header">
        <div>
          <h2>Analysis Result</h2>
          <span className="panel-subtitle">Case ID #{analysis.id ?? 'N/A'}</span>
        </div>
      </div>

      <div className="result-summary">
        <RiskCard analysis={analysis} />
        <div className="summary-item">
          <div className="summary-label">Prediction</div>
          <div className="summary-value">{analysis.prediction || 'N/A'}</div>
        </div>
        <div className="summary-item">
          <div className="summary-label">Risk Level</div>
          <div className="summary-value">{analysis.riskLevel || 'N/A'}</div>
        </div>
      </div>

      <div className="detail-grid">
        <div className="analysis-card">
          <h3>Reasons</h3>
          <p>{analysis.reasons || 'No reasons provided.'}</p>
        </div>

        <div className="analysis-card">
          <h3>URL Analysis</h3>
          <p>{analysis.urlAnalysis || 'No URL provided'}</p>
        </div>

        <div className="analysis-card">
          <h3>Sender Analysis</h3>
          <p>{analysis.senderAnalysis || 'No sender analysis provided.'}</p>
        </div>

        <MitreAttack value={analysis.mitreAttack} />
      </div>

      <div className="detail-grid" style={{ marginTop: '16px' }}>
        <SecurityIndicators indicators={analysis.suspiciousIndicators} />
        <SocRecommendation recommendation={analysis.recommendedAction} />
      </div>
    </section>
  )
}

export default AnalysisResult
