/* ─── XML Formatter ─────────────────────────────────────────────────────────────────────── */
(function () {
    'use strict';

    // ── DOM refs ──────────────────────────────────────────────────────────────────────────────
    const input      = document.getElementById('jf-input');
    const output     = document.getElementById('jf-output');
    const errorPanel = document.getElementById('jf-error-panel');
    const errorMsg   = document.getElementById('jf-error-msg');
    const statusDot  = document.getElementById('jf-status-indicator');
    const statusText = document.getElementById('jf-status-text');
    const charCount  = document.getElementById('jf-char-count');
    const nodeCount  = document.getElementById('jf-node-count');
    const depthChip  = document.getElementById('jf-depth');
    const treeWrap   = document.getElementById('jf-tree-wrap');
    const treeRoot   = document.getElementById('jf-tree');

    const pasteBtn    = document.getElementById('jf-paste-btn');
    const clearBtn    = document.getElementById('jf-clear-btn');
    const copyBtn     = document.getElementById('jf-copy-btn');
    const indent2Btn  = document.getElementById('jf-indent-2');
    const indent4Btn  = document.getElementById('jf-indent-4');
    const minifyBtn   = document.getElementById('jf-minify-btn');
    const expandAll   = document.getElementById('jf-expand-all');
    const collapseAll = document.getElementById('jf-collapse-all');

    // ── State ─────────────────────────────────────────────────────────────────────────────────
    let parsedDoc = null;
    let indent    = 2;
    let minified  = false;

    // ── Status helpers ────────────────────────────────────────────────────────────────────────
    function setStatus(state, text) {
        statusDot.className    = 'jf-status-dot jf-status-' + state;
        statusText.textContent = text;
    }

    function showError(msg) {
        errorPanel.classList.remove('hidden');
        errorMsg.textContent = msg;
        setStatus('error', 'invalid xml');
        output.innerHTML = '';
        treeWrap.classList.add('hidden');
        [nodeCount, depthChip].forEach(el => el.classList.add('hidden'));
    }

    function hideError() {
        errorPanel.classList.add('hidden');
        errorMsg.textContent = '';
    }

    // ── XML parse ─────────────────────────────────────────────────────────────────────────────
    function parseXML(raw) {
        const parser = new DOMParser();
        const doc    = parser.parseFromString(raw, 'application/xml');
        const err    = doc.querySelector('parsererror');
        if (err) {
            const text = err.textContent || '';
            const line = text.match(/line\s+(\d+)/i);
            const col  = text.match(/col(?:umn)?\s+(\d+)/i);
            let msg = 'XML parse error';
            if (line) msg += ' at line ' + line[1];
            if (col)  msg += ', column ' + col[1];
            throw new Error(msg);
        }
        return doc;
    }

    // ── Stats ─────────────────────────────────────────────────────────────────────────────────
    function countElements(node) {
        let count = 0;
        if (node.nodeType === Node.ELEMENT_NODE) count = 1;
        for (const child of node.childNodes) count += countElements(child);
        return count;
    }

    function maxDepth(node, d) {
        d = d || 0;
        if (node.nodeType !== Node.ELEMENT_NODE) return d;
        let max = d;
        for (const child of node.childNodes) {
            if (child.nodeType === Node.ELEMENT_NODE) {
                max = Math.max(max, maxDepth(child, d + 1));
            }
        }
        return max;
    }

    // ── Escape ────────────────────────────────────────────────────────────────────────────────
    function escapeXml(str) {
        return str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
    }

    // ── Serialise ─────────────────────────────────────────────────────────────────────────────
    function serializeNode(node, indentStr, currentIndent, isMinified) {
        const next = isMinified ? '' : currentIndent + indentStr;
        const nl   = isMinified ? '' : '\n';

        if (node.nodeType === Node.TEXT_NODE) {
            const text = node.textContent.trim();
            return text ? escapeXml(text) : '';
        }

        if (node.nodeType === Node.CDATA_SECTION_NODE) {
            return currentIndent + '<![CDATA[' + node.textContent + ']]>';
        }

        if (node.nodeType === Node.COMMENT_NODE) {
            return currentIndent + '<!--' + node.textContent + '-->';
        }

        if (node.nodeType === Node.PROCESSING_INSTRUCTION_NODE) {
            return currentIndent + '<?' + node.target + ' ' + node.data + '?>';
        }

        if (node.nodeType !== Node.ELEMENT_NODE) return '';

        // Build opening tag
        let tag = '<' + node.tagName;
        for (const attr of node.attributes) {
            tag += ' ' + attr.name + '="' + escapeXml(attr.value) + '"';
        }

        const children = Array.from(node.childNodes).filter(n => {
            if (n.nodeType === Node.TEXT_NODE) return n.textContent.trim() !== '';
            return true;
        });

        if (children.length === 0) {
            return currentIndent + tag + '/>';
        }

        // Single text child — inline
        if (children.length === 1 && children[0].nodeType === Node.TEXT_NODE) {
            return (
                currentIndent +
                tag + '>' +
                escapeXml(children[0].textContent.trim()) +
                '</' + node.tagName + '>'
            );
        }

        // Multi-child — recurse
        const childParts = children
            .map(c => serializeNode(c, indentStr, next, isMinified))
            .filter(Boolean);

        return (
            currentIndent + tag + '>' +
            nl +
            childParts.join(nl) +
            nl +
            currentIndent + '</' + node.tagName + '>'
        );
    }

    function serialize(doc, indentSize, isMinified) {
        const indentStr = isMinified ? '' : ' '.repeat(indentSize);
        const parts = [];

        if (doc.xmlVersion) {
            let decl = '<?xml version="' + (doc.xmlVersion || '1.0') + '"';
            if (doc.xmlEncoding) decl += ' encoding="' + doc.xmlEncoding + '"';
            decl += '?>';
            parts.push(decl);
        }

        for (const child of doc.childNodes) {
            const s = serializeNode(child, indentStr, '', isMinified);
            if (s) parts.push(s);
        }

        return isMinified ? parts.join('') : parts.join('\n');
    }

    // ── Syntax highlighting ───────────────────────────────────────────────────────────────────
    // Operates on plain XML text (not pre-escaped). We escape for HTML display
    // ourselves here, token by token, so we have full control over what gets
    // wrapped in spans vs what gets escaped.
    function syntaxHighlight(xmlStr) {
        // Tokenise the raw serialised XML string into an array of tokens,
        // then HTML-escape each token's text content and wrap it in the
        // appropriate span. This avoids any regex fighting with escaped HTML.

        const tokens = tokenize(xmlStr);
        let html = '';

        for (const tok of tokens) {
            switch (tok.type) {
                case 'xml-decl':
                    html += renderDecl(tok.raw);
                    break;
                case 'comment':
                    html += '<span class="xml-comment">' + esc(tok.raw) + '</span>';
                    break;
                case 'cdata':
                    html += '<span class="xml-cdata">' + esc(tok.raw) + '</span>';
                    break;
                case 'pi':
                    html += '<span class="xml-pi">' + esc(tok.raw) + '</span>';
                    break;
                case 'open-tag':
                case 'self-close-tag':
                    html += renderOpenTag(tok.raw, tok.type === 'self-close-tag');
                    break;
                case 'close-tag':
                    html += renderCloseTag(tok.raw);
                    break;
                case 'text':
                    html += tok.raw.trim()
                        ? '<span class="xml-text">' + esc(tok.raw) + '</span>'
                        : esc(tok.raw); // preserve whitespace-only text (newlines/indent) unstyled
                    break;
                default:
                    html += esc(tok.raw);
            }
        }

        return html;
    }

    // Escape a string for safe innerHTML insertion
    function esc(str) {
        return str
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
    }

    // Tokenize raw XML into typed chunks
    function tokenize(xml) {
        const tokens = [];
        let i = 0;

        while (i < xml.length) {
            if (xml[i] !== '<') {
                // Text node
                const end = xml.indexOf('<', i);
                const text = end === -1 ? xml.slice(i) : xml.slice(i, end);
                tokens.push({ type: 'text', raw: text });
                i = end === -1 ? xml.length : end;
                continue;
            }

            // Comment
            if (xml.startsWith('<!--', i)) {
                const end = xml.indexOf('-->', i) + 3;
                tokens.push({ type: 'comment', raw: xml.slice(i, end) });
                i = end;
                continue;
            }

            // CDATA
            if (xml.startsWith('<![CDATA[', i)) {
                const end = xml.indexOf(']]>', i) + 3;
                tokens.push({ type: 'cdata', raw: xml.slice(i, end) });
                i = end;
                continue;
            }

            // XML declaration or processing instruction
            if (xml.startsWith('<?', i)) {
                const end = xml.indexOf('?>', i) + 2;
                const raw = xml.slice(i, end);
                const type = /^<\?xml\s/i.test(raw) ? 'xml-decl' : 'pi';
                tokens.push({ type, raw });
                i = end;
                continue;
            }

            // Closing tag
            if (xml.startsWith('</', i)) {
                const end = xml.indexOf('>', i) + 1;
                tokens.push({ type: 'close-tag', raw: xml.slice(i, end) });
                i = end;
                continue;
            }

            // Opening or self-closing tag
            const end = xml.indexOf('>', i) + 1;
            const raw = xml.slice(i, end);
            const type = raw.endsWith('/>') ? 'self-close-tag' : 'open-tag';
            tokens.push({ type, raw });
            i = end;
        }

        return tokens;
    }

    // Render <?xml ... ?> declaration
    function renderDecl(raw) {
        // raw = <?xml version="1.0" encoding="UTF-8"?>
        let out = '<span class="xml-punct">&lt;?</span>';
        out += '<span class="xml-decl">xml</span>';
        const inner = raw.slice(5, raw.length - 2); // strip <?xml and ?>
        out += renderAttrs(inner);
        out += '<span class="xml-punct">?&gt;</span>';
        return out;
    }

    // Render opening tag, optionally self-closing
    function renderOpenTag(raw, selfClose) {
        // raw = <tagName attr="val" ...> or <tagName attr="val" ... />
        const inner = selfClose ? raw.slice(1, raw.length - 2) : raw.slice(1, raw.length - 1);
        const spaceIdx = inner.search(/[\s\/]/);
        const tagName  = spaceIdx === -1 ? inner : inner.slice(0, spaceIdx);
        const rest     = spaceIdx === -1 ? ''    : inner.slice(spaceIdx);

        let out = '<span class="xml-punct">&lt;</span>';
        out += '<span class="xml-tag">' + esc(tagName) + '</span>';
        out += renderAttrs(rest);
        out += selfClose
            ? '<span class="xml-punct">/&gt;</span>'
            : '<span class="xml-punct">&gt;</span>';
        return out;
    }

    // Render closing tag
    function renderCloseTag(raw) {
        // raw = </tagName>
        const tagName = raw.slice(2, raw.length - 1);
        return (
            '<span class="xml-punct">&lt;/</span>' +
            '<span class="xml-tag">' + esc(tagName) + '</span>' +
            '<span class="xml-punct">&gt;</span>'
        );
    }

    // Render attribute string (may contain whitespace, attr="val" pairs)
    function renderAttrs(str) {
        // Match: attrName="value" or attrName='value'
        const attrRe = /([a-zA-Z_:][\w:.-]*)\s*=\s*("(?:[^"]*)"|'(?:[^']*)')/g;
        let out  = '';
        let last = 0;
        let m;

        while ((m = attrRe.exec(str)) !== null) {
            // Any whitespace/text before this match
            if (m.index > last) {
                out += esc(str.slice(last, m.index));
            }
            out += '<span class="xml-attr-name">' + esc(m[1]) + '</span>';
            out += '<span class="xml-punct">=</span>';
            out += '<span class="xml-attr-val">' + esc(m[2]) + '</span>';
            last = m.index + m[0].length;
        }

        // Trailing text (spaces, /  etc.)
        if (last < str.length) {
            out += esc(str.slice(last));
        }

        return out;
    }

    // ── Core process ──────────────────────────────────────────────────────────────────────────
    function process() {
        const raw = input.value.trim();
        charCount.textContent = input.value.length + ' chars';

        if (!raw) {
            setStatus('idle', 'ready');
            hideError();
            output.innerHTML = '';
            treeWrap.classList.add('hidden');
            [nodeCount, depthChip].forEach(el => el.classList.add('hidden'));
            parsedDoc = null;
            return;
        }

        try {
            parsedDoc = parseXML(raw);
        } catch (e) {
            showError(e.message);
            parsedDoc = null;
            return;
        }

        hideError();

        const root  = parsedDoc.documentElement;
        const elems = root ? countElements(root) : 0;
        const depth = root ? maxDepth(root, 0)   : 0;

        nodeCount.textContent = elems + ' elements';
        depthChip.textContent = 'depth ' + depth;
        [nodeCount, depthChip].forEach(el => el.classList.remove('hidden'));

        renderOutput();
        renderTree(parsedDoc);
        treeWrap.classList.remove('hidden');
        setStatus('ok', 'valid xml');
    }

    function renderOutput() {
        if (!parsedDoc) return;
        const str = serialize(parsedDoc, indent, minified);
        output.innerHTML = syntaxHighlight(str);
    }

    // ── Tree builder ──────────────────────────────────────────────────────────────────────────
    function buildTreeNode(node) {
        if (node.nodeType === Node.TEXT_NODE) {
            const text = node.textContent.trim();
            if (!text) return null;
            const li  = document.createElement('li');
            const val = document.createElement('span');
            val.className   = 'jf-tree-value jf-str';
            val.textContent = '"' + text + '"';
            li.appendChild(val);
            return li;
        }

        if (node.nodeType === Node.COMMENT_NODE) {
            const li  = document.createElement('li');
            const val = document.createElement('span');
            val.className   = 'jf-tree-value xml-comment-tree';
            val.textContent = '<!-- ' + node.textContent.trim() + ' -->';
            li.appendChild(val);
            return li;
        }

        if (node.nodeType !== Node.ELEMENT_NODE) return null;

        const li   = document.createElement('li');
        const span = document.createElement('span');
        span.className = 'jf-tree-node';

        const toggle = document.createElement('span');
        toggle.className = 'jf-tree-toggle';

        const keyEl = document.createElement('span');
        keyEl.className   = 'jf-tree-key';
        keyEl.textContent = '<' + node.tagName + '>';

        const typeEl = document.createElement('span');
        typeEl.className = 'jf-tree-type';

        const childElements = Array.from(node.childNodes).filter(n =>
            (n.nodeType === Node.ELEMENT_NODE) ||
            (n.nodeType === Node.TEXT_NODE && n.textContent.trim()) ||
            (n.nodeType === Node.COMMENT_NODE)
        );

        const hasChildren = childElements.length > 0;
        const attrCount   = node.attributes.length;

        const typeParts = [];
        if (attrCount > 0)   typeParts.push(attrCount + ' attr' + (attrCount > 1 ? 's' : ''));
        if (hasChildren)     typeParts.push(childElements.length + ' child' + (childElements.length > 1 ? 'ren' : ''));
        typeEl.textContent = typeParts.join(', ');

        if (hasChildren || attrCount > 0) {
            span.classList.add('collapsible');
            toggle.textContent = '▾';
            span.append(toggle, keyEl, typeEl);
            li.appendChild(span);

            const childrenUl = document.createElement('ul');
            childrenUl.className = 'jf-tree-children';

            // Attributes as pseudo-children
            for (const attr of node.attributes) {
                const attrLi   = document.createElement('li');
                const attrSpan = document.createElement('span');
                attrSpan.className = 'jf-tree-node';

                const attrToggle       = document.createElement('span');
                attrToggle.className   = 'jf-tree-toggle';
                attrToggle.textContent = ' ';

                const attrKey       = document.createElement('span');
                attrKey.className   = 'jf-tree-key';
                attrKey.textContent = '@' + attr.name;

                const attrColon       = document.createElement('span');
                attrColon.className   = 'jf-tree-colon';
                attrColon.textContent = ': ';

                const attrVal       = document.createElement('span');
                attrVal.className   = 'jf-tree-value jf-str';
                attrVal.textContent = '"' + attr.value + '"';

                attrSpan.append(attrToggle, attrKey, attrColon, attrVal);
                attrLi.appendChild(attrSpan);
                childrenUl.appendChild(attrLi);
            }

            for (const child of childElements) {
                const childLi = buildTreeNode(child);
                if (childLi) childrenUl.appendChild(childLi);
            }

            li.appendChild(childrenUl);

            span.addEventListener('click', function (e) {
                e.stopPropagation();
                const collapsed = childrenUl.classList.toggle('collapsed');
                toggle.textContent = collapsed ? '▸' : '▾';
            });

        } else {
            // Leaf element
            toggle.textContent = ' ';
            const textChild = Array.from(node.childNodes).find(n =>
                n.nodeType === Node.TEXT_NODE && n.textContent.trim()
            );

            const colon       = document.createElement('span');
            colon.className   = 'jf-tree-colon';
            colon.textContent = textChild ? ': ' : '';

            const valEl       = document.createElement('span');
            valEl.className   = 'jf-tree-value jf-str';
            valEl.textContent = textChild ? '"' + textChild.textContent.trim() + '"' : '';

            span.append(toggle, keyEl, colon, valEl, typeEl);
            li.appendChild(span);
        }

        return li;
    }

    function renderTree(doc) {
        treeRoot.innerHTML = '';
        const ul = document.createElement('ul');

        for (const child of doc.childNodes) {
            if (child.nodeType === Node.ELEMENT_NODE ||
                child.nodeType === Node.COMMENT_NODE) {
                const li = buildTreeNode(child);
                if (li) ul.appendChild(li);
            }
        }

        treeRoot.appendChild(ul);
    }


    // ── Event bindings ────────────────────────────────────────────────────────────────────────
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
        treeRoot.querySelectorAll('.jf-tree-children').forEach(el => el.classList.remove('collapsed'));
        treeRoot.querySelectorAll('.jf-tree-toggle').forEach(el => {
            if (el.textContent === '▸') el.textContent = '▾';
        });
    });

    collapseAll.addEventListener('click', function () {
        treeRoot.querySelectorAll('.jf-tree-children').forEach(el => el.classList.add('collapsed'));
        treeRoot.querySelectorAll('.jf-tree-toggle').forEach(el => {
            if (el.textContent === '▾') el.textContent = '▸';
        });
    });

    // ── Init ──────────────────────────────────────────────────────────────────────────────────
    indent2Btn.classList.add('active');
    setStatus('idle', 'ready');

})();
