const SocRecommendation = ({ recommendation }) => {
  const message = recommendation && String(recommendation).trim() ? recommendation : 'No recommendation available.'

  return (
    <div className="rec-card">
      <h3>Recommended Action</h3>
      <div className="recommendation-box">
        <p>{message}</p>
      </div>
    </div>
  )
}

export default SocRecommendation
