import {
  ref,
  push,
  onChildAdded,
  onValue,
  update,
  set,
  onDisconnect,
  get
} from "https://www.gstatic.com/firebasejs/12.0.0/firebase-database.js";

const currentUser = localStorage.getItem("currentUser");
const users = {
  him: { partner: "her" },
  her: { partner: "him" }
};
const partner = users[currentUser]?.partner;

const input = document.getElementById("chatInput");
const chatBox = document.getElementById("chatBox");
const form = document.getElementById("chatForm");
const logoutBtn = document.getElementById("logoutBtn");
const status = document.querySelector(".chat-status");

const chatId = [currentUser, partner].sort().join("_");
const chatRef = ref(window.db, `chats/${chatId}`);
const typingRef = ref(window.db, `typingStatus/${currentUser}`);
const partnerTypingRef = ref(window.db, `typingStatus/${partner}`);
const presenceRef = ref(window.db, `presence/${currentUser}`);
const partnerPresenceRef = ref(window.db, `presence/${partner}`);

// ------------------ PRESENCE ------------------ //
function updatePresence(online) {
  set(presenceRef, {
    online,
    lastSeen: Date.now(),
    onChat: true
  });
}
updatePresence(navigator.onLine);
onDisconnect(presenceRef).set({
  online: false,
  lastSeen: Date.now(),
  onChat: false
});
window.addEventListener("offline", () => updatePresence(false));
window.addEventListener("online", () => updatePresence(true));

// ------------------ TYPING ------------------ //
let typing = false;
let typingTimeout;

input.addEventListener("input", () => {
  if (!typing) {
    typing = true;
    set(typingRef, true);
  }
  clearTimeout(typingTimeout);
  typingTimeout = setTimeout(() => {
    typing = false;
    set(typingRef, false);
  }, 1500);
});

// ------------------ PRESENCE DISPLAY ------------------ //
onValue(partnerTypingRef, (snap) => {
  const isTyping = snap.val();
  if (isTyping) {
    status.textContent = "typing...";
  } else {
    updateStatusFromPresence();
  }
});

onValue(partnerPresenceRef, async (snap) => {
  const data = snap.val();
  if (!data) return;

  const { online, onChat, lastSeen } = data;
  status.dataset.online = online;
  status.dataset.lastSeen = lastSeen;
  status.dataset.onChat = onChat;

  updateStatusFromPresence();

  // ✅ Update ticks for existing messages
  const snapshot = await get(chatRef);
  const messages = snapshot.val();
  if (!messages) return;

  for (const [key, msg] of Object.entries(messages)) {
    if (msg.sender !== currentUser) continue;

    if (onChat && msg.status !== "read") {
      await update(ref(window.db, `chats/${chatId}/${key}`), { status: "read" });
    } else if (online && msg.status === "sent") {
      await update(ref(window.db, `chats/${chatId}/${key}`), { status: "delivered" });
    } else if (!online && msg.status === "sent") {
      sendNotificationToPartner(msg.content);
    }
  }
});

function updateStatusFromPresence() {
  const online = status.dataset.online === "true";
  const lastSeen = parseInt(status.dataset.lastSeen || "0");

  if (online) {
    status.textContent = "online";
  } else {
    const last = new Date(lastSeen);
    const now = new Date();
    const isYesterday =
      last.getDate() === now.getDate() - 1 &&
      last.getMonth() === now.getMonth() &&
      last.getFullYear() === now.getFullYear();

    const timeStr = last.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
      hour12: true
    });

    status.textContent = isYesterday
      ? `last seen yesterday at ${timeStr}`
      : `last seen at ${timeStr}`;
  }
}

// ------------------ LOGOUT ------------------ //
logoutBtn?.addEventListener("click", () => {
  localStorage.removeItem("currentUser");
  window.location.href = "index.html";
});

// ------------------ SEND MESSAGE ------------------ //
form.addEventListener("submit", async (e) => {
  e.preventDefault();
  const msg = input.value.trim();
  if (!msg) return;

  const newMsgRef = push(chatRef);
  await set(newMsgRef, {
    sender: currentUser,
    receiver: partner,
    content: msg,
    timestamp: Date.now(),
    status: "sent"
  });

  input.value = "";
});

// ------------------ RECEIVE MESSAGES ------------------ //
onChildAdded(chatRef, (snapshot) => {
  const msg = snapshot.val();
  const key = snapshot.key;

  if (msg.receiver === currentUser && msg.status !== "read") {
    update(ref(window.db, `chats/${chatId}/${key}`), { status: "read" });
  }

  renderMessage(msg);
  chatBox.scrollTop = chatBox.scrollHeight;
});

// ------------------ RENDER MESSAGES ------------------ //
function renderMessage(msg) {
  const div = document.createElement("div");
  div.className = `chat-message ${msg.sender === currentUser ? "you" : "partner"}`;

  const time = new Date(msg.timestamp).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true
  });

  let ticks = "";
  if (msg.sender === currentUser) {
    if (msg.status === "read") {
      ticks = `<span style="color: blue;">✔✔</span>`;
    } else if (msg.status === "delivered") {
      ticks = `<span style="color: gray;">✔✔</span>`;
    } else {
      ticks = `<span style="color: gray;">✔</span>`;
    }
  }

  div.innerHTML = `
    ${msg.content}
    <span class="timestamp">${time} ${ticks}</span>
  `;
  chatBox.appendChild(div);
}

// ------------------ ONESIGNAL PUSH (OFFLINE) ------------------ //
function sendNotificationToPartner(message) {
  fetch("https://onesignal.com/api/v1/notifications", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Basic qzftpxdnqurd5pikjh5zkzdjh"
    },
    body: JSON.stringify({
      app_id: "02d91be9-005b-4470-9c62-f552a1ea2064",
      include_external_user_ids: [partner],
      headings: { en: "New Message 💌" },
      contents: { en: message }
    })
  });
}
