const EmailForm = ({ formData, setFormData, onSubmit, loading, error, successMessage }) => {
  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }))
  }

  return (
    <section className="panel form-panel">
      <div className="panel-header">
        <div>
          <h2>Email Intake</h2>
          <span className="panel-subtitle">Threat triage and analysis</span>
        </div>
      </div>

      <form
        className="form-grid"
        onSubmit={(event) => {
          event.preventDefault()
          onSubmit()
        }}
      >
        <div className="form-field">
          <label htmlFor="sender">Sender Email</label>
          <input
            id="sender"
            name="sender"
            type="email"
            value={formData.sender}
            onChange={handleChange}
            placeholder="security@fakebank.com"
            autoComplete="email"
          />
        </div>

        <div className="form-field">
          <label htmlFor="subject">Subject</label>
          <input
            id="subject"
            name="subject"
            type="text"
            value={formData.subject}
            onChange={handleChange}
            placeholder="Urgent: verify your account"
          />
        </div>

        <div className="form-field">
          <label htmlFor="emailBody">Email Body</label>
          <textarea
            id="emailBody"
            name="emailBody"
            value={formData.emailBody}
            onChange={handleChange}
            placeholder="Paste the full email body to assess phishing indicators..."
          />
        </div>

        <div className="form-field">
          <label htmlFor="url">URL</label>
          <input
            id="url"
            name="url"
            type="text"
            value={formData.url}
            onChange={handleChange}
            placeholder="https://example.com/login"
          />
        </div>

        <div className="form-actions">
          <button className="primary-button" type="submit" disabled={loading}>
            {loading ? 'Analyzing...' : 'Analyze Email'}
          </button>
        </div>
      </form>

      {error && <div className="inline-error">{error}</div>}
      {successMessage && <div className="inline-success">{successMessage}</div>}
    </section>
  )
}

export default EmailForm
