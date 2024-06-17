# Makefile for a multi-module project with Docker and Maven

# Define variables
MVN = mvn
DOCKER_COMPOSE = docker-compose
DOCKER = docker
DOCKERFILE_DIR = docker
PRODUCER_DIR = producer
DOCKER_COMPOSE_FILE = $(DOCKERFILE_DIR)/docker-compose.yml
DOCKERFILE = $(DOCKERFILE_DIR)/Dockerfile
ARTIFACT_NAME = app.jar
TARGET_DIR = $(PRODUCER_DIR)/target

.PHONY: all build clean docker-build up down logs

# Default target to build everything
all: build copy-artifact docker-build

# Build the Maven project
build:
	$(MVN) -f $(PRODUCER_DIR)/pom.xml clean install

# Clean the Maven project
clean:
	$(MVN) -f $(PRODUCER_DIR)/pom.xml clean

# Copy the built artifact to the Docker context
copy-artifact: build
	cp $(TARGET_DIR)/$(ARTIFACT_NAME) $(DOCKERFILE_DIR)/producer/$(ARTIFACT_NAME)

# Build the Docker image
docker-build: copy-artifact
	$(DOCKER) build -t kafka-producer $(DOCKERFILE_DIR)

# Bring up the Docker Compose services
up:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) up -d

# Bring down the Docker Compose services
down:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) down

# Tail logs from Docker Compose services
logs:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) logs -f
