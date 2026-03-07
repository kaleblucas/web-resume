(function () {
    'use strict';

    function observeFadeIns(container) {
        if (!window.fadeObserver) return;
        container.querySelectorAll('.fade-in').forEach(function (el) {
            window.fadeObserver.observe(el);
        });
    }

    function renderSkills(categories) {
        var grid = document.getElementById('skills-grid');
        if (!grid) return;

        categories.forEach(function (cat) {
            var card = document.createElement('div');
            card.className = 'skill-category fade-in';

            var heading = document.createElement('h3');
            heading.textContent = cat.name;
            card.appendChild(heading);

            var ul = document.createElement('ul');
            cat.skills.forEach(function (skill) {
                var li = document.createElement('li');
                li.textContent = skill;
                ul.appendChild(li);
            });
            card.appendChild(ul);

            grid.appendChild(card);
        });

        observeFadeIns(grid);
    }

    function renderExperience(experiences) {
        var grid = document.getElementById('projects-grid');
        if (!grid) return;

        experiences.forEach(function (exp) {
            var card = document.createElement('div');
            card.className = 'project-card fade-in';

            var meta = document.createElement('div');
            meta.className = 'project-card-meta';

            var tenure = document.createElement('span');
            tenure.className = 'project-tenure';
            tenure.textContent = exp.tenure;
            meta.appendChild(tenure);

            var location = document.createElement('span');
            location.className = 'project-location';
            location.textContent = exp.location;
            meta.appendChild(location);

            card.appendChild(meta);

            var heading = document.createElement('h3');
            heading.textContent = exp.company + ' ';

            var roleSpan = document.createElement('span');
            roleSpan.className = 'project-role';
            roleSpan.textContent = exp.role;
            heading.appendChild(roleSpan);

            card.appendChild(heading);

            var ul = document.createElement('ul');
            ul.className = 'project-bullets';
            exp.bullets.forEach(function (bullet) {
                var li = document.createElement('li');
                li.textContent = bullet;
                ul.appendChild(li);
            });
            card.appendChild(ul);

            var techDiv = document.createElement('div');
            techDiv.className = 'project-tech';
            exp.tech.forEach(function (t) {
                var span = document.createElement('span');
                span.textContent = t;
                techDiv.appendChild(span);
            });
            card.appendChild(techDiv);

            grid.appendChild(card);
        });

        observeFadeIns(grid);
    }

    document.addEventListener('DOMContentLoaded', function () {
        Promise.all([
            fetch('/api/skills').then(function (r) { return r.json(); }),
            fetch('/api/experience').then(function (r) { return r.json(); })
        ]).then(function (results) {
            renderSkills(results[0]);
            renderExperience(results[1]);
        }).catch(function (err) {
            console.error('Failed to load resume data:', err);
        });
    });
})();
