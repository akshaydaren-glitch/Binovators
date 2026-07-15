function getUsers() {
    const storedUsers = localStorage.getItem('binovatorUsers');
    if (!storedUsers) return [];
    try {
        return JSON.parse(storedUsers);
    } catch (error) {
        return [];
    }
}

function saveUsers(users) {
    localStorage.setItem('binovatorUsers', JSON.stringify(users));
}

function getCurrentUser() {
    const user = localStorage.getItem('binovatorCurrentUser');
    if (!user) return null;
    try {
        return JSON.parse(user);
    } catch (error) {
        return null;
    }
}

function setCurrentUser(user) {
    localStorage.setItem('binovatorCurrentUser', JSON.stringify(user));
}

function clearCurrentUser() {
    localStorage.removeItem('binovatorCurrentUser');
}

function showToast(message) {
    const existing = document.getElementById('page-toast');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.id = 'page-toast';
    toast.className = 'page-toast';
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3000);
}

function updateNavUserStatus() {
    const navRight = document.querySelector('nav .nav-right');
    if (!navRight) return;

    let statusEl = document.getElementById('user-status');
    if (!statusEl) {
        statusEl = document.createElement('div');
        statusEl.id = 'user-status';
        statusEl.className = 'user-status';
        navRight.insertBefore(statusEl, navRight.firstChild);
    }

    const user = getCurrentUser();
    if (user) {
        statusEl.innerHTML = `<span>Ingelogd als ${user.username}</span> <button type="button" id="logout-btn" class="btn secondary-btn small-btn">Uitloggen</button>`;
        const logoutBtn = document.getElementById('logout-btn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', function () {
                clearCurrentUser();
                updateNavUserStatus();
                window.location.href = 'Index.html';
            });
        }
        navRight.querySelectorAll('a[href="login.html"]').forEach(a => a.style.display = 'none');
    } else {
        statusEl.textContent = '';
        const logoutBtn = document.getElementById('logout-btn');
        if (logoutBtn) logoutBtn.remove();
        navRight.querySelectorAll('a[href="login.html"]').forEach(a => a.style.display = 'inline-block');
    }
}

function handleLoginForm() {
    const form = document.querySelector('form');
    if (!form) return;

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;

        const users = getUsers();
        const user = users.find(u => u.username.toLowerCase() === username.toLowerCase());

        if (!user || user.password !== password) {
            showToast('Gebruikersnaam of wachtwoord is onjuist');
            return;
        }

        setCurrentUser({
            username: user.username,
            email: user.email
        });

        showToast('Je bent ingelogd');
        setTimeout(() => {
            window.location.href = 'Index.html';
        }, 800);
    });
}

function handleSignupForm() {
    const form = document.querySelector('form');
    if (!form) return;

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        if (!username || !email || !password || !confirmPassword) {
            showToast('Vul alle velden in');
            return;
        }

        if (password !== confirmPassword) {
            showToast('Wachtwoorden komen niet overeen');
            return;
        }

        const users = getUsers();
        if (users.some(u => u.username.toLowerCase() === username.toLowerCase())) {
            showToast('Deze gebruikersnaam is al in gebruik');
            return;
        }

        const newUser = {
            username,
            email,
            password
        };

        users.push(newUser);
        saveUsers(users);
        setCurrentUser({ username, email });

        showToast('Account aangemaakt en ingelogd');
        setTimeout(() => {
            window.location.href = 'Index.html';
        }, 800);
    });
}

// Handle nav link clicks and dropdown toggles
document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelectorAll('nav a[href], nav ul a');
    navLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            navLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });

    updateNavUserStatus();

    if (document.body.classList.contains('login-page')) {
        handleLoginForm();
    }

    if (document.body.classList.contains('signup-page')) {
        handleSignupForm();
    }

    // Find A Pet -> toggles the pet-strip and flips the arrow
    const petToggle = document.getElementById('pet-toggle');
    const petStrip = document.getElementById('pet-strip');
    if (petToggle && petStrip) {
        petToggle.addEventListener('click', function (e) {
            e.stopPropagation();
            const isHidden = petStrip.hasAttribute('hidden');
            if (isHidden) {
                petStrip.removeAttribute('hidden');
                petToggle.setAttribute('aria-expanded', 'true');
            } else {
                petStrip.setAttribute('hidden', '');
                petToggle.setAttribute('aria-expanded', 'false');
            }
        });
        document.addEventListener('click', function (e) {
            if (!petStrip.contains(e.target) && e.target !== petToggle) {
                petStrip.setAttribute('hidden', '');
                petToggle.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // Others card dropdown toggle (card-level)
    const otherToggle = document.getElementById('other-toggle');
    const otherMenu = document.getElementById('other-menu');
    if (otherToggle && otherMenu) {
        otherToggle.addEventListener('click', function (e) {
            e.stopPropagation();
            const open = !otherMenu.hasAttribute('hidden');
            if (open) {
                otherMenu.setAttribute('hidden', '');
                otherToggle.setAttribute('aria-expanded', 'false');
            } else {
                otherMenu.removeAttribute('hidden');
                otherToggle.setAttribute('aria-expanded', 'true');
            }
        });
        document.addEventListener('click', function (e) {
            if (!otherMenu.contains(e.target) && e.target !== otherToggle) {
                otherMenu.setAttribute('hidden', '');
                otherToggle.setAttribute('aria-expanded', 'false');
            }
        });
        otherMenu.querySelectorAll('a').forEach(a => a.addEventListener('click', function () {
            otherMenu.setAttribute('hidden', '');
            otherToggle.setAttribute('aria-expanded', 'false');
        }));
    }

    // Top 'Other' dropdown in pet-strip
    const otherTopToggle = document.getElementById('other-top-toggle');
    const otherTopMenu = document.getElementById('other-top-menu');
    if (otherTopToggle && otherTopMenu) {
        otherTopToggle.addEventListener('click', function (e) {
            e.stopPropagation();
            const open = !otherTopMenu.hasAttribute('hidden');
            if (open) {
                otherTopMenu.setAttribute('hidden', '');
                otherTopToggle.setAttribute('aria-expanded', 'false');
            } else {
                otherTopMenu.removeAttribute('hidden');
                otherTopToggle.setAttribute('aria-expanded', 'true');
            }
        });
        document.addEventListener('click', function (e) {
            if (!otherTopMenu.contains(e.target) && e.target !== otherTopToggle) {
                otherTopMenu.setAttribute('hidden', '');
                otherTopToggle.setAttribute('aria-expanded', 'false');
            }
        });
        otherTopMenu.querySelectorAll('a').forEach(a => a.addEventListener('click', function () {
            otherTopMenu.setAttribute('hidden', '');
            otherTopToggle.setAttribute('aria-expanded', 'false');
        }));
    }
});

function showSection(event, sectionId) {
    if (event) event.preventDefault();
}
