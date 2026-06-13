// =========================================================================
// 1. GATEWAY BASELINE CONFIGURATION
// =========================================================================
// Go to your VS Code PORTS tab, copy the Forwarded URL address link for Port 8080,
// right-click it, change port visibility to PUBLIC, and paste it here:
// Automatically detects whether you are running in your Codespace or live on Render!
const BASE_URL = window.location.origin;

// =========================================================================
// 2. CONSUMER ACCOUNT REGISTRATION SUBMITTER
// =========================================================================
const formConsumer = document.getElementById("formConsumer");
if (formConsumer) {
    formConsumer.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            name: document.getElementById("consumerName").value,
            email: document.getElementById("consumerEmail").value,
            password: document.getElementById("consumerPassword").value,
            userType: "CONSUMER",
            latitude: 3.1390, 
            longitude: 101.6869
        };

        await shipRegistrationRequest(payload);
    });
}

// =========================================================================
// 3. MERCHANT BUSINESS REGISTRATION SUBMITTER
// =========================================================================
const formBusiness = document.getElementById("formBusiness");
if (formBusiness) {
    formBusiness.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            name: document.getElementById("businessName").value,
            email: document.getElementById("businessEmail").value,
            password: document.getElementById("businessPassword").value,
            userType: "BUSINESS",
            latitude: parseFloat(document.getElementById("businessLat").value),
            longitude: parseFloat(document.getElementById("businessLng").value)
        };

        await shipRegistrationRequest(payload);
    });
}

// Helper for registration
async function shipRegistrationRequest(payload) {
    try {
        const response = await fetch(`${BASE_URL}/auth/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("🎉 Registration Successful! Redirecting to login view context...");
            window.location.href = "login.html";
        } else {
            const errorText = await response.text();
            alert(`❌ Registration Refused: ${errorText}`);
        }
    } catch (error) {
        console.error("Connection Error:", error);
        alert("❌ Failed to reach backend engine. Confirm Port 8080 is set to PUBLIC.");
    }
}

// =========================================================================
// 4. USER LOGIN SUBMITTER (Perfect match for login.html IDs)
// =========================================================================
const loginForm = document.getElementById("loginForm");
if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            email: document.getElementById("loginEmail").value,
            password: document.getElementById("loginPassword").value
        };

        try {
            const response = await fetch(`${BASE_URL}/auth/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const data = await response.json(); // Backend AuthResponse object
                
                // Save token and info into browser memory
                localStorage.setItem("token", data.token);
                localStorage.setItem("userEmail", data.email);
                localStorage.setItem("userType", data.userType);

                alert("🔑 Login successful! Entering dashboard...");
                
                // Route users contextually
                if (data.userType === "BUSINESS") {
                    window.location.href = "business-dashboard2.html";
                } else {
                    window.location.href = "home.html.html";
                }
            } else {
                const errorText = await response.text();
                alert(`❌ Login Failed: ${errorText}`);
            }
        } catch (error) {
            console.error("Login Connection Error:", error);
            alert("❌ Backend unreachable. Check if Spring Boot server is running and Port 8080 is PUBLIC!");
        }
    });
}