const normalizeIndicators = (value) => {
  if (!value) return []

  if (Array.isArray(value)) {
    return value
      .map((item) => String(item).trim())
      .filter(Boolean)
  }

  return String(value)
    .split(',')
    .map((item) => item.trim())
    .filter(Boolean)
}

const SecurityIndicators = ({ indicators }) => {
  const formattedIndicators = normalizeIndicators(indicators)

  return (
    <div className="analysis-card">
      <h3>Suspicious Indicators</h3>

      {formattedIndicators.length > 0 ? (
        <div className="tag-list">
          {formattedIndicators.map((indicator) => (
            <span key={indicator} className="tag-badge">
              {indicator}
            </span>
          ))}
        </div>
      ) : (
        <p>No suspicious indicators detected.</p>
      )}
    </div>
  )
}

export default SecurityIndicators
