import axios from 'axios'

const api = axios.create({
    baseURL: 'https://worlde-backend-1788530434134.azurewebsites.net'
})

export default api
