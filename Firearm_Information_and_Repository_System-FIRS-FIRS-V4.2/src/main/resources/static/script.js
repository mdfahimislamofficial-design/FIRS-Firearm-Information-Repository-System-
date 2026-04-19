// ------------------------------------------------------------
// FIRS Common JavaScript
// - Master inventory (shared across all roles)
// - Product catalog (synced from master)
// - Cart management
// - Backend API calls for login, register, orders
// - Dynamic navigation
// ------------------------------------------------------------

const productCatalog = [
    // Handguns
    { id: 'g19', name: 'Glock 19 Gen5', brand: 'Glock', category: 'Handgun', caliber: '9mm', price: 649, rating: 4.8, image: './resources/images/glock_19.png', spec: '9mm · 15+1 · 4.02" barrel', restricted: false },
    { id: 'p320', name: 'Sig Sauer P320', brand: 'Sig Sauer', category: 'Handgun', caliber: '9mm', price: 629, rating: 5.0, image: './resources/images/sig_sauer.png', spec: '9mm · 17+1 · 119mm barrel · 710g unloaded', restricted: false },
    { id: 'm1911', name: 'M1911', brand: 'Colt', category: 'Handgun', caliber: '.45 ACP', price: 549, rating: 4.6, image: './resources/images/m1911.png', spec: '.45 ACP · 7+1 · 216mm barrel · 1050g unloaded', restricted: false },
    { id: 'czp10c', name: 'CZ P-10C', brand: 'CZ', category: 'Handgun', caliber: '9mm', price: 499, rating: 4.5, image: './resources/images/czp10c.png', spec: '9mm · 15+1 · 4.02" barrel', restricted: false },
    { id: 'g17', name: 'Glock 17 Gen5', brand: 'Glock', category: 'Handgun', caliber: '9mm', price: 599, rating: 5.0, image: './resources/images/glock17.png', spec: '9mm · 17+1 · 114mm barrel · 705g unloaded', restricted: false },
    { id: 'p365', name: 'Sig Sauer P365', brand: 'Sig Sauer', category: 'Handgun', caliber: '9mm', price: 629, rating: 4.8, image: './resources/images/sigsauer365.png', spec: '9mm · 10+1 · 3.1" barrel', restricted: false },
    { id: 'shield', name: 'S&W M&P Shield', brand: 'Smith & Wesson', category: 'Handgun', caliber: '9mm', price: 479, rating: 4.7, image: './resources/images/swmp.png', spec: '9mm · 8+1 · 3.1" barrel', restricted: false },
    { id: 'p07', name: 'CZ P-07', brand: 'CZ', category: 'Handgun', caliber: '9mm', price: 529, rating: 4.6, image: './resources/images/czp07.png', spec: '9mm · 15+1 · 3.8" barrel', restricted: false },
    // Revolvers
    { id: 'gp100', name: 'Ruger GP100', brand: 'Ruger', category: 'Revolver', caliber: '.357 Mag', price: 749, rating: 5.0, image: './resources/images/ruzergp100.png', spec: '.357 Mag · 6/7-Round · 4-6" barrel · 1050g unloaded', restricted: false },
    { id: 'python', name: 'Colt Python', brand: 'Colt', category: 'Revolver', caliber: '.357 Mag', price: 1499, rating: 5.0, image: './resources/images/coltpython.png', spec: '.357 Mag · 6-Round · 4-6" barrel · 1200g unloaded', restricted: false },
    { id: 'taurus856', name: 'Taurus 856', brand: 'Taurus', category: 'Revolver', caliber: '.38 Special', price: 349, rating: 4.4, image: './resources/images/tauras856.png', spec: '.38 Special · 6-Round · 2" Barrel', restricted: false },
    { id: 'bulldog', name: 'Charter Arms Bulldog', brand: 'Charter Arms', category: 'Revolver', caliber: '.44 Special', price: 399, rating: 4.3, image: './resources/images/charter.png', spec: '.44 Special · 5-Round · 2.5" Barrel', restricted: false },
    { id: 'sw686', name: 'S&W 686+', brand: 'Smith & Wesson', category: 'Revolver', caliber: '.357 Mag', price: 829, rating: 4.9, image: './resources/images/sw686.png', spec: '.357 Mag · 7-Round · 4" Barrel', restricted: false },
    { id: 'sp101', name: 'Ruger SP101', brand: 'Ruger', category: 'Revolver', caliber: '.357 Mag', price: 679, rating: 4.7, image: './resources/images/ruger.png', spec: '.357 Mag · 5-Round · 2.25" Barrel', restricted: false },
    // Rifles - restricted
    { id: 'ar15', name: 'AR-15 Platform', brand: 'Colt', category: 'Rifle', caliber: '5.56 NATO', price: 1299, rating: 4.7, image: './resources/images/ar15.png', spec: '5.56 NATO · 16" Barrel · Semi-Auto', restricted: true },
    { id: 'm4', name: 'M4 Carbine', brand: 'Colt', category: 'Rifle', caliber: '5.56 NATO', price: 1499, rating: 5.0, image: './resources/images/m4.png', spec: '5.56 NATO · 30-Round · 370mm barrel · 2.9-3.3kg unloaded', restricted: true },
    { id: 'ak47', name: 'AK-47', brand: 'Kalashnikov', category: 'Rifle', caliber: '7.62×39mm', price: 899, rating: 5.0, image: './resources/images/ak47.png', spec: '7.62×39mm · 30-Round · 415mm barrel · 3.8kg unloaded', restricted: true },
    { id: 'ddm4v7', name: 'Daniel Defense DDM4 V7', brand: 'Daniel Defense', category: 'Rifle', caliber: '5.56 NATO', price: 2099, rating: 4.9, image: './resources/images/ddm4.png', spec: '5.56 NATO · 16" Barrel · M-LOK', restricted: true },
    { id: 'scar17', name: 'FN SCAR 17S', brand: 'FN Herstal', category: 'Rifle', caliber: '.308 Win', price: 3499, rating: 4.9, image: './resources/images/scar.png', spec: '.308 Win · 16.25" Barrel · Semi-Auto', restricted: true },
    // Snipers - restricted
    { id: 'mrad', name: 'Barrett MRAD Mk22', brand: 'Barrett', category: 'Sniper', caliber: '.308 Win', price: 5999, rating: 5.0, image: './resources/images/mk22.png', spec: '.308 Win · Bolt-Action · 24" Barrel', restricted: true },
    { id: 'rem700', name: 'Remington 700', brand: 'Remington', category: 'Sniper', caliber: '.308 Win', price: 899, rating: 5.0, image: './resources/images/remington700.png', spec: '.308 Win · 3-5-Round · 510-660mm barrel · 3.2-3.6kg unloaded', restricted: true },
    { id: 'aiat', name: 'Accuracy International AT', brand: 'Accuracy International', category: 'Sniper', caliber: '.338 Lapua', price: 4799, rating: 4.9, image: './resources/images/at.png', spec: '.338 Lapua · Bolt-Action · 26" Barrel', restricted: true },
    { id: 'savage110', name: 'Savage 110 Elite', brand: 'Savage', category: 'Sniper', caliber: '6.5 Creedmoor', price: 1699, rating: 4.8, image: './resources/images/savage.png', spec: '6.5 Creedmoor · Bolt-Action · 24" Barrel', restricted: true }
];

// ---------- MASTER INVENTORY (shared across all roles) ----------
const MASTER_INVENTORY_KEY = 'firs_master_inventory';

function initializeMasterInventory() {
    if (!localStorage.getItem(MASTER_INVENTORY_KEY)) {
        // Exact quantities as requested
        const quantityMap = {
            // Snipers
            'mrad': 4, 'rem700': 7, 'aiat': 25, 'savage110': 31,
            // Rifles
            'ar15': 12, 'm4': 15, 'ak47': 18, 'ddm4v7': 64, 'scar17': 57,
            // Handguns
            'g19': 85, 'p320': 72, 'm1911': 66, 'czp10c': 91, 'g17': 110,
            'p365': 78, 'shield': 54, 'p07': 49,
            // Revolvers
            'gp100': 62, 'python': 33, 'taurus856': 120, 'bulldog': 47,
            'sw686': 58, 'sp101': 69
        };
        const seeded = productCatalog.map(p => ({
            ...p,
            qty: quantityMap[p.id] !== undefined ? quantityMap[p.id] : 10
        }));
        localStorage.setItem(MASTER_INVENTORY_KEY, JSON.stringify(seeded));
    }
    syncProductCatalogFromMaster();
}

function getMasterInventory() {
    const data = localStorage.getItem(MASTER_INVENTORY_KEY);
    return data ? JSON.parse(data) : [];
}

function saveMasterInventory(inventory) {
    localStorage.setItem(MASTER_INVENTORY_KEY, JSON.stringify(inventory));
    syncProductCatalogFromMaster();
    if (typeof window.updateAllViews === 'function') window.updateAllViews();
}

function syncProductCatalogFromMaster() {
    const master = getMasterInventory();
    productCatalog.length = 0;
    productCatalog.push(...master);
}

initializeMasterInventory();

const originalProductCatalog = productCatalog.map(p => ({ ...p, qty: 10 }));
function resetMasterInventoryToDefault() {
    const resetData = originalProductCatalog.map(p => ({ ...p, qty: 10 }));
    saveMasterInventory(resetData);
}
window.resetMasterInventoryToDefault = resetMasterInventoryToDefault;

function getProductById(id) { return productCatalog.find(p => p.id === id); }

function getAccessibleProducts(userRole) {
    if (userRole === 'customer')
        return productCatalog.filter(p => !p.restricted && (p.category === 'Handgun' || p.category === 'Revolver'));
    return productCatalog;
}

// ---------- SESSION MANAGEMENT ----------
function setCurrentUser(user) { localStorage.setItem('firs_user', JSON.stringify(user)); }
function getCurrentUser() { const u = localStorage.getItem('firs_user'); return u ? JSON.parse(u) : null; }
function logout() { localStorage.removeItem('firs_user'); window.location.href = 'index.html'; }

// ---------- DYNAMIC NAVIGATION ----------
function updateNavigation() {
    const user = getCurrentUser();
    const navCta = document.querySelector('.nav-cta');
    if (!navCta) return;

    if (user) {
        const initials = user.name.split(' ').map(n => n.charAt(0)).join('');
        let tierClass = user.role;
        let dropdownItems = `
            <a href="${user.role === 'admin' ? 'admin_dashboard.html' : user.role + '_view.html'}">Dashboard</a>
            <a href="user_profile.html">My Profile</a>
        `;
        if (user.role !== 'dealer') dropdownItems += `<a href="order_history.html">Order History</a>`;
        dropdownItems += `<a href="#" onclick="logout(); return false;">Sign Out</a>`;

        navCta.innerHTML = `
            <div class="nav-tier ${tierClass}">${user.role}</div>
            <div class="nav-cart" id="cartIcon" style="position:relative; width:40px; height:40px; display:flex; align-items:center; justify-content:center; background:var(--bg2); border:1px solid var(--border); cursor:pointer;">
                <img src="./resources/icons/icons_cart.png" alt="cart" width="20">
                <span id="cartCount" style="position:absolute; top:-6px; right:-6px; width:20px; height:20px; background:var(--red); border-radius:50%; font-size:11px; display:flex; align-items:center; justify-content:center; font-family:'Rajdhani';">0</span>
            </div>
            <div class="nav-user" style="display:flex; align-items:center; gap:10px; padding:6px 14px; background:var(--bg2); border:1px solid var(--border); cursor:pointer; position:relative;">
                <div style="width:28px; height:28px; background:var(--red); border-radius:50%; display:flex; align-items:center; justify-content:center; font-size:13px;">${initials}</div>
                <span style="font-family:'Rajdhani'; font-size:13px;">${user.name.split(' ')[0]}</span>
                <div class="dropdown-menu">${dropdownItems}</div>
            </div>
        `;
    } else {
        navCta.innerHTML = `
            <div class="nav-cart" id="cartIcon" style="display:flex; align-items:center; justify-content:center; width:40px; height:40px; background:var(--bg2); border:1px solid var(--border); cursor:pointer; margin-right:8px;">
                <img src="./resources/icons/icons_cart.png" alt="cart" width="20">
                <span id="cartCount" style="margin-left:4px; font-family:'Rajdhani';">0</span>
            </div>
            <a href="auth_page.html" class="btn-ghost">Sign In</a>
            <a href="auth_page.html" class="btn-red">Register</a>
        `;
    }

    const cartIcon = document.getElementById('cartIcon');
    if (cartIcon) cartIcon.addEventListener('click', toggleCart);
    updateCartUI();
}

// ---------- CART SYSTEM ----------
let cart = JSON.parse(localStorage.getItem('firs_cart')) || [];

function saveCart() { localStorage.setItem('firs_cart', JSON.stringify(cart)); updateCartUI(); }
function addToCart(productId) {
    const product = getProductById(productId);
    if (!product) return;
    const existing = cart.find(item => item.id === product.id);
    if (existing) existing.qty += 1;
    else cart.push({ id: product.id, name: product.name, price: product.price, spec: product.spec, qty: 1 });
    saveCart();
}
function removeFromCart(productId) { cart = cart.filter(item => item.id !== productId); saveCart(); }
function clearCart() { cart = []; saveCart(); }

function updateCartUI() {
    const container = document.getElementById('cart-items-container');
    const countSpan = document.getElementById('cartCount');
    const subtotalSpan = document.getElementById('cartSubtotal');
    const fflSpan = document.getElementById('cartFFL');
    const totalSpan = document.getElementById('cartTotal');
    const checkoutLink = document.getElementById('checkoutLink');

    const totalItems = cart.reduce((sum, i) => sum + i.qty, 0);
    if (countSpan) countSpan.innerText = totalItems;
    if (checkoutLink) checkoutLink.href = getCurrentUser() ? 'payment_page.html' : 'auth_page.html';
    if (!container) return;

    if (cart.length === 0) {
        container.innerHTML = '<div style="padding:20px; text-align:center; color:var(--ash2);">Your cart is empty</div>';
        if (subtotalSpan) subtotalSpan.innerText = '$0';
        if (fflSpan) fflSpan.innerText = '$0';
        if (totalSpan) totalSpan.innerText = '$0';
        return;
    }

    let subtotal = 0, html = '';
    cart.forEach(item => {
        subtotal += item.price * item.qty;
        const product = getProductById(item.id);
        const iconSrc = product?.image || './resources/icons/icons_handgun.png';
        html += `<div class="cart-item">
            <div class="cart-item-img"><img src="${iconSrc}" alt="item" width="30"></div>
            <div style="flex:1;">
                <div class="cart-item-name">${item.name}</div>
                <div class="cart-item-spec">${item.spec || ''} · Qty: ${item.qty}</div>
                <div class="cart-item-price">$${item.price}</div>
            </div>
            <div class="cart-item-remove" onclick="removeFromCart('${item.id}')"><img src="./resources/icons/icons_cross.png" alt="remove" width="16"></div>
        </div>`;
    });
    container.innerHTML = html;
    const fflFee = cart.length * 25, total = subtotal + fflFee;
    if (subtotalSpan) subtotalSpan.innerText = '$' + subtotal.toFixed(0);
    if (fflSpan) fflSpan.innerText = '$' + fflFee;
    if (totalSpan) totalSpan.innerText = '$' + total.toFixed(0);
}
function toggleCart() {
    document.getElementById('cart-drawer')?.classList.toggle('open');
    document.getElementById('cart-overlay')?.classList.toggle('open');
}

// ---------- BACKEND API CALLS ----------
const API_BASE = 'http://localhost:8080/api';

async function registerUser(userData) {
    const res = await fetch(`${API_BASE}/register`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(userData)
    });
    if (!res.ok) throw new Error(await res.text() || 'Registration failed');
    return res.json();
}
async function loginUser(email, password) {
    const res = await fetch(`${API_BASE}/login`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ email, password })
    });
    if (!res.ok) throw new Error(await res.text() || 'Invalid credentials');
    return res.json();
}
async function createOrder(orderData) {
    const res = await fetch(`${API_BASE}/orders`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(orderData)
    });
    if (!res.ok) throw new Error(await res.text() || 'Order failed');
    return res.json();
}
async function fetchUserOrders(userEmail) {
    const res = await fetch(`${API_BASE}/orders`);
    if (!res.ok) throw new Error('Failed to fetch orders');
    const orders = await res.json();
    return orders.filter(o => o.user.email === userEmail);
}

// ---------- AUTHENTICATION & REDIRECT ----------
async function authenticateAndRedirect(email, password) {
    try {
        const user = await loginUser(email, password);
        const allUsers = JSON.parse(localStorage.getItem('firs_all_users')) || [];
        const blocked = allUsers.find(u => u.email === email && u.blocked);
        if (blocked) { alert('You are blocked by the Admin. Access denied.'); return false; }
        setCurrentUser({ name: user.name, role: user.role, email: user.email });
        const redirects = { customer: 'customer_view.html', govt: 'govt_view.html', dealer: 'dealer_view.html', admin: 'admin_dashboard.html' };
        window.location.href = redirects[user.role] || 'index.html';
        return true;
    } catch (e) { alert(e.message); return false; }
}

// ---------- EXPOSE TO WINDOW ----------
window.addToCart = addToCart;
window.removeFromCart = removeFromCart;
window.clearCart = clearCart;
window.toggleCart = toggleCart;
window.productCatalog = productCatalog;
window.getAccessibleProducts = getAccessibleProducts;
window.getCurrentUser = getCurrentUser;
window.logout = logout;
window.updateNavigation = updateNavigation;
window.setCurrentUser = setCurrentUser;
window.authenticateAndRedirect = authenticateAndRedirect;
window.registerUser = registerUser;
window.loginUser = loginUser;
window.createOrder = createOrder;
window.fetchUserOrders = fetchUserOrders;
window.API_BASE = API_BASE;
window.getMasterInventory = getMasterInventory;
window.saveMasterInventory = saveMasterInventory;

document.addEventListener('DOMContentLoaded', updateNavigation);