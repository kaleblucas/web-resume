# Web Resume - Portfolio Project

A professional web-based resume and portfolio site with complete Docker containerization for easy deployment and version control.

## 📋 Features

- ✅ Professional resume presentation
- ✅ JSON and XML formatter utilities
- ✅ Responsive design with custom styling
- ✅ Docker containerization with Nginx
- ✅ Git version control
- ✅ Easy deployment and updates
- ✅ SSL/TLS ready

## 🚀 Quick Start

### Clone and Deploy

```bash
# Clone the repository
git clone git@github.com:kaleblucas/web-resume.git
cd web-resume

# Set up Docker environment
cp docker/.env.example docker/.env

# Start the container
cd docker
docker-compose up -d

# Access the site
# Open http://localhost in your browser
```

### Update from GitHub

```bash
cd ~/portfolio-prod/web-resume/docker
./update.sh
```

## 📁 Directory Structure

```
web-resume/
├── index.html              # Main resume page
├── styles.css              # Main stylesheet
├── formatter.css           # Formatter styles
├── theme-override.css      # Theme customization
│
├── script.js               # Main JavaScript
├── json-formatter.js       # JSON formatter utility
├── xml-formatter.js        # XML formatter utility
├── json-formatter.html     # JSON formatter page
├── xml-formatter.html      # XML formatter page
│
├── favicon.svg             # Site favicon
├── 50x.html                # Error page
│
├── docker/                 # 🐳 Docker Configuration
│   ├── docker-compose.yml  # Docker Compose setup
│   ├── .env.example        # Environment variables
│   ├── update.sh           # Update script
│   └── README.md           # Docker documentation
│
├── nginx/                  # Nginx Configuration
│   └── nginx.conf          # Nginx server config
│
├── ssl/                    # SSL/TLS Certificates (optional)
│   ├── certs/
│   └── keys/
│
├── DEPLOYMENT.md           # Deployment guide
├── README.md               # This file
└── LICENSE                 # License
```

## 🐳 Docker

This project uses Docker and Docker Compose for containerization. All configuration is in the `docker/` directory.

### Start Services
```bash
cd docker
docker-compose up -d
```

### View Logs
```bash
cd docker
docker-compose logs -f portfolio-prod
```

### Stop Services
```bash
cd docker
docker-compose down
```

### Update and Restart
```bash
cd docker
./update.sh
```

For more Docker details, see [docker/README.md](docker/README.md)

## 🔄 Development Workflow

1. **Make changes** to HTML, CSS, or JS files
2. **Test locally** with the running container
3. **Commit changes**:
   ```bash
   git add .
   git commit -m "Description of changes"
   git push origin main
   ```
4. **Deploy** by pulling and restarting:
   ```bash
   git pull origin main
   cd docker && docker-compose restart portfolio-prod
   ```

## 📝 Deployment

For detailed deployment instructions, see [DEPLOYMENT.md](DEPLOYMENT.md)

Key topics:
- Quick start with Docker
- Development workflow
- Docker commands
- SSL/TLS configuration
- Troubleshooting

## 🌐 Accessing the Site

### Local Development
- http://localhost (or configured port)

### Production
- Configure domain in `nginx/nginx.conf`
- Set up SSL certificates in `ssl/` directory
- Update Docker configuration as needed

## 🔒 SSL/TLS Setup

To enable HTTPS:
1. Add certificate to `ssl/certs/certificate.pem`
2. Add key to `ssl/keys/private.key`
3. Uncomment HTTPS in `nginx/nginx.conf`
4. Restart container

## 🛠️ Requirements

- Docker & Docker Compose
- Git (for version control)
- Modern web browser (for viewing)

## 📚 Technologies

- **Frontend**: HTML, CSS, JavaScript
- **Server**: Nginx
- **Containerization**: Docker & Docker Compose
- **Version Control**: Git & GitHub

## 📞 Support

For issues or questions:
1. Check [DEPLOYMENT.md](DEPLOYMENT.md) troubleshooting section
2. Check Docker logs: `docker-compose logs portfolio-prod`
3. Review Nginx configuration in `nginx/nginx.conf`

## 📄 License

See [LICENSE](LICENSE) file for details.

## 🔗 Links

- **Repository**: https://github.com/kaleblucas/web-resume
- **GitHub Profile**: https://github.com/kaleblucas

---

**Last Updated**: 2026-02-22  
**Version**: 1.0.0
