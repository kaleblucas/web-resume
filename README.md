# Web Resume

A simple, professional web-based resume built with HTML, CSS, and JavaScript.

## Features

- Clean, responsive design
- JSON and XML formatters
- Professional styling with custom themes

## Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kaleblucas/web-resume.git
   cd web-resume
   ```

2. **Run with Docker:**
   ```bash
   docker-compose up -d
   ```

3. **Access the site:**
   - Open http://localhost:8082

## Development

To run locally without Docker:
```bash
# Serve files with any web server
python -m http.server 8082
# or
npx serve -p 8082
```

## Files

- `index.html` - Main resume page
- `styles.css` - Primary styling
- `script.js` - Interactive functionality
- `*-formatter.*` - JSON/XML formatting tools
- `docker-compose.yml` - Container configuration

## License

MIT License - see LICENSE file for details.