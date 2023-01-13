#!/usr/bin/env bash
# SPDX-License-Identifier: MIT

cd $(dirname "$0")
source "../shared/scripts/9999-helper.sh"

ENVIRONMENT_FILES_FOLDER="../shared/environment"
ENVIRONMENT_FILE=".env-single"

# Only variables from .env can be used in the Docker-Compose file
# all other variables are only available in the container
setup_environment_file ".env" "env" "$ENVIRONMENT_FILES_FOLDER/env-base-image"
setup_environment_file "$ENVIRONMENT_FILE" "$ENVIRONMENT_FILES_FOLDER/env-base"

# Use Docker BuildKit
export BUILDKIT_PROGRESS=plain
export DOCKER_BUILDKIT=1

echo "Starting single container with external network."
docker compose --file docker-compose_pds_scancode_external-network.yaml up --build --remove-orphans
