// script.js
import { supabase } from "./supabaseClient.js";

const users = {
  him: { password: "loveher", name: "Him 💙" },
  her: { password: "lovehim", name: "Her 💖" }
};

const currentUser = localStorage.getItem("currentUser");

// --- LOGIN ---
const loginForm = document.getElementById("loginForm");
if (loginForm) {
  loginForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const uname = document.getElementById("username").value.trim().toLowerCase();
    const pass = document.getElementById("password").value.trim();

    if (users[uname] && users[uname].password === pass) {
      localStorage.setItem("currentUser", uname);
      window.location.href = "home.html";
    } else {
      alert("Wrong credentials 🥲");
    }
  });
}

// --- LOGOUT ---
const logoutBtn = document.getElementById("logoutBtn");
if (logoutBtn) {
  logoutBtn.addEventListener("click", () => {
    localStorage.removeItem("currentUser");
    window.location.href = "index.html";
  });
}

// --- DAILY QUESTIONS ---
const dailyForm = document.getElementById("dailyForm");
if (dailyForm) {
  dailyForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const q1 = document.getElementById("q1").value.trim();
    const q2 = document.getElementById("q2").value.trim();
    const q3 = document.getElementById("q3").value.trim();

    if (!q1 || !q2 || !q3) {
      alert("Please fill in all fields!");
      return;
    }

    const { error } = await supabase.from("daily_questions").insert([{
      username: currentUser,
      q1,
      q2,
      q3,
      created_at: new Date().toISOString()
    }]);

    const status = document.getElementById("statusMsg");
    if (!error) {
      if (status) status.textContent = "Saved successfully 💞";
      dailyForm.reset();
    } else {
      console.error("Supabase Error:", error);
      if (status) status.textContent = "Something went wrong 😢";
    }
  });
}

// --- SECRET GALLERY ---
async function loadSecretGallery() {
  const gallery = document.getElementById("secretGallery");
  if (!gallery) return;

  const { data, error } = await supabase
    .from("secrets")
    .select("*")
    .eq("to_user", currentUser)
    .order("created_at", { ascending: false });

  if (error) {
    console.error("Gallery Load Error:", error);
    return;
  }

  gallery.innerHTML = "";
  data.forEach((item) => {
    const card = document.createElement("div");
    card.classList.add("secret-card");
    card.innerHTML = `
      <img src="${item.url}" alt="secret" class="blur" />
      <div class="overlay">🔥 Tap to Reveal</div>
      <p class="caption">${item.caption}</p>
    `;
    card.addEventListener("click", () => {
      card.classList.add("revealed");
      markSeen(item.id);
    });
    gallery.appendChild(card);
  });
}

async function markSeen(id) {
  await supabase.from("secrets").update({ seen: true }).eq("id", id);
}

if (document.getElementById("secretGallery")) {
  loadSecretGallery();
}
