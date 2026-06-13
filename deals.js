const DEALS_API_URL = "http://localhost:8080/api/deals";

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    
    // Safety Guardrail: Send unauthenticated users back to login
    if (!token && !window.location.pathname.includes("login.html") && !window.location.pathname.includes("register.html")) {
        window.location.href = "login.html";
        return;
    }

    // 1. ---- SUBMIT NEW DEAL (Runs on post-deal.html) ----
    const postDealForm = document.getElementById("postDealForm");
    if (postDealForm) {
        postDealForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const dealPayload = {
                title: postDealForm.querySelector('input[placeholder*="Nasi Lemak"]').value,
                description: postDealForm.querySelector('textarea').value,
                discountPercentage: parseFloat(postDealForm.querySelector('input[type="number"]').value),
                category: postDealForm.querySelector('select').value,
                expiryDate: postDealForm.querySelector('input[type="datetime-local"]').value
            };

            try {
                const response = await fetch(DEALS_API_URL, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}` // Secure routing pass
                    },
                    body: JSON.stringify(dealPayload)
                });

                if (response.ok) {
                    alert("Flash Deal Broadcasted Live! 🚀");
                    window.location.href = "business-dashboard.html";
                } else {
                    alert("Failed to publish flash promotion.");
                }
            } catch (error) {
                alert("Connection error trying to broadcast deal.");
            }
        });
    }

    // 2. ---- LOAD NEARBY DEALS (Runs on consumer discovery home.html) ----
    const dealsGrid = document.getElementById("dealsGrid");
    if (dealsGrid) {
        // Mock current user coordinates (Klang/KL coordinates)
        const userLat = 3.1390;
        const userLng = 101.6869;
        loadNearbyDeals(userLat, userLng, 5, "all");
    }
});

// Async consumer fetch wrapper
async function loadNearbyDeals(lat, lng, radius, category) {
    const dealsGrid = document.getElementById("dealsGrid");
    const token = localStorage.getItem("token");

    try {
        // Calls Sharvessh's geospatial Haversine query engine endpoint
        const response = await fetch(`${DEALS_API_URL}/nearby?lat=${lat}&lng=${lng}&radius=${radius}&category=${category}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        const deals = await response.json();
        dealsGrid.innerHTML = ""; // Clear existing placeholder HTML cards

        if (deals.length === 0) {
            dealsGrid.innerHTML = `<div class="col-12 text-center my-5"><p class="text-muted fs-4">No active flash promotions inside this radius.</p></div>`;
            return;
        }

        // Dynamically loop and append cards straight into your grid
        deals.forEach(deal => {
            dealsGrid.innerHTML += `
                <div class="col">
                    <div class="card h-100 deal-card shadow-sm position-relative">
                        <span class="badge deal-badge px-3 py-2 rounded-pill fs-6">${deal.discountPercentage}% OFF</span>
                        <div class="card-body d-flex flex-column justify-content-between p-4">
                            <div>
                                <h4 class="card-title fw-bold text-dark mb-2">${deal.title}</h4>
                                <p class="card-text text-muted small">${deal.description}</p>
                            </div>
                            <div class="mt-4 pt-3 border-top d-flex align-items-center justify-content-between">
                                <div class="text-danger fw-bold small"><i class="bi bi-clock-history"></i> Live!</div>
                                <a href="deal-detail.html?id=${deal.id}" class="btn btn-brand-outline px-3">View Details</a>
                            </div>
                        </div>
                    </div>
                </div>`;
        });
    } catch (err) {
        console.error("Could not fetch nearby deals engine:", err);
    }
}