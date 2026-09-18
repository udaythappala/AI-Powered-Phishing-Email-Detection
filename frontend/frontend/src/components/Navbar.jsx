const Navbar = () => {
  return (
    <header className="topbar panel">
      <div className="brand">
        <div className="brand-icon" aria-label="Security shield icon">
          🛡️
        </div>
        <div>
          <h1>PhishGuard SOC</h1>
          <p>AI-Powered Phishing Email Detection</p>
        </div>
      </div>

      <div className="status-pill" aria-live="polite">
        <span className="status-dot" aria-hidden="true"></span>
        Monitoring Active
      </div>
    </header>
  )
}

export default Navbar
