#!/bin/bash

cmd="$1"
shift || true

case "$cmd" in
    "start")
    echo "Starting Redis docker"
      docker run -d -p 6379:6379 --name echoAt redis
        ;;
    "finish")
     echo "Stopping Redis docker"
        docker stop echoAt
        docker rm echoAt
        docker image rm redis
        ;;
    *)
        echo "Unsupported command: $cmd" >&2
        usage
        ;;
esac

