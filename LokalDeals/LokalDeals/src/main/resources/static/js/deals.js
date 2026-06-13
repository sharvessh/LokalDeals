// Handles active data-mapping for index.html, saved-deals.html, and deal-detail.html
document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    
    // Auth Route Sentry Sieve
    if (!token && !window.location.pathname.includes("login.html") && !window.location.pathname.includes("register.html")) {
        window.location.href = "login.html";
        return;
    }

    // 1. ---- CONDITIONAL INJECTION: HOMEPAGE BROWSE (index.html) ----
    const dealsGrid = document.getElementById("dealsGrid");
    if (dealsGrid) {
        // Fallback default coordinates targeting Klang/UPM vicinity areas
        fetchNearbyDeals(3.1390, 101.6869, 5, "all");
    }

    // 2. ---- CONDITIONAL INJECTION: DEEP DIVE DETAILS VIEW (deal-detail.html) ----
    const dealTitleHeader = document.getElementById("dealTitleHeader");
    if (dealTitleHeader) {
        const urlParams = new URLSearchParams(window.location.search);
        const dealId = urlParams.get("id");

        if (dealId) {
            loadDealDeepDiveDetails(dealId);
            setupReviewSubmissionListener(dealId);
            setupBookmarkToggleListener(dealId);
        } else {
            window.location.href = "index.html";
        }
    }
});

// A. --- FETCH ALL NEARBY DEALS (index.html Grid Painter) ---
async function fetchNearbyDeals(lat, lng, radius, category) {
    const token = localStorage.getItem("token");
    try {
        const res = await fetch(`${BASE_URL}/deals/nearby?lat=${lat}&lng=${lng}&radius=${radius}&category=${category}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });
        const items = await res.json();
        const grid = document.getElementById("dealsGrid");
        grid.innerHTML = "";
        
        if (items.length === 0) {
            grid.innerHTML = `<div class="col-12 text-center my-5"><p class="text-muted fs-4">No active flash promotions inside this radius.</p></div>`;
            return;
        }

        items.forEach(deal => {
            grid.innerHTML += `
                <div class="col">
                    <div class="card h-100 deal-card shadow-sm position-relative bg-white border-0">
                        <span class="badge deal-badge px-3 py-2 rounded-pill fs-6">${deal.discountPercentage}% OFF</span>
                        <div class="card-body p-4 d-flex flex-column justify-content-between">
                            <div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="badge bg-warning text-dark fw-bold">${deal.category || 'F&B'}</span>
                                </div>
                                <h4 class="card-title fw-bold text-dark">${deal.title}</h4>
                                <p class="text-muted small">${deal.description}</p>
                            </div>
                            <div class="pt-3 border-top mt-4 d-flex justify-content-between align-items-center">
                                <span class="text-danger small fw-bold"><i class="bi bi-clock-history"></i> Live</span>
                                <a href="deal-detail.html?id=${deal.id}" class="btn btn-brand-outline px-3">View Details</a>
                            </div>
                        </div>
                    </div>
                </div>`;
        });
    } catch (err) { console.error("Error drawing nearby deals map entries:", err); }
}

// B. --- FETCH TARGETED DEAL BY ID (deal-detail.html Populator) ---
async function loadDealDeepDiveDetails(dealId) {
    const token = localStorage.getItem("token");
    try {
        const res = await fetch(`${BASE_URL}/deals/${dealId}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });
        
        if (!res.ok) throw new Error("Deal lookup error");
        const deal = await res.json();

        // Bind Data Fields to Layout Dom Elements
        document.getElementById("dealDiscountBadge").innerText = `${deal.discountPercentage}% OFF FLASH SPECIAL`;
        document.getElementById("dealTitleHeader").innerText = deal.title;
        document.getElementById("dealDescriptionText").innerText = deal.description;
        document.getElementById("dealCategoryText").innerText = deal.category || "F&B";
        document.getElementById("dealExpiryText").innerText = `Expires: ${new Date(deal.expiryDate).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}`;
        
        // Populating nested merchant details if present in your backend response schema
        if (deal.business) {
            document.getElementById("bizNameHeader").innerHTML = `<i class="bi bi-shop me-2"></i>${deal.business.name}`;
            document.getElementById("bizPhoneText").innerText = deal.business.contact || "--";
            document.getElementById("callVendorBtn").href = `tel:${deal.business.contact}`;
        }
        
        // Fetch and draw associated business reviews
        if (deal.businessId) {
            loadBusinessReviewsTimeline(deal.businessId);
        }
    } catch (err) {
        console.error("Deep-dive payload mapping failed:", err);
    }
}

// C. --- LOAD REVIEWS (Render Review Lists) ---
async function loadBusinessReviewsTimeline(businessId) {
    try {
        const res = await fetch(`${BASE_URL}/reviews/business/${businessId}`);
        const reviews = await res.json();
        const container = document.getElementById("reviewsListContainer");
        container.innerHTML = "";

        if (reviews.length === 0) {
            container.innerHTML = `<p class="text-muted small">No reviews written for this vendor yet. Be the first!</p>`;
            return;
        }

        reviews.forEach(rev => {
            const starString = "⭐".repeat(rev.rating);
            container.innerHTML += `
                <div class="d-flex mb-3 border-bottom pb-3">
                    <div class="fs-2 text-secondary me-3"><i class="bi bi-person-circle"></i></div>
                    <div>
                        <div class="d-flex align-items-center mb-1">
                            <h6 class="fw-bold text-dark mb-0 me-2">Verified Customer</h6>
                            <span class="small text-warning">${starString}</span>
                        </div>
                        <p class="text-muted small mb-0">${rev.comment || rev.text || ''}</p>
                    </div>
                </div>`;
        });
    } catch (err) { console.error("Error retrieving reviews timeline:", err); }
}

// D. --- POST REVIEWS ACTION ---
function setupReviewSubmissionListener(dealId) {
    const form = document.getElementById("reviewSubmitForm");
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const token = localStorage.getItem("token");
        
        const payload = {
            dealId: parseInt(dealId),
            rating: parseInt(document.getElementById("reviewRating").value),
            comment: document.getElementById("reviewComment").value
        };

        try {
            const res = await fetch(`${BASE_URL}/reviews`, {
                method: "POST",
                headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                alert("Review submitted successfully! ⭐");
                form.reset();
                window.location.reload();
            }
        } catch (err) { alert("Could not submit customer review evaluation."); }
    });
}

// E. --- BOOKMARK FLIP TRIGGER HANDLER (saved-deals.html Connector) ---
function setupBookmarkToggleListener(dealId) {
    const saveBtn = document.getElementById("saveDealBtn");
    saveBtn.addEventListener("click", async () => {
        const token = localStorage.getItem("token");
        try {
            const res = await fetch(`${BASE_URL}/saved-deals`, {
                method: "POST",
                headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
                body: JSON.stringify({ dealId: parseInt(dealId) })
            });

            if (res.ok) {
                saveBtn.className = "btn btn-danger btn-sm fw-bold";
                saveBtn.innerHTML = `<i class="bi bi-heart-fill"></i> Saved!`;
                alert("Deal saved to bookmarks folder! ❤️");
            }
        } catch (err) { console.error("Error setting toggle bookmark state:", err); }
    });
}

// Global Single Logout Trigger Function
document.getElementById("logoutBtn")?.addEventListener("click", () => {
    localStorage.clear();
    window.location.href = "login.html";
});