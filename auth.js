// Change this to match Sharvessh's local backend port if different
const AUTH_API_URL = "http://localhost:8080/api/auth"; 

document.addEventListener("DOMContentLoaded", () => {
    // ---- LOGIN FORM LOGIC ----
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            
            const email = loginForm.querySelector('input[type="email"]').value;
            const password = loginForm.querySelector('input[type="password"]').value;

            try {
                const response = await fetch(`${AUTH_API_URL}/login`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password })
                });

                const data = await response.json();

                if (response.ok) {
                    // Save JWT Token & User Data inside the browser storage
                    localStorage.setItem("token", data.token);
                    localStorage.setItem("userRole", data.role); // e.g., "CONSUMER" or "BUSINESS"
                    localStorage.setItem("userName", data.name);

                    // Redirect based on user type rule schema
                    if (data.role === "BUSINESS") {
                        window.location.href = "business-dashboard.html";
                    } else {
                        window.location.href = "home.html";
                    }
                } else {
                    alert(data.message || "Invalid credentials. Please try again.");
                }
            } catch (error) {
                console.error("Auth error:", error);
                alert("Could not connect to authentication server.");
            }
        });
    }

    // ---- REGISTRATION FORMS LOGIC ----
    const formConsumer = document.getElementById("formConsumer");
    if (formConsumer) {
        formConsumer.addEventListener("submit", async (e) => {
            e.preventDefault();
            handleRegistration(formConsumer, "CONSUMER");
        });
    }

    const formBusiness = document.getElementById("formBusiness");
    if (formBusiness) {
        formBusiness.addEventListener("submit", async (e) => {
            e.preventDefault();
            handleRegistration(formBusiness, "BUSINESS");
        });
    }
});

// Helper wrapper function to clean up registration requests
async function handleRegistration(formElement, role) {
    const inputs = formElement.getElementsByClassName("form-control");
    let payload = { type: role };

    // Dynamically collect basic inputs
    for (let input of inputs) {
        if (input.placeholder.includes("name") || input.placeholder.includes("Doe")) payload.name = input.value;
        if (input.type === "email") payload.email = input.value;
        if (input.type === "password") payload.password = input.value;
    }

    // Capture extra vendor configuration fields if role is business
    if (role === "BUSINESS") {
        payload.businessName = formElement.querySelector('input[placeholder="Ali\'s Mamak Stall"]').value;
        payload.contact = formElement.querySelector('input[type="tel"]').value;
        payload.latitude = parseFloat(formElement.querySelector('input[placeholder="3.1390"]').value);
        payload.longitude = parseFloat(formElement.querySelector('input[placeholder="101.6869"]').value);
    }

    try {
        const response = await fetch(`${AUTH_API_URL}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Account created successfully! Please log in.");
            window.location.href = "login.html";
        } else {
            const errData = await response.json();
            alert(errData.message || "Registration failed.");
        }
    } catch (error) {
        alert("Server error during registration.");
    }
}

// Global logout hook used across all dashboard headers
document.getElementById("logoutBtn")?.addEventListener("click", () => {
    localStorage.clear();
    window.location.href = "login.html";
});