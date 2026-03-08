// ─── Hamburger Menu ───────────────────────────────────────────────────────────
const navbar    = document.querySelector('.navbar');
const hamburger = document.querySelector('.hamburger');

function closeMenu() {
    navbar.classList.remove('nav-open');
    if (hamburger) hamburger.setAttribute('aria-expanded', 'false');
    document.body.style.overflow = '';
}

if (hamburger) {
    hamburger.addEventListener('click', function (e) {
        e.stopPropagation();
        const isOpen = navbar.classList.toggle('nav-open');
        hamburger.setAttribute('aria-expanded', String(isOpen));
        document.body.style.overflow = isOpen ? 'hidden' : '';
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') closeMenu();
    });
}

// ─── Tools Dropdown ───────────────────────────────────────────────────────────
const dropdown = document.querySelector('.nav-dropdown');
const toggle   = document.querySelector('.nav-dropdown-toggle');
const menu     = document.querySelector('.nav-dropdown-menu');

if (toggle && menu) {
    toggle.addEventListener('click', function (e) {
        e.stopPropagation();
        const isOpen = toggle.getAttribute('aria-expanded') === 'true';
        toggle.setAttribute('aria-expanded', String(!isOpen));
        menu.classList.toggle('open', !isOpen);
    });

    document.addEventListener('click', function () {
        toggle.setAttribute('aria-expanded', 'false');
        menu.classList.remove('open');
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            toggle.setAttribute('aria-expanded', 'false');
            menu.classList.remove('open');
            toggle.focus();
        }
    });
}

// ─── Smooth Scrolling ─────────────────────────────────────────────────────────
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const href = this.getAttribute('href');
        if (href === '#top') {
            window.scrollTo({ top: 0, behavior: 'smooth' });
            closeMenu();
            return;
        }
        const target = document.querySelector(href);
        if (target) {
            const navbarHeight = document.querySelector('.navbar').offsetHeight;
            const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - navbarHeight - 40;
            window.scrollTo({ top: targetPosition, behavior: 'smooth' });
        }
        closeMenu();
    });
});

// ─── Scroll Fade-in ───────────────────────────────────────────────────────────
window.fadeObserver = new IntersectionObserver(function(entries) {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
            window.fadeObserver.unobserve(entry.target);
        }
    });
}, {
    threshold: 0.12,
    rootMargin: '0px 0px -60px 0px'
});

document.querySelectorAll('.fade-in').forEach(el => window.fadeObserver.observe(el));

// ─── Hero Load Animation ──────────────────────────────────────────────────────
window.addEventListener('load', function() {
    document.body.classList.add('loaded');
    document.querySelectorAll('.hero .fade-in').forEach(el => {
        el.classList.add('visible');
    });
});

// ─── Nav Logo Visibility ──────────────────────────────────────────────────────
const navLogo     = document.querySelector('.nav-logo');
const heroSection = document.querySelector('.hero');

if (heroSection && navLogo) {
    const logoObserver = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                navLogo.classList.remove('logo-visible');
            } else {
                navLogo.classList.add('logo-visible');
            }
        });
    }, {
        threshold: 0.1
    });
    logoObserver.observe(heroSection);
}

// ─── Navbar Scroll Behaviour ──────────────────────────────────────────────────
window.addEventListener('scroll', function() {
    if (window.scrollY > 80) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
}, { passive: true });

// ─── Dark Mode Toggle ────────────────────────────────────────────────────────
(function () {
    const STORAGE_KEY = 'portfolio-dark-mode';

    function applyTheme(dark) {
        document.documentElement.classList.toggle('dark-mode', dark);
        document.querySelectorAll('.dark-mode-toggle').forEach(el => {
            el.textContent = dark ? 'light mode' : 'dark mode';
        });
    }

    // Apply persisted preference immediately
    const stored = localStorage.getItem(STORAGE_KEY);
    applyTheme(stored === 'true');

    document.addEventListener('click', function (e) {
        if (!e.target.classList.contains('dark-mode-toggle')) return;
        e.preventDefault();
        const isDark = document.documentElement.classList.toggle('dark-mode');
        localStorage.setItem(STORAGE_KEY, isDark);
        document.querySelectorAll('.dark-mode-toggle').forEach(el => {
            el.textContent = isDark ? 'light mode' : 'dark mode';
        });
        closeMenu();
    });
})();
