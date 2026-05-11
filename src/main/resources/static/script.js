let stompClient = null;
let currentUser = null;
let jwtToken = null;
let activeRoom = null;
let messages = { public: [] };
let privateUsers = [];

/* ================= AUTH ================= */

function showRegister() {
    document.getElementById("loginBox").style.display = "none";
    document.getElementById("registerBox").style.display = "flex";
}

function showLogin() {
    document.getElementById("registerBox").style.display = "none";
    document.getElementById("loginBox").style.display = "flex";
}

async function register() {
    const name = document.getElementById("regName").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const password = document.getElementById("regPassword").value.trim();
    if (!name || !email || !password) return;

    const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password })
    });

    if (res.ok) {
        const data = await res.json();
        onAuthSuccess(data);
    } else {
        document.getElementById("registerError").textContent = "Email already in use.";
    }
}

async function login() {
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    if (!email || !password) return;

    const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });

    if (res.ok) {
        const data = await res.json();
        onAuthSuccess(data);
    } else {
        document.getElementById("loginError").textContent = "Invalid email or password.";
    }
}

function onAuthSuccess(data) {
    localStorage.setItem("token", data.token);
    localStorage.setItem("name", data.name);
    jwtToken = data.token;
    currentUser = data.name;

    document.getElementById("authScreen").style.display = "none";
    document.getElementById("appScreen").style.display = "flex";
    document.getElementById("myName").textContent = currentUser;

    connect();
}

function logout() {
    localStorage.clear();
    if (stompClient) stompClient.disconnect();
    location.reload();
}

/* ================= SOCKET ================= */

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({ Authorization: 'Bearer ' + jwtToken }, () => {

        stompClient.subscribe('/topic/messages', (msg) => {
            handlePublicMessage(JSON.parse(msg.body));
        });

        stompClient.subscribe('/user/queue/private', (msg) => {
            handlePrivateMessage(JSON.parse(msg.body));
        });

        loadHistory();
        stompClient.send("/app/chat.join", {}, JSON.stringify({ messageType: "JOIN" }));
        switchRoom('public');
    });
}