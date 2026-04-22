const toast = document.getElementById("toast");

function showToast(message, isError = false) {
  toast.textContent = message;
  toast.style.background = isError ? "#7f1d1d" : "#0f172a";
  toast.classList.add("show");
  window.setTimeout(() => toast.classList.remove("show"), 2000);
}

function formatResult(targetId, data) {
  document.getElementById(targetId).textContent = JSON.stringify(data, null, 2);
}

async function request(path, options = {}) {
  const response = await fetch(path, options);
  const contentType = response.headers.get("content-type") || "";
  const payload = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message = typeof payload === "object" && payload?.error
      ? payload.error
      : `Request failed with status ${response.status}`;
    throw new Error(message);
  }

  return payload;
}

function toIsoFromDatetimeLocal(value) {
  return new Date(value).toISOString().slice(0, 19);
}

document.getElementById("createUserForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const body = {
      name: document.getElementById("userName").value,
      email: document.getElementById("userEmail").value
    };
    const data = await request("/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    formatResult("usersResult", data);
    showToast("User created");
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("loadUsersBtn").addEventListener("click", async () => {
  try {
    const data = await request("/api/users");
    formatResult("usersResult", data);
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("createFlightForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const body = {
      flightNumber: document.getElementById("flightNumber").value,
      origin: document.getElementById("origin").value,
      destination: document.getElementById("destination").value,
      departureTime: toIsoFromDatetimeLocal(document.getElementById("departureTime").value),
      arrivalTime: toIsoFromDatetimeLocal(document.getElementById("arrivalTime").value),
      price: Number(document.getElementById("price").value),
      availableSeats: Number(document.getElementById("availableSeats").value)
    };
    const data = await request("/api/flights", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });
    formatResult("flightsResult", data);
    showToast("Flight created");
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("loadFlightsBtn").addEventListener("click", async () => {
  try {
    const data = await request("/api/flights");
    formatResult("flightsResult", data);
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("searchFlightsForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const origin = encodeURIComponent(document.getElementById("searchOrigin").value);
    const destination = encodeURIComponent(document.getElementById("searchDestination").value);
    const data = await request(`/api/flights/search?origin=${origin}&destination=${destination}`);
    formatResult("flightsResult", data);
    showToast("Flights loaded");
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("bookFlightForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const userId = document.getElementById("bookUserId").value;
    const flightId = document.getElementById("bookFlightId").value;
    const data = await request(`/api/bookings?userId=${userId}&flightId=${flightId}`, {
      method: "POST"
    });
    formatResult("bookingsResult", data);
    showToast("Booking created");
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("cancelBookingForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const bookingId = document.getElementById("cancelBookingId").value;
    const data = await request(`/api/bookings/${bookingId}/cancel`, {
      method: "PUT"
    });
    formatResult("bookingsResult", data);
    showToast("Booking cancelled");
  } catch (err) {
    showToast(err.message, true);
  }
});

document.getElementById("loadBookingsBtn").addEventListener("click", async () => {
  try {
    const data = await request("/api/bookings");
    formatResult("bookingsResult", data);
  } catch (err) {
    showToast(err.message, true);
  }
});
