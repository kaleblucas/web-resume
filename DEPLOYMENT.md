# Portfolio-Prod Deployment Guide

This repository contains the web-resume portfolio site with complete Docker containerization for easy deployment and version control.

## 📁 Directory Structure

```
.
├── index.html                 # Main resume page
├── *.css                      # Stylesheets
├── *.js                       # JavaScript files
├── *.html                     # Additional pages (formatters, etc.)
├── favicon.svg                # Site icon
│
├── docker/                    # Docker configuration
│   ├── docker-compose.yml     # Docker Compose configuration
│   ├── .env.example           # Environment variables template
│   ├── update.sh              # Quick update script
│   └── README.md              # Docker-specific docs
│
├── nginx/                     # Nginx configuration
│   └── nginx.conf             # Nginx server settings
│
├── ssl/                       # SSL certificates (optional)
│   ├── certs/
│   └── keys/
│
├── DEPLOYMENT.md              # This file
├── README.md                  # Project documentation
└── LICENSE                    # License file
```

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose installed
- Git installed and configured

### Option 1: Clone and Run (Recommended)

```bash
# Clone the repository
git clone git@github.com:kaleblucas/web-resume.git
cd web-resume

# Set up environment
cp docker/.env.example docker/.env

# Start the container
cd docker
docker-compose up -d

# View logs
docker-compose logs -f portfolio-prod
```

### Option 2: Update Existing Container

```bash
cd ~/portfolio-prod/web-resume/docker
./update.sh
```

## 🔄 Workflow: Development to Deployment

### Making Changes
1. Edit files in the repository locally
2. Test changes with the running container
3. Commit changes to Git:
   ```bash
   git add .
   git commit -m "Description of changes"
   git push origin main
   ```

### Deploying Updates
After pushing to GitHub, pull the latest changes:

```bash
cd ~/portfolio-prod/web-resume
git pull origin main

# Restart container to apply changes
cd docker
docker-compose restart portfolio-prod
```

Or use the quick update script:
```bash
cd ~/portfolio-prod/web-resume/docker
./update.sh
```

## 📝 Docker Commands

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

### Rebuild Container
```bash
cd docker
docker-compose down
docker-compose up -d --build
```

### Access Container Shell
```bash
docker exec -it portfolio-prod sh
```

## 🌐 Accessing the Site

- **Local**: http://localhost
- **With domain**: Configure nginx.conf and update docker-compose.yml ports

## 🔒 SSL/TLS Configuration

To enable HTTPS:

1. Place your certificate in `ssl/certs/certificate.pem`
2. Place your key in `ssl/keys/private.key`
3. Uncomment HTTPS section in `nginx/nginx.conf`
4. Update port mapping in `docker/docker-compose.yml`
5. Restart container:
   ```bash
   cd docker
   docker-compose restart portfolio-prod
   ```

## 🛠️ Troubleshooting

### Container won't start
```bash
cd docker
docker-compose logs portfolio-prod
```

### Port already in use
Edit `docker/docker-compose.yml` and change the port mapping:
```yaml
ports:
  - "8080:80"  # Change 8080 to your desired port
```

### File permissions issues
```bash
chmod -R 755 ~/portfolio-prod/web-resume
```

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Nginx Documentation](https://nginx.org/en/docs/)

## 📝 License

See LICENSE file for details.
