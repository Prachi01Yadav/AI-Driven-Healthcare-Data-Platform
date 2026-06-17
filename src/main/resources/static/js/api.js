// API Configuration
const API_BASE = '';
let authToken = localStorage.getItem('arogya360_token');

// Set auth token
function setAuthToken(token) {
    authToken = token;
    if (token) localStorage.setItem('arogya360_token', token);
    else localStorage.removeItem('arogya360_token');
}

// Get auth headers
function getHeaders() {
    const headers = { 'Content-Type': 'application/json' };
    if (authToken) headers['Authorization'] = `Bearer ${authToken}`;
    return headers;
}

async function apiGet(path) {
    try {
        const res = await fetch(API_BASE + path, { headers: getHeaders() });
        if (res.status === 401) { handleUnauthorized(); throw new Error('Unauthorized'); }
        if (!res.ok) throw new Error(`GET ${path} failed: ${res.status}`);
        return await res.json();
    } catch (e) {
        console.error(e);
        throw e;
    }
}

async function apiPost(path, data) {
    try {
        const res = await fetch(API_BASE + path, {
            method: 'POST', headers: getHeaders(), body: JSON.stringify(data)
        });
        if (res.status === 401) { handleUnauthorized(); throw new Error('Unauthorized'); }
        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || `POST ${path} failed`);
        }
        const text = await res.text();
        try { return JSON.parse(text); } catch { return text; }
    } catch (e) {
        console.error(e);
        throw e;
    }
}

async function apiPut(path, data) {
    try {
        const res = await fetch(API_BASE + path, {
            method: 'PUT', headers: getHeaders(), body: JSON.stringify(data)
        });
        if (res.status === 401) { handleUnauthorized(); throw new Error('Unauthorized'); }
        if (!res.ok) throw new Error(`PUT ${path} failed`);
        const text = await res.text();
        try { return JSON.parse(text); } catch { return text; }
    } catch (e) {
        console.error(e);
        throw e;
    }
}

async function apiDelete(path) {
    try {
        const res = await fetch(API_BASE + path, { method: 'DELETE', headers: getHeaders() });
        if (res.status === 401) { handleUnauthorized(); throw new Error('Unauthorized'); }
        if (!res.ok) throw new Error(`DELETE ${path} failed`);
        return await res.text();
    } catch (e) {
        console.error(e);
        throw e;
    }
}

function handleUnauthorized() {
    setAuthToken(null);
    document.getElementById('appContainer').style.display = 'none';
    document.getElementById('loginPage').style.display = 'flex';
}

// Toast Notification System
function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    
    // SVG icons instead of emojis
    let iconSvg = '';
    if (type === 'success') iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--success)" stroke-width="2"><polyline points="20 6 9 17 4 12"></polyline></svg>';
    else if (type === 'error') iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--danger)" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>';
    else if (type === 'warning') iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--warning)" stroke-width="2"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>';
    else iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--primary)" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>';

    toast.innerHTML = `<span class="toast-icon">${iconSvg}</span><span class="toast-message">${message}</span>`;
    container.appendChild(toast);
    
    setTimeout(() => { 
        toast.classList.add('toast-exit'); 
        setTimeout(() => toast.remove(), 300); 
    }, 4000);
}

// Modal System
function showModal(title, contentHtml, footerHtml = '') {
    const overlay = document.getElementById('modalOverlay');
    const modal = document.getElementById('modalContent');
    modal.innerHTML = `
        <div class="modal-header">
            <h3>${title}</h3>
            <button class="modal-close" onclick="closeModal()">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
            </button>
        </div>
        <div class="modal-body">${contentHtml}</div>
        ${footerHtml ? `<div class="modal-footer">${footerHtml}</div>` : ''}
    `;
    overlay.style.display = 'flex';
}

function closeModal() {
    document.getElementById('modalOverlay').style.display = 'none';
}

// WebSocket Connection
let stompClient = null;
function connectWebSocket() {
    try {
        if (typeof SockJS === 'undefined' || typeof StompJs === 'undefined') return;
        const socket = new SockJS('/ws');
        stompClient = new StompJs.Client({ webSocketFactory: () => socket });
        stompClient.onConnect = () => {
            stompClient.subscribe('/topic/notifications', (msg) => {
                const notif = JSON.parse(msg.body);
                showToast(notif.message, 'info');
                fetchUnreadNotificationsCount();
            });
        };
        stompClient.activate();
    } catch(e) { console.log('WebSocket not available', e); }
}

async function fetchUnreadNotificationsCount() {
    try {
        const notifs = await apiGet('/api/notifications/unread');
        const badge = document.getElementById('notifBadge');
        if (notifs.length > 0) {
            badge.textContent = notifs.length;
            badge.style.display = 'flex';
        } else {
            badge.style.display = 'none';
        }
    } catch (e) {
        console.error("Failed to fetch notifications");
    }
}
