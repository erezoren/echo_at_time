#!/bin/bash

cmd="$1"
shift || true

usage() {
    echo "Usage: $0 {start|finish|status}"
    echo "  start  - Start Redis Docker container"
    echo "  finish - Stop and remove Redis Docker container"
    echo "  status - Show container status"
    exit 1
}

case "$cmd" in
    "start")
        echo "Starting Redis docker container..."
        if docker ps -a | grep -q "echoAt"; then
            echo "Container 'echoAt' already exists. Removing it first..."
            docker stop echoAt 2>/dev/null || true
            docker rm echoAt 2>/dev/null || true
        fi
        docker run -d -p 6379:6379 --name echoAt redis:latest
        if [ $? -eq 0 ]; then
            echo "Redis container started successfully"
            echo "Redis is available at localhost:6379"
        else
            echo "Failed to start Redis container"
            exit 1
        fi
        ;;
    "finish")
        echo "Stopping Redis docker container..."
        docker stop echoAt 2>/dev/null || echo "Container 'echoAt' not running"
        docker rm echoAt 2>/dev/null || echo "Container 'echoAt' not found"
        echo "Redis container stopped and removed"
        # Note: Not removing the Redis image to avoid re-downloading
        ;;
    "status")
        echo "Redis container status:"
        docker ps -a | grep echoAt || echo "Container 'echoAt' not found"
        ;;
    *)
        echo "Unsupported command: $cmd" >&2
        usage
        ;;
esac

