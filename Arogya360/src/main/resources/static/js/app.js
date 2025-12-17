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
  sectionTitle.textContent = section.charAt(0).toUpperCase() + section.slice(1);
  if (section === "patients") showPatients();
  else if (section === "doctors") showDoctors();
  else if (section === "appointments") showAppointments();
  else if (section === "bills") showBills();
  else if (section === "prescriptions") showPrescriptions();
}

// === Patients ===
async function showPatients() {
  contentArea.innerHTML = `
    <form id="patientForm">
      <input id="pname" placeholder="Name" required>
      <input id="page" type="number" placeholder="Age" required>
      <button>Add Patient</button>
    </form>
    <ul id="patientList" class="data-list"></ul>
  `;

  const data = await apiGet("/patients");
  renderList(data, "patientList", "/patients", ["name", "age"]);

  document.getElementById("patientForm").addEventListener("submit", async e => {
    e.preventDefault();
    const name = document.getElementById("pname").value;
    const age = document.getElementById("page").value;
    await apiPost("/patients", { name, age });
    showPatients();
  });
}

// === Doctors ===
async function showDoctors() {
  contentArea.innerHTML = `
    <form id="doctorForm">
      <input id="dname" placeholder="Name" required>
      <input id="dspeciality" placeholder="Speciality" required>
      <button>Add Doctor</button>
    </form>
    <ul id="doctorList" class="data-list"></ul>
  `;

  const data = await apiGet("/doctors");
  renderList(data, "doctorList", "/doctors", ["name", "speciality"]);

  document.getElementById("doctorForm").addEventListener("submit", async e => {
    e.preventDefault();
    const name = document.getElementById("dname").value;
    const speciality = document.getElementById("dspeciality").value;
    await apiPost("/doctors", { name, speciality });
    showDoctors();
  });
}

// === Appointments ===
async function showAppointments() {
  contentArea.innerHTML = `<ul id="appList" class="data-list"></ul>`;
  const data = await apiGet("/appointments");
  renderList(data, "appList", "/appointments", ["date", "patientName", "doctorName"]);
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

    const delBtn = document.createElement("button");
    delBtn.textContent = "Delete";
    delBtn.onclick = async () => {
      await apiDelete(`${baseUrl}/${item.id}`);
      loadSection(baseUrl.replace("/", ""));
    };
    li.appendChild(delBtn);
    list.appendChild(li);
  });
}

// Load default section
loadSection("patients");
