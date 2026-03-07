function pasteFromClipboard() {
    if (!navigator.clipboard || !navigator.clipboard.readText) {
        return Promise.reject(new Error('Clipboard API not available'));
    }
    return navigator.clipboard.readText();
}
