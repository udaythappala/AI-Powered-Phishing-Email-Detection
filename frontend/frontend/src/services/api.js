import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 20000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const analyzeEmail = (data) => api.post('/api/emails/analyze', data)
export const getHistory = () => api.get('/api/emails/history')
export const getAnalysisById = (id) => api.get(`/api/emails/${id}`)
export const deleteAnalysis = (id) => api.delete(`/api/emails/${id}`)

export default api
