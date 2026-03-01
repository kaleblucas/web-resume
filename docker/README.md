# Docker Configuration

This directory contains the Docker Compose setup for the web-resume portfolio container.

## Files

- **docker-compose.yml** - Main Docker Compose configuration
- **.env.example** - Environment variables template (copy to `.env` locally)
- **update.sh** - Quick update script to pull latest from GitHub and restart
- **README.md** - This file

## Quick Commands

### Start the Container
```bash
docker-compose up -d
```

### View Logs
```bash
docker-compose logs -f portfolio-prod
```

### Stop the Container
```bash
docker-compose down
```

### Update from GitHub and Restart
```bash
./update.sh
```

## Environment Setup

Before running, copy the example env file:
```bash
cp .env.example .env
```

Edit `.env` if you need to change:
- Container name
- Port mappings
- Environment variables

## Container Details

- **Image**: `nginx:alpine`
- **Container Name**: `portfolio-prod`
- **Port**: 80 (configurable)
- **Volume**: Git repository mounted at `/usr/share/nginx/html/`
- **Network**: bridge (default)

## Notes

- The repository is mounted as read-only for the web root
- SSL certificates can be added to `../ssl/` directory
- Nginx configuration is in `../nginx/nginx.conf`
