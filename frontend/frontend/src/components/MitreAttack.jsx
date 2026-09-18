const MitreAttack = ({ value }) => {
  const text = value && String(value).trim() ? value : 'No MITRE ATT&CK mapping provided.'

  return (
    <div className="analysis-card">
      <h3>MITRE ATT&CK</h3>
      <p>{text}</p>
    </div>
  )
}

export default MitreAttack
