/* ─── JSON Formatter ──────────────────────────────────────────────────────────── */
(function () {
    'use strict';

    // ── DOM refs ──────────────────────────────────────────────────────────────────
    const input       = document.getElementById('jf-input');
    const output      = document.getElementById('jf-output');
    const errorPanel  = document.getElementById('jf-error-panel');
    const errorMsg    = document.getElementById('jf-error-msg');
    const statusDot   = document.getElementById('jf-status-indicator');
    const statusText  = document.getElementById('jf-status-text');
    const charCount   = document.getElementById('jf-char-count');
    const keyCount    = document.getElementById('jf-key-count');
    const depthChip   = document.getElementById('jf-depth');
    const treeWrap    = document.getElementById('jf-tree-wrap');
    const treeRoot    = document.getElementById('jf-tree');

    const pasteBtn    = document.getElementById('jf-paste-btn');
    const clearBtn    = document.getElementById('jf-clear-btn');
    const copyBtn     = document.getElementById('jf-copy-btn');
    const indent2Btn  = document.getElementById('jf-indent-2');
    const indent4Btn  = document.getElementById('jf-indent-4');
    const minifyBtn   = document.getElementById('jf-minify-btn');
    const expandAll   = document.getElementById('jf-expand-all');
    const collapseAll = document.getElementById('jf-collapse-all');

    // ── State ─────────────────────────────────────────────────────────────────────
    let parsed   = null;
    let indent   = 2;
    let minified = false;

    // ── Helpers ───────────────────────────────────────────────────────────────────
    function setStatus(state, text) {
        statusDot.className = 'jf-status-dot jf-status-' + state;
        statusText.textContent = text;
    }

    function showError(msg) {
        errorPanel.classList.remove('hidden');
        errorMsg.textContent = msg;
        setStatus('error', 'invalid json');
        output.innerHTML = '';
        treeWrap.classList.add('hidden');
        [keyCount, depthChip].forEach(el => el.classList.add('hidden'));
    }

    function hideError() {
        errorPanel.classList.add('hidden');
        errorMsg.textContent = '';
    }

    function countKeys(obj) {
        if (typeof obj !== 'object' || obj === null) return 0;
        let count = 0;
        if (Array.isArray(obj)) {
            obj.forEach(v => { count += 1 + countKeys(v); });
        } else {
            Object.keys(obj).forEach(k => { count += 1 + countKeys(obj[k]); });
        }
        return count;
    }

    function maxDepth(obj, d = 0) {
        if (typeof obj !== 'object' || obj === null) return d;
        const vals = Array.isArray(obj) ? obj : Object.values(obj);
        if (vals.length === 0) return d;
        return Math.max(...vals.map(v => maxDepth(v, d + 1)));
    }

    // ── Syntax highlighting ───────────────────────────────────────────────────────
    function syntaxHighlight(json) {
        // Escape HTML entities first so angle brackets in values don't break markup
        json = json
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');

        return json.replace(
            /("(\\u[a-fA-F0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
            function (match) {
                let cls = 'jf-num';
                if (/^"/.test(match)) {
                    cls = /:$/.test(match) ? 'jf-key' : 'jf-str';
                } else if (/true|false/.test(match)) {
                    cls = 'jf-bool';
                } else if (/null/.test(match)) {
                    cls = 'jf-null';
                }
                return '<span class="' + cls + '">' + match + '</span>';
            }
        );
    }

    // ── Core format ───────────────────────────────────────────────────────────────
    function process() {
        const raw = input.value.trim();
        charCount.textContent = input.value.length + ' chars';

        if (!raw) {
            setStatus('idle', 'ready');
            hideError();
            output.innerHTML = '';
            treeWrap.classList.add('hidden');
            [keyCount, depthChip].forEach(el => el.classList.add('hidden'));
            parsed = null;
            return;
        }

        try {
            parsed = JSON.parse(raw);
        } catch (e) {
            showError(e.message);
            parsed = null;
            return;
        }

        hideError();

        // Stats
        const keys  = countKeys(parsed);
        const depth = maxDepth(parsed);
        keyCount.textContent  = keys + ' keys';
        depthChip.textContent = 'depth ' + depth;
        [keyCount, depthChip].forEach(el => el.classList.remove('hidden'));

        renderOutput();
        renderTree(parsed);
        treeWrap.classList.remove('hidden');
        setStatus('ok', 'valid json');
    }

    function renderOutput() {
        if (parsed === null && !input.value.trim()) return;
        if (parsed === null) return;
        const str = minified
            ? JSON.stringify(parsed)
            : JSON.stringify(parsed, null, indent);
        // innerHTML renders the syntax-highlighted spans into the contenteditable div
        output.innerHTML = syntaxHighlight(str);
    }

    // ── Tree builder ──────────────────────────────────────────────────────────────
    function buildTreeNode(key, value, isRoot) {
        const li   = document.createElement('li');
        const node = document.createElement('span');
        node.className = 'jf-tree-node';

        const isObj = typeof value === 'object' && value !== null;
        const isArr = Array.isArray(value);
        const size  = isObj ? (isArr ? value.length : Object.keys(value).length) : 0;

        const toggle = document.createElement('span');
        toggle.className = 'jf-tree-toggle';

        const keyEl = document.createElement('span');
        keyEl.className = 'jf-tree-key';
        if (!isRoot) keyEl.textContent = isArr || typeof key === 'number'
            ? '[' + key + ']'
            : '"' + key + '"';

        const colon = document.createElement('span');
        colon.className = 'jf-tree-colon';
        if (!isRoot) colon.textContent = ': ';

        if (isObj) {
            node.classList.add('collapsible');
            toggle.textContent = '▾';

            const typeEl = document.createElement('span');
            typeEl.className = 'jf-tree-type';
            typeEl.textContent = isArr
                ? 'array[' + size + ']'
                : 'object{' + size + '}';

            node.append(toggle, keyEl, colon, typeEl);
            li.appendChild(node);

            const children = document.createElement('ul');
            children.className = 'jf-tree-children';

            const entries = isArr
                ? value.map((v, i) => [i, v])
                : Object.entries(value);

            entries.forEach(([k, v]) => {
                children.appendChild(buildTreeNode(k, v, false));
            });

            li.appendChild(children);

            node.addEventListener('click', function (e) {
                e.stopPropagation();
                const collapsed = children.classList.toggle('collapsed');
                toggle.textContent = collapsed ? '▸' : '▾';
            });
        } else {
            toggle.textContent = ' ';
            let valClass = 'jf-tree-value ';
            let display  = '';

            if (typeof value === 'string') {
                valClass += 'jf-str';
                display   = '"' + value + '"';
            } else if (typeof value === 'number') {
                valClass += 'jf-num';
                display   = String(value);
            } else if (typeof value === 'boolean') {
                valClass += 'jf-bool';
                display   = String(value);
            } else if (value === null) {
                valClass += 'jf-null';
                display   = 'null';
            }

            const valEl = document.createElement('span');
            valEl.className = valClass;
            valEl.textContent = display;

            node.append(toggle, keyEl, colon, valEl);
            li.appendChild(node);
        }

        return li;
    }

    function renderTree(data) {
        treeRoot.innerHTML = '';
        const ul = document.createElement('ul');
        ul.appendChild(buildTreeNode('root', data, true));
        treeRoot.appendChild(ul);
    }


    // ── Event bindings ────────────────────────────────────────────────────────────
    let debounceTimer;
    input.addEventListener('input', function () {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(process, 300);
    });

    pasteBtn.addEventListener('click', function () {
        pasteFromClipboard().then(function (text) {
            input.value = text;
            process();
        }).catch(function () {
            // Clipboard API unavailable (HTTP) — focus the input for manual Ctrl+V
            input.focus();
        });
    });

    clearBtn.addEventListener('click', function () {
        input.value = '';
        process();
        input.focus();
    });

    copyBtn.addEventListener('click', function () {
        const text = output.innerText.trim();
        if (!text) return;
        navigator.clipboard.writeText(text).then(function () {
            copyBtn.textContent = 'copied!';
            setTimeout(function () { copyBtn.textContent = 'copy'; }, 1500);
        }).catch(function () {
            const range = document.createRange();
            range.selectNodeContents(output);
            const sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
        });
    });

    function setIndent(n) {
        indent   = n;
        minified = false;
        [indent2Btn, indent4Btn, minifyBtn].forEach(b => b.classList.remove('active'));
        (n === 2 ? indent2Btn : indent4Btn).classList.add('active');
        renderOutput();
    }

    indent2Btn.addEventListener('click', () => setIndent(2));
    indent4Btn.addEventListener('click', () => setIndent(4));

    minifyBtn.addEventListener('click', function () {
        minified = !minified;
        minifyBtn.classList.toggle('active', minified);
        [indent2Btn, indent4Btn].forEach(b => b.classList.remove('active'));
        renderOutput();
    });


    expandAll.addEventListener('click', function () {
        treeRoot.querySelectorAll('.jf-tree-children').forEach(el => {
            el.classList.remove('collapsed');
        });
        treeRoot.querySelectorAll('.jf-tree-toggle').forEach(el => {
            if (el.textContent === '▸') el.textContent = '▾';
        });
    });

    collapseAll.addEventListener('click', function () {
        treeRoot.querySelectorAll('.jf-tree-children').forEach(el => {
            el.classList.add('collapsed');
        });
        treeRoot.querySelectorAll('.jf-tree-toggle').forEach(el => {
            if (el.textContent === '▾') el.textContent = '▸';
        });
    });

    // ── Init ──────────────────────────────────────────────────────────────────────
    indent2Btn.classList.add('active');
    setStatus('idle', 'ready');

})();
