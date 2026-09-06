/* Jaipur Book Store - frontend
   The backend controller is mapped to /Jaipur-BookStore-online/ai.
   Keep this value in ONE place so every request uses the same URL.
*/
const API_BASE = window.location.origin + "/Jaipur-BookStore-online/ai";

const $ = (id) => document.getElementById(id);
const esc = (value) => String(value ?? "").replace(/[&<>'"]/g, (c) => ({
  "&": "&amp;", "<": "&lt;", ">": "&gt;", "'": "&#39;", '"': "&quot;"
}[c]));

let users = [];
let books = [];
let currentChatMode = "general";

function setResult(id, message, ok = true) {
  const el = $(id);
  el.textContent = message;
  el.className = "result " + (ok ? "success" : "error");
}

function setBusy(button, busy, text) {
  if (!button) return;
  if (busy) {
    button.dataset.originalText = button.textContent;
    button.disabled = true;
    button.textContent = text || "Please wait…";
  } else {
    button.disabled = false;
    button.textContent = button.dataset.originalText || button.textContent;
  }
}

async function request(path, options = {}) {
  const url = API_BASE + path;
  let response;
  try {
    response = await fetch(url, {
      ...options,
      headers: {
        ...(options.body ? { "Content-Type": "application/json" } : {}),
        ...(options.headers || {})
      }
    });
  } catch (networkError) {
    throw new Error(`Cannot connect to Spring Boot: ${networkError.message}`);
  }

  const text = await response.text();
  if (!response.ok) {
    let detail = text;
    try {
      const json = JSON.parse(text);
      detail = json.message || json.error || text;
    } catch (_) {}
    throw new Error(`${response.status} ${response.statusText}: ${detail || "Request failed"}`);
  }

  const type = response.headers.get("content-type") || "";
  if (type.includes("application/json")) return text ? JSON.parse(text) : null;
  return text;
}

async function checkApi() {
  const status = $("apiStatus");
  try {
    const data = await request("/getUserList?page=0&size=1");
    status.textContent = "● Backend online";
    status.className = "status online";
    $("apiDetail").textContent = `Connected to ${API_BASE}`;
    return data;
  } catch (e) {
    status.textContent = "● Backend unavailable";
    status.className = "status offline";
    $("apiDetail").textContent = e.message;
    return null;
  }
}

function pageContent(data) {
  return Array.isArray(data) ? data : (data?.content || []);
}

function populateUserSelects() {
  const selects = [$("cartUserId"), $("chatCustomerId"), $("updateUserId"), $("deleteUserId")];
  selects.forEach(select => {
    if (!select) return;
    const old = select.value;
    const first = select.id === "cartUserId" ? "Select customer" : "Select user";
    select.innerHTML = `<option value="">${first}</option>` + users.map(u =>
      `<option value="${esc(u.userId)}">${esc(u.userName)} — ID ${esc(u.userId)}</option>`
    ).join("");
    if (old && users.some(u => String(u.userId) === String(old))) select.value = old;
  });
}

function populateBookSelect() {
  const select = $("cartBookId");
  const old = select.value;
  select.innerHTML = `<option value="">Select book</option>` + books.map(b =>
    `<option value="${esc(b.bookId)}">${esc(b.bookName)} — ₹${esc(b.sellingPrice)} — stock ${esc(b.stockQuantity)} — ID ${esc(b.bookId)}</option>`
  ).join("");
  if (old && books.some(b => String(b.bookId) === String(old))) select.value = old;
}

async function loadBooks() {
  const el = $("books");
  el.innerHTML = `<div class="loading">Loading books from H2…</div>`;
  try {
    const data = await request("/getBookList?page=0&size=100&sort=bookName,asc");
    books = pageContent(data);
    populateBookSelect();
    $("bookCount").textContent = books.length;
    if (!books.length) {
      el.innerHTML = `<div class="empty">No books found in H2. Use <b>Add Book</b> below.</div>`;
      return;
    }
    el.innerHTML = books.map(b => `
      <article class="book-card">
        <div class="book-top"><span class="pill">${esc(b.category)}</span><span class="id">ID ${esc(b.bookId)}</span></div>
        <h3>${esc(b.bookName)}</h3>
        <p class="writer">by ${esc(b.writerName)}</p>
        <div class="book-bottom">
          <strong>₹${esc(b.sellingPrice)}</strong>
          <span class="stock ${Number(b.stockQuantity) > 0 ? "in" : "out"}">${Number(b.stockQuantity) > 0 ? `Stock ${esc(b.stockQuantity)}` : "Out of stock"}</span>
        </div>
      </article>
    `).join("");
  } catch (e) {
    el.innerHTML = `<div class="empty error-box">Could not load books.<br><small>${esc(e.message)}</small></div>`;
  }
}

async function loadUsers() {
  const el = $("users");
  el.innerHTML = `<div class="loading">Loading users from H2…</div>`;
  try {
    const data = await request("/getUserList?page=0&size=100&sort=userName,asc");
    users = pageContent(data);
    populateUserSelects();
    $("userCount").textContent = users.length;
    if (!users.length) {
      el.innerHTML = `<div class="empty">No users found in H2. Use <b>Add User</b> below.</div>`;
      return;
    }
    el.innerHTML = users.map(u => `
      <div class="user-row">
        <div class="avatar">${esc((u.userName || "?").charAt(0).toUpperCase())}</div>
        <div class="user-main"><strong>${esc(u.userName)}</strong><span>${esc(u.gender)} · ${esc(u.userStatus)}</span></div>
        <span class="id">ID ${esc(u.userId)}</span>
      </div>
    `).join("");
  } catch (e) {
    el.innerHTML = `<div class="empty error-box">Could not load users.<br><small>${esc(e.message)}</small></div>`;
  }
}

async function addUser(event) {
  event?.preventDefault();
  const button = $("addUserBtn");
  const name = $("userName").value.trim();
  if (name.length < 3) return setResult("userFormResult", "User name must be at least 3 characters.", false);

  const body = { userName: name, gender: $("gender").value, userStatus: $("userStatus").value };
  setBusy(button, true, "Saving user…");
  try {
    const result = await request("/addUser", { method: "POST", body: JSON.stringify(body) });
    setResult("userFormResult", result || "User added", true);
    $("userName").value = "";
    await loadUsers();
  } catch (e) {
    setResult("userFormResult", "Error: " + e.message, false);
  } finally { setBusy(button, false); }
}

async function addBook(event) {
  event?.preventDefault();
  const button = $("addBookBtn");
  const body = {
    bookName: $("bookName").value.trim(),
    writerName: $("writerName").value.trim(),
    sellingPrice: Number($("sellingPrice").value),
    stockQuantity: Number($("stockQuantity").value),
    category: $("category").value
  };
  if (body.bookName.length < 3 || body.writerName.length < 3) return setResult("bookFormResult", "Book name and writer must be at least 3 characters.", false);
  if (!Number.isInteger(body.sellingPrice) || body.sellingPrice < 0) return setResult("bookFormResult", "Enter a valid whole-number price.", false);
  if (!Number.isInteger(body.stockQuantity) || body.stockQuantity < 0) return setResult("bookFormResult", "Enter a valid stock quantity.", false);

  setBusy(button, true, "Saving book…");
  try {
    const result = await request("/addBook", { method: "POST", body: JSON.stringify(body) });
    setResult("bookFormResult", result || "Book added", true);
    ["bookName", "writerName", "sellingPrice", "stockQuantity"].forEach(id => $(id).value = "");
    await loadBooks();
  } catch (e) {
    setResult("bookFormResult", "Error: " + e.message, false);
  } finally { setBusy(button, false); }
}

async function updateUser(event) {
  event?.preventDefault();
  const id = Number($("updateUserId").value);
  const name = $("updateUserName").value.trim();
  if (!id) return setResult("updateResult", "Select a user first.", false);
  if (name.length < 3) return setResult("updateResult", "User name must be at least 3 characters.", false);
  try {
    const result = await request(`/updateUser/${id}`, { method: "POST", body: JSON.stringify({ userName: name, gender: $("updateGender").value, userStatus: $("updateStatus").value }) });
    setResult("updateResult", result || "User updated", true);
    await loadUsers();
  } catch (e) { setResult("updateResult", "Error: " + e.message, false); }
}

async function deleteUser() {
  const id = Number($("deleteUserId").value);
  if (!id) return setResult("deleteResult", "Select a user first.", false);
  if (!confirm("Delete this user?")) return;
  try {
    const result = await request(`/deleteUser/${id}`, { method: "DELETE" });
    setResult("deleteResult", result || "User deleted", true);
    await loadUsers();
  } catch (e) { setResult("deleteResult", "Error: " + e.message, false); }
}

async function addToCart(event) {
  event?.preventDefault();
  const bookId = Number($("cartBookId").value);
  const userId = Number($("cartUserId").value);
  const quantity = Number($("cartQuantity").value);
  if (!bookId || !userId || !Number.isInteger(quantity) || quantity < 1) return setResult("cartResult", "Select a customer, book, and quantity of at least 1.", false);
  try {
    const result = await request(`/manageCart/${bookId}/${userId}/${quantity}`, { method: "POST" });
    setResult("cartResult", result || "Item added to cart", true);
  } catch (e) { setResult("cartResult", "Error: " + e.message, false); }
}

async function placeOrder(event) {
  event?.preventDefault();
  const cartId = Number($("cartId").value);
  if (!cartId) return setResult("orderResult", "Enter the cart ID returned/created by your backend.", false);
  try {
    const result = await request(`/placeYourOrder/${cartId}`, { method: "POST" });
    setResult("orderResult", result || "Order placed", true);
  } catch (e) { setResult("orderResult", "Error: " + e.message, false); }
}

function addBubble(text, type) {
  const wrap = $("chatMessages");
  const div = document.createElement("div");
  div.className = `bubble ${type}`;
  div.textContent = text;
  wrap.appendChild(div);
  wrap.scrollTop = wrap.scrollHeight;
  return div;
}

function updateChatMode() {
  $("customerIdWrap").classList.toggle("hidden", currentChatMode !== "customer");
  $("ragNotice").classList.toggle("hidden", currentChatMode !== "rag");
  $("chatHelp").textContent = currentChatMode === "general"
    ? "General AI assistant using your /GeneralChat endpoint."
    : currentChatMode === "customer"
      ? "Customer AI using /customer/{id}/askAi and the customer's memory/context."
      : "Your current Java project has the RAG service, but no RAG controller endpoint. It is shown for reference and is disabled until you add that endpoint.";
  $("sendChatBtn").disabled = currentChatMode === "rag";
}

async function sendChat() {
  if (currentChatMode === "rag") return;
  const input = $("chatInput");
  const msg = input.value.trim();
  if (!msg) return;
  input.value = "";
  addBubble(msg, "user");
  const waiting = addBubble("Thinking…", "assistant");
  try {
    let path;
    if (currentChatMode === "general") {
      path = `/GeneralChat?msg=${encodeURIComponent(msg)}`;
    } else {
      const id = Number($("chatCustomerId").value);
      if (!id) throw new Error("Select a customer first.");
      path = `/customer/${id}/askAi?msg=${encodeURIComponent(msg)}`;
    }
    waiting.textContent = await request(path);
  } catch (e) { waiting.textContent = "Error: " + e.message; }
}

function wire() {
  $("addUserForm").addEventListener("submit", addUser);
  $("addBookForm").addEventListener("submit", addBook);
  $("updateUserForm").addEventListener("submit", updateUser);
  $("deleteUserBtn").addEventListener("click", deleteUser);
  $("cartForm").addEventListener("submit", addToCart);
  $("orderForm").addEventListener("submit", placeOrder);
  $("refreshBooks").addEventListener("click", loadBooks);
  $("refreshUsers").addEventListener("click", loadUsers);
  $("sendChatBtn").addEventListener("click", sendChat);
  $("chatInput").addEventListener("keydown", e => { if (e.key === "Enter" && !e.shiftKey) { e.preventDefault(); sendChat(); } });
  $("updateUserId").addEventListener("change", () => {
    const u = users.find(x => String(x.userId) === $("updateUserId").value);
    if (u) { $("updateUserName").value = u.userName; $("updateGender").value = u.gender; $("updateStatus").value = u.userStatus; }
  });
  document.querySelectorAll(".tab").forEach(btn => btn.addEventListener("click", () => {
    document.querySelectorAll(".tab").forEach(b => b.classList.remove("active"));
    btn.classList.add("active");
    currentChatMode = btn.dataset.mode;
    updateChatMode();
  }));
  updateChatMode();
}

document.addEventListener("DOMContentLoaded", async () => {
  wire();
  await checkApi();
  await Promise.all([loadBooks(), loadUsers()]);
});
