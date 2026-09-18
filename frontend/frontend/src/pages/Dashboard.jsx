import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import EmailForm from '../components/EmailForm'
import AnalysisResult from '../components/AnalysisResult'
import HistoryTable from '../components/HistoryTable'
import {
  analyzeEmail,
  getAnalysisById,
  getHistory,
  deleteAnalysis,
} from '../services/api'

const initialFormData = {
  sender: '',
  subject: '',
  emailBody: '',
  url: '',
}

const Dashboard = () => {
  const [formData, setFormData] = useState(initialFormData)
  const [history, setHistory] = useState([])
  const [selectedAnalysis, setSelectedAnalysis] = useState(null)
  const [loading, setLoading] = useState(false)
  const [isHistoryLoading, setIsHistoryLoading] = useState(false)
  const [formError, setFormError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const [deletingId, setDeletingId] = useState(null)

  const fetchHistory = async () => {
    setIsHistoryLoading(true)

    try {
      const response = await getHistory()
      setHistory(response.data || [])
    } catch (error) {
      console.error('Unable to fetch history:', error)
      setHistory([])
    } finally {
      setIsHistoryLoading(false)
    }
  }

  useEffect(() => {
    fetchHistory()
  }, [])

  const validateForm = () => {
    const trimmedSender = formData.sender.trim()
    const trimmedSubject = formData.subject.trim()
    const trimmedBody = formData.emailBody.trim()

    if (!trimmedSender) {
      return 'Sender cannot be empty.'
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(trimmedSender)) {
      return 'Please enter a valid sender email address.'
    }

    if (!trimmedSubject) {
      return 'Subject cannot be empty.'
    }

    if (!trimmedBody) {
      return 'Email body cannot be empty.'
    }

    return ''
  }

  const handleAnalyze = async () => {
    const validationMessage = validateForm()

    if (validationMessage) {
      setFormError(validationMessage)
      setSuccessMessage('')
      return
    }

    setLoading(true)
    setFormError('')
    setSuccessMessage('')

    try {
      const payload = {
        sender: formData.sender.trim(),
        subject: formData.subject.trim(),
        emailBody: formData.emailBody.trim(),
        url: formData.url.trim(),
      }

      const response = await analyzeEmail(payload)
      const result = response.data

      setSelectedAnalysis(result)
      setSuccessMessage('Analysis complete. Threat assessment updated.')
      setFormData(initialFormData)
      await fetchHistory()
    } catch (error) {
      const message =
        error?.response?.data?.message ||
        error?.response?.data?.error ||
        'Unable to analyze the email. Please check the backend connection and try again.'

      setFormError(message)
      setSelectedAnalysis(null)
    } finally {
      setLoading(false)
    }
  }

  const handleView = async (id) => {
    try {
      const response = await getAnalysisById(id)
      setSelectedAnalysis(response.data)
      setFormError('')
    } catch (error) {
      const message =
        error?.response?.data?.message ||
        'Unable to load the selected analysis.'
      setFormError(message)
    }
  }

  const handleDelete = async (id) => {
    if (!id) return

    setDeletingId(id)
    setFormError('')

    try {
      await deleteAnalysis(id)
      setSuccessMessage('Analysis deleted successfully.')
      setSelectedAnalysis((current) => (current && current.id === id ? null : current))
      await fetchHistory()
    } catch (error) {
      const message =
        error?.response?.data?.message ||
        'Unable to delete the selected analysis.'
      setFormError(message)
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <div className="dashboard-app">
      <div className="dashboard-shell">
        <Navbar />

        <div className="dashboard-grid">
          <EmailForm
            formData={formData}
            setFormData={setFormData}
            onSubmit={handleAnalyze}
            loading={loading}
            error={formError}
            successMessage={successMessage}
          />

          <AnalysisResult analysis={selectedAnalysis} />
        </div>

        <section className="history-section panel history-card">
          <div className="panel-header">
            <div>
              <h3>Analysis History</h3>
              <span className="panel-subtitle">
                {isHistoryLoading ? 'Refreshing records...' : 'Recent phishing assessments'}
              </span>
            </div>
          </div>

          {isHistoryLoading && history.length === 0 ? (
            <div className="empty-state">Loading analysis history...</div>
          ) : (
            <HistoryTable history={history} onView={handleView} onDelete={handleDelete} deletingId={deletingId} />
          )}
        </section>
      </div>
    </div>
  )
}

export default Dashboard
