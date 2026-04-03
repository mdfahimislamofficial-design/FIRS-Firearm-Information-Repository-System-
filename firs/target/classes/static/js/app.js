
 // ---------- PRODUCTS (11 items) ----------
        const products = [
            { id: 1, name: "Classic White Shirt", price: 50, type: "Knife", image: "https://images.unsplash.com/photo-1521334884684-d80222895322?w=400&auto=format" },
            { id: 2, name: "Black Slim Pants", price: 120, type: "Handguns", image: "https://images.unsplash.com/photo-1516826957135-700dedea698c?w=400&auto=format" },
            { id: 3, name: "Elegant Beige Shirt", price: 80, type: "Rifles", image: "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400&auto=format" },
            { id: 4, name: "Formal Navy Pants", price: 150, type: "Shotguns", image: "https://images.unsplash.com/photo-1593032465171-8b1c3c0b0d2e?w=400&auto=format" },
            { id: 5, name: "Luxury Black Shirt", price: 90, type: "Automatic Weapons", image: "https://images.unsplash.com/photo-1520975922203-b7d3a5e0c3d4?w=400&auto=format" },
            { id: 6, name: "Grey Casual Pants", price: 110, type: "Handguns", image: "https://images.unsplash.com/photo-1528701800489-20be3c1c89df?w=400&auto=format" },
            { id: 7, name: "Minimal White Tee", price: 40, type: "Rifles", image: "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400&auto=format" },
            { id: 8, name: "Premium Denim", price: 170, type: "Knife", image: "https://images.unsplash.com/photo-1514996937319-344454492b37?w=400&auto=format" },
            { id: 9, name: "Formal Cream Shirt", price: 75, type: "Handguns", image: "https://images.unsplash.com/photo-1520975682031-a7c2a6f1b7d8?w=400&auto=format" },
            { id: 10, name: "Luxury Trousers", price: 200, type: "Automatic Weapons", image: "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400&auto=format" },
            { id: 11, name: "Urban Fit Shirt", price: 65, type: "Shotguns", image: "https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?w=400&auto=format" }
        ];

        // ---------- STATE ----------
        let cart = [];
        let currentUser = null;

        // ---------- CART HELPERS ----------
        function saveCart() {
            localStorage.setItem("mediocore_cart", JSON.stringify(cart));
            updateCartBadge();
            renderCurrentCartIfNeeded();
            renderPaymentSummaryIfNeeded();
        }
        function loadCart() { const stored = localStorage.getItem("mediocore_cart"); cart = stored ? JSON.parse(stored) : []; updateCartBadge(); }
        function updateCartBadge() { const total = cart.reduce((sum, i) => sum + i.qty, 0); const badge = document.getElementById("cartCountNav"); if (badge) badge.innerText = total; }
        function addToCart(productId) { const existing = cart.find(i => i.id === productId); if (existing) existing.qty += 1; else cart.push({ id: productId, qty: 1 }); saveCart(); showToast("✓ Added to cart"); }
        function updateQty(productId, newQty) { if (newQty <= 0) cart = cart.filter(i => i.id !== productId); else { const item = cart.find(i => i.id === productId); if (item) item.qty = newQty; } saveCart(); }
        function removeItem(productId) { cart = cart.filter(i => i.id !== productId); saveCart(); }
        function getCartItemsWithDetails() { return cart.map(item => { const prod = products.find(p => p.id === item.id); return { ...item, name: prod.name, price: prod.price, image: prod.image }; }); }
        function getCartTotal() { return cart.reduce((sum, item) => { const prod = products.find(p => p.id === item.id); return sum + (prod.price * item.qty); }, 0); }

        // ---------- RENDER FUNCTIONS ----------
        function renderLandingFeatured() {
            const container = document.getElementById("landingFeaturedGrid");
            if (!container) return;
            const featured = products.slice(0, 4);
            container.innerHTML = featured.map(p => `
            <div class="product-card">
                <div class="img-box"><img src="${p.image}" alt="${p.name}" loading="lazy"></div>
                <div class="product-info">
                    <div class="product-title">${p.name}</div>
                    <div class="product-price">$${p.price}</div>
                    <button class="add-to-cart" data-id="${p.id}">Add to cart</button>
                </div>
            </div>
        `).join("");
            document.querySelectorAll("#landingFeaturedGrid .add-to-cart").forEach(btn => {
                btn.addEventListener("click", (e) => addToCart(parseInt(btn.dataset.id)));
            });
        }

        function renderShopGrid() {
            const container = document.getElementById("shopProductGrid");
            if (!container) return;
            let filtered = [...products];
            const maxPrice = parseInt(document.getElementById("priceFilter")?.value || 500);
            const priceSpan = document.getElementById("priceValue");
            if (priceSpan) priceSpan.innerText = maxPrice;
            filtered = filtered.filter(p => p.price <= maxPrice);
            const checkedTypes = Array.from(document.querySelectorAll(".type-filter:checked")).map(cb => cb.value);
            if (checkedTypes.length) filtered = filtered.filter(p => checkedTypes.includes(p.type));
            const sortValue = document.querySelector("input[name='sort']:checked")?.value;
            if (sortValue === "low") filtered.sort((a, b) => a.price - b.price);
            if (sortValue === "high") filtered.sort((a, b) => b.price - a.price);
            container.innerHTML = filtered.map(p => `
            <div class="product-card">
                <div class="img-box"><img src="${p.image}" alt="${p.name}" loading="lazy"></div>
                <div class="product-info">
                    <div class="product-title">${p.name}</div>
                    <div class="product-price">$${p.price}</div>
                    <button class="add-to-cart" data-id="${p.id}">Add to cart</button>
                </div>
            </div>
        `).join("");
            document.querySelectorAll("#shopProductGrid .add-to-cart").forEach(btn => {
                btn.addEventListener("click", (e) => addToCart(parseInt(btn.dataset.id)));
            });
        }

        function renderCartPage() {
            const container = document.getElementById("cartContainer");
            if (!container) return;
            const items = getCartItemsWithDetails();
            if (items.length === 0) {
                container.innerHTML = `<div class="alert">Your cart is empty. <a data-nav-shop style="cursor:pointer; text-decoration:underline;">Shop now</a></div>`;
                container.querySelectorAll("[data-nav-shop]").forEach(el => el.addEventListener("click", () => showPage("shop")));
                return;
            }
            let html = `<div style="overflow-x:auto;"><table class="cart-table"><thead><tr><th>Product</th><th>Price</th><th>Qty</th><th>Total</th><th></th></tr></thead><tbody>`;
            items.forEach(item => {
                const total = item.price * item.qty;
                html += `<tr>
                        <td style="display:flex; gap:12px; align-items:center;"><img src="${item.image}" width="50" height="60" style="object-fit:cover; border-radius:12px;"> ${item.name}</td>
                        <td>$${item.price}</td>
                        <td><input type="number" class="qty-input" data-id="${item.id}" value="${item.qty}" min="1" step="1"></td>
                        <td>$${total}</td>
                        <td><button class="remove-item" data-id="${item.id}">✕</button></td>
                      </tr>`;
            });
            html += `</tbody></table></div><div class="cart-summary"><p>Subtotal: <strong>$${getCartTotal()}</strong></p><button id="proceedToCheckoutBtn" class="btn">Proceed to checkout</button></div>`;
            container.innerHTML = html;
            document.querySelectorAll(".qty-input").forEach(inp => inp.addEventListener("change", (e) => updateQty(parseInt(inp.dataset.id), parseInt(inp.value))));
            document.querySelectorAll(".remove-item").forEach(btn => btn.addEventListener("click", (e) => removeItem(parseInt(btn.dataset.id))));
            const proceedBtn = document.getElementById("proceedToCheckoutBtn");
            if (proceedBtn) proceedBtn.addEventListener("click", () => {
                if (!currentUser) { alert("Please log in before checkout."); showPage("account"); return; }
                if (cart.length === 0) { alert("Cart empty."); return; }
                showPage("checkout");
                renderPaymentSummaryIfNeeded();
            });
        }

        function renderPaymentSummaryIfNeeded() {
            const summaryDiv = document.getElementById("paymentSummary");
            if (!summaryDiv) return;
            const items = getCartItemsWithDetails();
            if (items.length === 0) { summaryDiv.innerHTML = `<div class="alert">No items in cart.</div>`; return; }
            let html = `<div style="margin-bottom:20px;"><strong>Order summary</strong></div>`;
            items.forEach(it => { html += `<div style="display:flex; justify-content:space-between; margin:8px 0;"><span>${it.name} (x${it.qty})</span><span>$${it.price * it.qty}</span></div>`; });
            html += `<div style="font-weight:800; font-size:20px; margin-top:12px;">Total: $${getCartTotal()}</div>`;
            summaryDiv.innerHTML = html;
        }

        function renderCurrentCartIfNeeded() {
            if (document.getElementById("page-cart").classList.contains("active-page")) renderCartPage();
            if (document.getElementById("page-checkout").classList.contains("active-page")) renderPaymentSummaryIfNeeded();
        }

        // ---------- AUTHENTICATION (shared) ----------
        function updateLandingLoginUI() {
            const panel = document.getElementById("landingLoginPanel");
            const loggedPanel = document.getElementById("landingLoggedInPanel");
            const userNameSpan = document.getElementById("landingUserName");
            if (currentUser) {
                if (panel) panel.style.display = "none";
                if (loggedPanel) { loggedPanel.style.display = "block"; userNameSpan.innerText = currentUser.name || currentUser.email.split('@')[0]; }
            } else {
                if (panel) panel.style.display = "block";
                if (loggedPanel) loggedPanel.style.display = "none";
            }
        }
        function loginUser(email, pass, source = "account") {
            fetch("http://localhost:8080/api/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: pass
                })
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === "success") {
                    currentUser = data.user;
                    sessionStorage.setItem("mediocore_user", JSON.stringify(currentUser));

                    updateAccountUI();
                    updateLandingLoginUI();

                    document.getElementById("loginMsg").innerText = "Login success!";
                } else {
                    document.getElementById("loginMsg").innerText = "Invalid credentials.";
                }
            });
        }
        function signupUser(name, email, pass, dob, nid, comment, fileInput) {

            fetch("http://localhost:8080/api/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: pass,
                    dob: dob,
                    nid: nid,
                    comment: comment
                })
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === "success") {
                    document.getElementById("signupMsg").innerText = "Account created!";
                } else {
                    document.getElementById("signupMsg").innerText = data.message;
                }
            });
        }

        function logout() {
            currentUser = null;
            sessionStorage.removeItem("mediocore_user");
            updateAccountUI();
            updateLandingLoginUI();
            showPage("landing");
        }

        function updateAccountUI() {
            const loginPanel = document.getElementById("loginPanel");
            const signupPanel = document.getElementById("signupPanel");
            const logoutPanel = document.getElementById("logoutPanel");
            if (currentUser) {
                if (loginPanel) loginPanel.style.display = "none";
                if (signupPanel) signupPanel.style.display = "none";
                if (logoutPanel) logoutPanel.style.display = "block";
                const emailSpan = document.getElementById("currentUserEmail");
                if (emailSpan) emailSpan.innerText = currentUser.email;
            } else {
                if (loginPanel) loginPanel.style.display = "block";
                if (signupPanel) signupPanel.style.display = "none";
                if (logoutPanel) logoutPanel.style.display = "none";
            }
        }

        function initAuth() {
            const stored = sessionStorage.getItem("mediocore_user");
            if (stored) { try { currentUser = JSON.parse(stored); } catch (e) { currentUser = null; } }
            updateAccountUI();
            updateLandingLoginUI();
        }

        // ---------- PAYMENT GATEWAY ----------
        function handlePayment(e) {
            e.preventDefault();
            if (!currentUser) { document.getElementById("paymentMsg").innerHTML = "<span style='color:#b15e5e'>Login required.</span>"; showPage("account"); return; }
            if (cart.length === 0) { document.getElementById("paymentMsg").innerHTML = "<span style='color:#b15e5e'>Cart empty.</span>"; return; }
            const cardNum = document.getElementById("cardNumber").value.replace(/\s/g, "");
            const expiry = document.getElementById("cardExpiry").value;
            const cvc = document.getElementById("cardCvc").value;
            if (!cardNum || cardNum.length < 13 || !expiry.match(/^\d{2}\/\d{2}$/) || !cvc) {
                document.getElementById("paymentMsg").innerHTML = "<span style='color:#b15e5e'>Invalid card (demo: 4242424242424242, 12/28, 123)</span>";
                return;
            }
            document.getElementById("paymentMsg").innerHTML = "<span style='color:#2e7d64'>✓ Payment successful! Order placed.</span>";
            cart = [];
            saveCart();
            renderCartPage();
            renderPaymentSummaryIfNeeded();
            setTimeout(() => { alert("Thank you! Your order is confirmed."); showPage("landing"); }, 1500);
        }

        // ---------- NAVIGATION ----------
        function showPage(pageId) {
            document.querySelectorAll(".page").forEach(p => p.classList.remove("active-page"));
            const target = document.getElementById(`page-${pageId}`);
            if (target) target.classList.add("active-page");
            document.querySelectorAll(".nav-links a").forEach(link => {
                if (link.dataset.page === pageId) link.classList.add("active");
                else link.classList.remove("active");
            });
            if (pageId === "shop") renderShopGrid();
            if (pageId === "cart") renderCartPage();
            if (pageId === "checkout") { if (!currentUser) { alert("Please log in first."); showPage("account"); return; } renderPaymentSummaryIfNeeded(); }
            if (pageId === "account") updateAccountUI();
            if (pageId === "landing") { renderLandingFeatured(); updateLandingLoginUI(); }
        }

        function showToast(msg) {
            const toast = document.createElement("div");
            toast.innerText = msg;
            toast.style.position = "fixed";
            toast.style.bottom = "20px";
            toast.style.right = "20px";
            toast.style.background = "#1e1e2a";
            toast.style.color = "white";
            toast.style.padding = "10px 20px";
            toast.style.borderRadius = "40px";
            toast.style.zIndex = "999";
            toast.style.fontSize = "13px";
            document.body.appendChild(toast);
            setTimeout(() => toast.remove(), 1300);
        }

        // ---------- EVENT BINDINGS ----------
        function bindEvents() {
            document.querySelectorAll(".nav-links a").forEach(link => {
                link.addEventListener("click", () => { const page = link.dataset.page; if (page) showPage(page); });
            });
            document.querySelectorAll("[data-nav-shop], .logo[data-nav-landing]").forEach(el => {
                if (el.classList.contains("logo")) el.addEventListener("click", () => showPage("landing"));
                else el.addEventListener("click", () => showPage("shop"));
            });
            document.getElementById("priceFilter")?.addEventListener("input", () => renderShopGrid());
            document.querySelectorAll(".type-filter").forEach(cb => cb.addEventListener("change", () => renderShopGrid()));
            document.querySelectorAll("input[name='sort']").forEach(radio => radio.addEventListener("change", () => renderShopGrid()));

            // Account page forms
            document.getElementById("loginForm")?.addEventListener("submit", (e) => { e.preventDefault(); loginUser(document.getElementById("loginEmail").value, document.getElementById("loginPassword").value, "account"); });
            document.getElementById("signupForm")?.addEventListener("submit", (e) => { e.preventDefault(); signupUser(document.getElementById("signupName").value, document.getElementById("signupEmail").value, document.getElementById("signupPassword").value, document.getElementById("signupDob").value, document.getElementById("signupNid").value, document.getElementById("signupComment").value, document.getElementById("signupLicense")); });
            document.getElementById("showLoginBtn")?.addEventListener("click", () => { document.getElementById("loginPanel").style.display = "block"; document.getElementById("signupPanel").style.display = "none"; });
            document.getElementById("showSignupBtn")?.addEventListener("click", () => { document.getElementById("loginPanel").style.display = "none"; document.getElementById("signupPanel").style.display = "block"; });
            document.getElementById("logoutBtn")?.addEventListener("click", () => logout());
            document.getElementById("paymentForm")?.addEventListener("submit", handlePayment);

            // Landing page login form
            const landingLoginForm = document.getElementById("landingLoginForm");
            if (landingLoginForm) {
                landingLoginForm.addEventListener("submit", (e) => {
                    e.preventDefault();
                    const email = document.getElementById("landingLoginEmail").value;
                    const pass = document.getElementById("landingLoginPassword").value;
                    loginUser(email, pass, "landing");
                });
            }
            const landingLogoutBtn = document.getElementById("landingLogoutBtn");
            if (landingLogoutBtn) landingLogoutBtn.addEventListener("click", () => logout());

            // Link from landing login card to account page
            const createAccountLink = document.querySelector("#landingLoginSection a[data-page='account']");
            if (createAccountLink) createAccountLink.addEventListener("click", () => showPage("account"));
        }

        // ---------- INIT ----------
        function init() {
            loadCart();
            initAuth();
            bindEvents();
            renderShopGrid();
            renderLandingFeatured();
            showPage("landing");
        }
        init();
