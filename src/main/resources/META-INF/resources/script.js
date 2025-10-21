    let eventSource;

    function setRange() {
    const config = {
    userId: document.getElementById('userId').value,
    metricType: document.getElementById('metricType').value,
    minValue: parseFloat(document.getElementById('minValue').value),
    maxValue: parseFloat(document.getElementById('maxValue').value)
};

    fetch(`/ranges/${config.userId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(config)
})
    .then(response => response.json())
    .then(data => {
    showNotification(`✅ Range set for ${data.userId}`, 'success');
})
    .catch(error => {
    showNotification('❌ Error setting range', 'error');
    console.error('Error:', error);
});
}

    function connectAlerts() {
    if (eventSource) {
    eventSource.close();
}

    eventSource = new EventSource('/alerts/stream');
    updateStatus('connected');

    eventSource.onmessage = function(event) {
    const alert = JSON.parse(event.data);
    displayAlert(alert);
};

    eventSource.onerror = function(error) {
    updateStatus('error');
    console.error('EventSource error:', error);
};
}

    function disconnectAlerts() {
    if (eventSource) {
    eventSource.close();
    eventSource = null;
    updateStatus('disconnected');
}
}

    function updateStatus(status) {
    const statusEl = document.getElementById('status');
    const statusMap = {
    connected: { text: 'Connected', class: 'bg-green-100 text-green-800' },
    disconnected: { text: 'Disconnected', class: 'bg-gray-200 text-gray-700' },
    error: { text: 'Error', class: 'bg-red-100 text-red-800' }
};
    const config = statusMap[status];
    statusEl.textContent = config.text;
    statusEl.className = `px-3 py-1 rounded-full text-sm font-medium ${config.class}`;
}

    function displayAlert(alert) {
    const alertsDiv = document.getElementById('alerts');
    const noAlerts = document.getElementById('no-alerts');

    if (noAlerts) {
    noAlerts.remove();
}

    const alertElement = document.createElement('div');
    alertElement.className = 'bg-red-50 border-l-4 border-red-500 p-4 rounded-lg animate-fade-in';
    alertElement.innerHTML = `
                <div class="flex items-start">
                    <div class="flex-shrink-0">
                        <span class="text-2xl">🚨</span>
                    </div>
                    <div class="ml-3 flex-1">
                        <p class="font-semibold text-red-800">${alert.userId}</p>
                        <p class="text-red-700 mt-1">${alert.message}</p>
                        <p class="text-sm text-red-600 mt-2">
                            ${alert.metricType}: ${alert.currentValue.toFixed(2)}
                            (Range: ${alert.minValue.toFixed(2)} - ${alert.maxValue.toFixed(2)})
                        </p>
                        <p class="text-xs text-red-500 mt-1">
                            ${new Date(alert.timestamp).toLocaleString()}
                        </p>
                    </div>
                </div>
            `;

    alertsDiv.insertBefore(alertElement, alertsDiv.firstChild);

    // Keep only last 50 alerts
    while (alertsDiv.children.length > 50) {
    alertsDiv.removeChild(alertsDiv.lastChild);
}
}

    function showNotification(message, type) {
    const notification = document.createElement('div');
    const bgColor = type === 'success' ? 'bg-green-500' : 'bg-red-500';
    notification.className = `fixed top-4 right-4 ${bgColor} text-white px-6 py-3 rounded-lg shadow-lg z-50 animate-fade-in`;
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
    notification.remove();
}, 3000);
}

    // Auto-connect on page load
    window.onload = connectAlerts;
