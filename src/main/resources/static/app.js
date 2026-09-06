const API = '';
let currentChatMode = 'general';
let currentCartId = null;

const esc = s => String(s ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c]));

async function request(path, options={}) {
  const res = await fetch(API + path, options);
  const text = await res.text();
  if (!res.ok) throw new Error(text || `HTTP ${res.status}`);
  const type = res.headers.get('content-type') || '';
  if (type.includes('application/json')) return JSON.parse(text);
  return text;
}

async function checkApi(){
  try{ await request('/getBookList?page=0&size=1'); document.getElementById('apiStatus').textContent='● Backend online'; document.getElementById('apiStatus').classList.add('ok'); }
  catch(e){ document.getElementById('apiStatus').textContent='● Backend unavailable'; }
}

async function loadBooks(){
  const el=document.getElementById('books'); el.innerHTML='<div class="muted">Loading books…</div>';
  try{
    const data=await request('/getBookList?page=0&size=100&sort=bookName,asc');
    el.innerHTML=data.content.map(b=>`<article class="card book"><div class="category">${esc(b.category)}</div><h3>${esc(b.bookName)}</h3><div class="writer">by ${esc(b.writerName)}</div><div class="meta"><span>₹${esc(b.sellingPrice)}</span><span class="stock">Stock: ${esc(b.stockQuantity)} · ID ${esc(b.bookId)}</span></div></article>`).join('');
  }catch(e){el.innerHTML=`<div class="result">${esc(e.message)}</div>`}
}

async function loadUsers(){
  const el=document.getElementById('users'); el.innerHTML='<div class="muted">Loading users…</div>';
  try{
    const data=await request('/getUserList?page=0&size=100&sort=userName,asc');
    el.innerHTML=data.content.map(u=>`<div class="user-row"><div><strong>${esc(u.userName)}</strong><br><small>${esc(u.gender)} · ${esc(u.userStatus)}</small></div><small>ID ${esc(u.userId)} · Orders ${(u.order||[]).length}</small></div>`).join('');
  }catch(e){el.innerHTML=`<div class="result">${esc(e.message)}</div>`}
}

async function addBook(){
  const out=document.getElementById('bookFormResult');
  const body={bookName:bookName.value,writerName:writerName.value,sellingPrice:Number(sellingPrice.value),stockQuantity:Number(stockQuantity.value),category:category.value};
  try{out.textContent=await request('/addBook',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}); await loadBooks();}
  catch(e){out.textContent=e.message}
}

async function addUser(){
  const out=document.getElementById('userFormResult');
  const body={userName:userName.value,gender:gender.value,userStatus:userStatus.value};
  try{out.textContent=await request('/addUser',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}); await loadUsers();}
  catch(e){out.textContent=e.message}
}

async function addToCart(){
  const uid=Number(cartUserId.value), bid=Number(cartBookId.value), qty=Number(cartQty.value), out=document.getElementById('cartResult');
  try{await request(`/manageCart/${bid}/${uid}/${qty}`,{method:'POST'}); out.textContent='Item added successfully. Loading cart…'; await viewCart();}
  catch(e){out.textContent=e.message}
}

async function viewCart(){
  const uid=Number(cartUserId.value), out=document.getElementById('cartResult');
  try{
    const c=await request(`/cart/${uid}`);
    if(!c){currentCartId=null; out.textContent='No cart exists yet for this user.'; placeOrderBtn.disabled=true; return;}
    currentCartId=c.cartId;
    const items=(c.cartItem||[]).map(i=>`${i.book?.bookName || 'Book'} × ${i.quantity}`).join('\n');
    out.textContent=`Cart ID: ${c.cartId}\nCart value: ₹${c.cartValue}${items?'\n\n'+items:''}`;
    placeOrderBtn.disabled=c.cartValue<=0;
  }catch(e){out.textContent=e.message; placeOrderBtn.disabled=true}
}

async function placeOrder(){
  const out=document.getElementById('cartResult');
  if(!currentCartId) return;
  try{out.textContent=await request(`/placeYourOrder/${currentCartId}`,{method:'POST'}); placeOrderBtn.disabled=true; await loadUsers();}
  catch(e){out.textContent=e.message}
}

function addBubble(text,type){
  const wrap=document.getElementById('chatMessages'); const div=document.createElement('div'); div.className=`bubble ${type}`; div.textContent=text; wrap.appendChild(div); wrap.scrollTop=wrap.scrollHeight;
}

async function sendChat(){
  const input=document.getElementById('chatInput'); const msg=input.value.trim(); if(!msg)return; input.value=''; addBubble(msg,'user'); addBubble('Thinking…','assistant');
  const bubbles=document.querySelectorAll('#chatMessages .assistant'); const waiting=bubbles[bubbles.length-1];
  try{
    let path;
    if(currentChatMode==='general') path=`/GeneralChat?msg=${encodeURIComponent(msg)}`;
    else if(currentChatMode==='customer') path=`/customer/${Number(chatCustomerId.value)}/askAi?msg=${encodeURIComponent(msg)}`;
    else path=`/rag?msg=${encodeURIComponent(msg)}`;
    waiting.textContent=await request(path);
  }catch(e){waiting.textContent='Error: '+e.message}
}

document.querySelectorAll('.tab').forEach(btn=>btn.addEventListener('click',()=>{
  document.querySelectorAll('.tab').forEach(b=>b.classList.remove('active')); btn.classList.add('active'); currentChatMode=btn.dataset.mode;
  customerIdWrap.classList.toggle('hidden',currentChatMode!=='customer');
}));

document.getElementById('chatInput').addEventListener('keydown',e=>{if(e.key==='Enter')sendChat()});

checkApi(); loadBooks(); loadUsers();