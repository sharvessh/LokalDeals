// Handles merchant deal creation workflows inside post-deal.html
document.addEventListener("DOMContentLoaded", () => {
    const postDealForm = document.getElementById("postDealForm");
    if (postDealForm) {
        postDealForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const token = localStorage.getItem("token");
            const payload = {
                title: postDealForm.querySelector('input[placeholder*="Nasi Lemak"]').value,
                description: postDealForm.querySelector('textarea').value,
                discountPercentage: parseFloat(postDealForm.querySelector('input[type="number"]').value),
                category: postDealForm.querySelector('select').value,
                expiryDate: postDealForm.querySelector('input[type="datetime-local"]').value
            };

            try {
                const response = await fetch(`${BASE_URL}/deals`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
                    body: JSON.stringify(payload)
                });
                if (response.ok) {
                    alert("Deal broadcasted! 🚀");
                    window.location.href = "business-dashboard.html";
                }
            } catch (err) { alert("Error updating merchant dashboard catalog."); }
        });
    }
});