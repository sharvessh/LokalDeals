class API {
    constructor() {
        this.baseURL = window.location.origin + '/api';
        this.token = localStorage.getItem('token');
    }

    setToken(token) {
        this.token = token;
        localStorage.setItem('token', token);
    }

    clearToken() {
        this.token = null;
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userName');
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        const config = { ...options, headers };

        try {
            const response = await fetch(url, config);
            
            if (response.status === 401) {
                this.clearToken();
                window.location.href = '/login.html';
                return null;
            }

            if (response.status === 204) return true;

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `HTTP ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API Error Network/Server:', error);
            throw error;
        }
    }

    // Auth
    async register(registerData) {
        return this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify(registerData)
        });
    }

    async login(email, password) {
        return this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
    }

    // Deals
    async getNearbyDeals(latitude, longitude, radiusKm, category = '') {
        let endpoint = `/deals/nearby?latitude=${latitude}&longitude=${longitude}&radiusKm=${radiusKm}`;
        if (category) endpoint += `&category=${encodeURIComponent(category)}`;
        return this.request(endpoint);
    }

    async createDeal(dealData) {
        return this.request('/deals', {
            method: 'POST',
            body: JSON.stringify(dealData)
        });
    }

    async deleteDeal(dealId) {
        return this.request(`/deals/${dealId}`, { method: 'DELETE' });
    }

    // Saved Deals
    async getSavedDeals() {
        return this.request('/saved-deals');
    }

    async saveDeal(dealId) {
        return this.request('/saved-deals', {
            method: 'POST',
            body: JSON.stringify({ dealId })
        });
    }

    async removeSavedDeal(dealId) {
        return this.request(`/saved-deals/${dealId}`, { method: 'DELETE' });
    }
}

const api = new API();

function logout() {
    api.clearToken();
    window.location.href = '/login.html';
}