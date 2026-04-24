const contentArea = document.getElementById("contentArea");
const sectionTitle = document.getElementById("sectionTitle");

// Handle sidebar navigation
document.querySelectorAll(".sidebar nav ul li").forEach(li => {
  li.addEventListener("click", () => {
    document.querySelectorAll(".sidebar nav ul li").forEach(l => l.classList.remove("active"));
    li.classList.add("active");
    const tab = li.dataset.tab;
    loadSection(tab);
  });
});

// Load section content dynamically
function loadSection(section) {
  sectionTitle.textContent = section === "ai" ? "AI Assistant" : section.charAt(0).toUpperCase() + section.slice(1);
  if (section === "dashboard") showDashboard();
  else if (section === "ai") showAIAssistant();
  else if (section === "patients") showPatients();
  else if (section === "doctors") showDoctors();
  else if (section === "appointments") showAppointments();
  else if (section === "bills") showBills();
  else if (section === "prescriptions") showPrescriptions();
}

// === AI Assistant ===
async function showAIAssistant() {
  contentArea.innerHTML = `
    <div class="ai-chat-container">
        <div id="chatBox" class="chat-box">
            <div class="message ai">Hello! I am the Arogya360 AI Assistant. I can help with hospital FAQs, standard medical queries, and checking doctor availability (e.g. "schedule for Smith"). How can I help you today?</div>
        </div>
        <div class="chat-input-area">
            <input type="text" id="chatInput" placeholder="Ask me anything...">
            <button onclick="sendChatMessage()">Send</button>
        </div>
    </div>
  `;
}

window.sendChatMessage = async function() {
    const input = document.getElementById("chatInput");
    const query = input.value.trim();
    if (!query) return;

    const chatBox = document.getElementById("chatBox");
    chatBox.innerHTML += `<div class="message user">${query}</div>`;
    input.value = "";
    
    // Add loading indicator
    const loadingId = "loading-" + Date.now();
    chatBox.innerHTML += `<div id="${loadingId}" class="message ai loading-msg">Thinking...</div>`;
    chatBox.scrollTop = chatBox.scrollHeight;

    try {
        const response = await apiPost("/api/ai/chat", { query });
        document.getElementById(loadingId).remove();
        chatBox.innerHTML += `<div class="message ai">${response.response.replace(/\\n/g, '<br>')}</div>`;
    } catch (e) {
        document.getElementById(loadingId).remove();
        chatBox.innerHTML += `<div class="message ai error-msg">Sorry, I couldn't process that request right now.</div>`;
    }
    chatBox.scrollTop = chatBox.scrollHeight;
}

// === Dashboard ===
async function showDashboard() {
  contentArea.innerHTML = `<div class="loading">Loading dashboard...</div>`;
  try {
    const stats = await apiGet("/dashboard/stats");
    contentArea.innerHTML = `
      <div class="dashboard-cards">
        <div class="card">
          <h3>Total Patients</h3>
          <p class="stat">${stats.totalPatients}</p>
        </div>
        <div class="card">
          <h3>Total Doctors</h3>
          <p class="stat">${stats.totalDoctors}</p>
        </div>
        <div class="card">
          <h3>Total Appointments</h3>
          <p class="stat">${stats.totalAppointments}</p>
        </div>
        <div class="card">
          <h3>Total Revenue</h3>
          <p class="stat">$${stats.totalRevenue ? stats.totalRevenue.toFixed(2) : '0.00'}</p>
        </div>
      </div>
    `;
  } catch (error) {
    contentArea.innerHTML = `<div class="error">Failed to load dashboard.</div>`;
  }
}

// === Patients ===
async function showPatients() {
  contentArea.innerHTML = `
    <div class="top-bar">
        <form id="patientForm">
          <input id="pname" placeholder="Name" required>
          <input id="page" type="number" placeholder="Age" required>
          <button>Add Patient</button>
        </form>
        <div class="search-bar">
            <input id="searchPatient" placeholder="Search patients...">
            <button onclick="searchPatient()">Search</button>
        </div>
    </div>
    <ul id="patientList" class="data-list"></ul>
  `;

  loadPatientList();

  document.getElementById("patientForm").addEventListener("submit", async e => {
    e.preventDefault();
    const name = document.getElementById("pname").value;
    const age = document.getElementById("page").value;
    await apiPost("/patients", { name, age });
    loadPatientList();
  });
}

async function loadPatientList(query = "") {
    const url = query ? `/patients/search?name=${encodeURIComponent(query)}` : "/patients";
    const data = await apiGet(url);
    renderList(data, "patientList", "/patients", ["name", "age"]);
}

window.searchPatient = function() {
    const query = document.getElementById("searchPatient").value;
    loadPatientList(query);
}

// === Doctors ===
async function showDoctors() {
  contentArea.innerHTML = `
    <div class="top-bar">
        <form id="doctorForm">
          <input id="dname" placeholder="Name" required>
          <input id="dspeciality" placeholder="Speciality" required>
          <button>Add Doctor</button>
        </form>
        <div class="search-bar">
            <input id="searchDoctor" placeholder="Search doctors...">
            <button onclick="searchDoctor()">Search</button>
        </div>
    </div>
    <ul id="doctorList" class="data-list"></ul>
  `;

  loadDoctorList();

  document.getElementById("doctorForm").addEventListener("submit", async e => {
    e.preventDefault();
    const name = document.getElementById("dname").value;
    const speciality = document.getElementById("dspeciality").value;
    await apiPost("/doctors", { name, speciality });
    loadDoctorList();
  });
}

async function loadDoctorList(query = "") {
    const url = query ? `/doctors/search?name=${encodeURIComponent(query)}` : "/doctors";
    const data = await apiGet(url);
    renderList(data, "doctorList", "/doctors", ["name", "speciality"]);
}

window.searchDoctor = function() {
    const query = document.getElementById("searchDoctor").value;
    loadDoctorList(query);
}


// === Appointments ===
async function showAppointments() {
  contentArea.innerHTML = `<ul id="appList" class="data-list"></ul>`;
  const data = await apiGet("/appointments");
  // Include status in the fields
  renderList(data, "appList", "/appointments", ["date", "patientName", "doctorName", "status"]);
}

// === Bills ===
async function showBills() {
  contentArea.innerHTML = `<ul id="billList" class="data-list"></ul>`;
  const data = await apiGet("/bills");
  renderList(data, "billList", "/bills", ["amount", "patientName"]);
}

// === Prescriptions ===
async function showPrescriptions() {
  contentArea.innerHTML = `<ul id="prescList" class="data-list"></ul>`;
  const data = await apiGet("/prescriptions");
  renderList(data, "prescList", "/prescriptions", ["medicineName", "dosage"]);
}

// === Helper to render lists ===
function renderList(items, elementId, baseUrl, fields) {
  const list = document.getElementById(elementId);
  list.innerHTML = "";
  items.forEach(item => {
    const li = document.createElement("li");
    li.className = "data-item";

    const info = fields.map(f => `${f}: ${item[f] || "-"}`).join(", ");
    li.innerHTML = `<span>${info}</span>`;

    const actionDiv = document.createElement("div");
    actionDiv.className = "action-buttons";

    if (baseUrl === "/patients") {
        const aiBtn = document.createElement("button");
        aiBtn.textContent = "💊 Suggest Medicine";
        aiBtn.className = "btn-ai";
        aiBtn.onclick = async () => {
            aiBtn.textContent = "Thinking...";
            try {
                const res = await apiGet(`/api/ai/suggest-medicine/${item.id}`);
                alert("AI Suggestion for " + item.name + ":\n\n" + res.suggestion);
            } catch (e) {
                alert("Failed to get suggestion.");
            }
            aiBtn.textContent = "💊 Suggest Medicine";
        };
        actionDiv.appendChild(aiBtn);
    }

    const delBtn = document.createElement("button");
    delBtn.textContent = "Delete";
    delBtn.className = "btn-delete";
    delBtn.onclick = async () => {
      await apiDelete(`${baseUrl}/${item.id}`);
      loadSection(baseUrl.replace("/", ""));
    };
    
    actionDiv.appendChild(delBtn);
    li.appendChild(actionDiv);
    list.appendChild(li);
  });
}

// Load default section
loadSection("dashboard");
