// =============================
// Welcome User
// =============================

const user = JSON.parse(localStorage.getItem("user"));

if (user) {

    document.getElementById("welcomeText").textContent =
        `${user.name} 👋`;

    // Update Profile Name
    const profileName = document.getElementById("profileName");

    if (profileName) {

        profileName.textContent = user.name;

    }

    // Update Email (if available)
    const profileEmail = document.getElementById("profileEmail");

    if (profileEmail && user.email) {

        profileEmail.textContent = user.email;

    }

}


// =============================
// Sidebar Toggle
// =============================

const menuToggle = document.getElementById("menuToggle");
const dashboard = document.querySelector(".dashboard");

if (menuToggle) {

    menuToggle.addEventListener("click", () => {

        dashboard.classList.toggle("collapsed");

    });

}


// =============================
// Profile Dropdown
// =============================

const profileBtn = document.getElementById("profileBtn");
const profileDropdown = document.getElementById("profileDropdown");

if (profileBtn && profileDropdown) {

    profileBtn.addEventListener("click", (e) => {

    e.stopPropagation();

    // Close notification dropdown
    if(notificationDropdown){
        notificationDropdown.classList.remove("show");
    }

    // Toggle profile dropdown
    profileDropdown.classList.toggle("show");

});

    document.addEventListener("click", () => {

    if(profileDropdown){
        profileDropdown.classList.remove("show");
    }

    if(notificationDropdown){
        notificationDropdown.classList.remove("show");
    }

});

    profileDropdown.addEventListener("click", (e) => {

        e.stopPropagation();

    });

}
// =============================
// Notification Dropdown
// =============================

const notificationBtn = document.getElementById("notificationBtn");
const notificationDropdown = document.getElementById("notificationDropdown");

if (notificationBtn && notificationDropdown) {

    notificationBtn.addEventListener("click", (e) => {

        e.stopPropagation();

        // Close profile dropdown if open
        profileDropdown.classList.remove("show");

        // Toggle notification dropdown
        notificationDropdown.classList.toggle("show");

    });

    notificationDropdown.addEventListener("click", (e) => {

        e.stopPropagation();

    });

}


// =============================
// My Profile
// =============================

const myProfile = document.getElementById("myProfile");

if (myProfile) {

    myProfile.addEventListener("click", () => {

        alert("Profile Page Coming Soon!");

    });

}


// =============================
// Logout
// =============================

function logoutUser() {

    localStorage.removeItem("user");

    window.location.href = "login.html";

}

const sidebarLogout = document.getElementById("sidebarLogout");
const dropdownLogout = document.getElementById("dropdownLogout");

if (sidebarLogout) {

    sidebarLogout.addEventListener("click", logoutUser);

}

if (dropdownLogout) {

    dropdownLogout.addEventListener("click", logoutUser);

}