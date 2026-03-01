#!/bin/bash
# Quick script to update portfolio-prod from GitHub

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
REPO_DIR="$SCRIPT_DIR/../portfolio-prod/web-resume"

echo "🔄 Updating portfolio-prod from GitHub..."

cd "$REPO_DIR"

# Check if we're in a git repository
if [ ! -d .git ]; then
    echo "❌ Error: Not a git repository"
    exit 1
fi

# Pull latest changes
echo "📥 Pulling from origin/main..."
git pull origin main

echo "✅ Update complete!"
echo "📍 Location: $REPO_DIR"
echo ""
echo "Changes are live on the portfolio-prod container (no restart needed)"
