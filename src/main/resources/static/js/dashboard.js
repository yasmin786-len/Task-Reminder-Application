const BASE = "http://localhost:8080";

window.onload = () => {
    loadTasks();
    loadOverview();
};

function addTask() {
    fetch(`${BASE}/tasks/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            title: title.value,
            description: description.value,
            email: email.value,
            dueDate: dueDate.value,
            completed: false
        })
    }).then(() => {
        loadTasks();
        loadOverview();
    });
}
function deleteTask(id) {
    if (!confirm("Are you sure you want to delete this task?")) return;

    fetch(`${BASE}/tasks/delete/${id}`, {
        method: "DELETE"
    }).then(() => {
        loadTasks();
        loadOverview();
    });
}

function loadTasks() {
    fetch(`${BASE}/tasks/list`)
        .then(r => r.json())
        .then(tasks => {
            taskTable.innerHTML = "";
            tasks.forEach(t => {
                taskTable.innerHTML += `
                    <tr>
                        <td>${t.id}</td>
                        <td>${t.title}</td>
                        <td>${t.dueDate}</td>
                        <td>${t.completed ? "Completed" : "Pending"}</td>
                        <td>
                            <button onclick="schedule(${t.id})">⏰</button>
    <button onclick="complete(${t.id})">✔</button>
    <button onclick="deleteTask(${t.id})">🗑</button>
                        </td>
                    </tr>
                `;
            });
        });
}

function schedule(id) {
    fetch(`${BASE}/schedule/set/${id}`, { method: "POST" })
        .then(() => alert("Reminder Scheduled"));
}

function complete(id) {
    fetch(`${BASE}/completion/mark/${id}`, { method: "PUT" })
        .then(() => {
            loadTasks();
            loadOverview();
        });
}

function loadOverview() {
    fetch(`${BASE}/reports/overview`)
        .then(r => r.json())
        .then(res => {
            const o = res.data;   // ✅ IMPORTANT
            totalTasks.innerText = o.totalTasks;
            completedTasks.innerText = o.completedTasks;
            pendingTasks.innerText = o.pendingTasks;
        });
}


function downloadCSV() {
    window.location.href = `${BASE}/reports/export`;
}
