const HistoryTable = ({ history, onView, onDelete, deletingId }) => {
  if (!history || history.length === 0) {
    return (
      <div className="history-card">
        <div className="panel-header">
          <h3>Analysis History</h3>
        </div>
        <div className="empty-state">No email analyses yet.</div>
      </div>
    )
  }

  return (
    <div className="history-card">
      <div className="panel-header">
        <h3>Analysis History</h3>
      </div>

      <div className="table-wrap">
        <table className="history-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Sender</th>
              <th>Subject</th>
              <th>Prediction</th>
              <th>Score</th>
              <th>Risk</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {history.map((item) => (
              <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.sender || 'N/A'}</td>
                <td>{item.subject || 'N/A'}</td>
                <td>
                  <span className={`status-badge ${String(item.prediction || '').toLowerCase()}`}>
                    {item.prediction || 'N/A'}
                  </span>
                </td>
                <td>{item.phishingScore != null ? Number(item.phishingScore).toFixed(2) : 'N/A'}</td>
                <td>
                  <span className={`status-badge ${String(item.riskLevel || '').toLowerCase()}`}>
                    {item.riskLevel || 'N/A'}
                  </span>
                </td>
                <td>
                  <div className="action-buttons">
                    <button type="button" className="table-button" onClick={() => onView(item.id)}>
                      View
                    </button>
                    <button
                      type="button"
                      className="delete-button"
                      onClick={() => onDelete(item.id)}
                      disabled={deletingId === item.id}
                    >
                      {deletingId === item.id ? 'Deleting...' : 'Delete'}
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default HistoryTable
