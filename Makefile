# Makefile for a multi-module project with Docker and Maven

# Define variables
MVN = mvn
DOCKER_COMPOSE = docker compose
DOCKER = docker
DOCKERFILE_DIR = docker
PRODUCER_DIR = producer
CONSUMER_DIR = consumer
DOCKER_COMPOSE_FILE = $(DOCKERFILE_DIR)/docker-compose.yml

.PHONY: all build clean docker-build up down logs clean-docker

# Default target to build everything
all: build copy-artifact docker-build

# Build the Maven project
build:
	$(MVN) -f $(PRODUCER_DIR)/pom.xml clean install
	$(MVN) -f $(CONSUMER_DIR)/pom.xml clean install

# Clean the Maven project
clean:
	$(MVN) -f $(PRODUCER_DIR)/pom.xml clean
	$(MVN) -f $(CONSUMER_DIR)/pom.xml clean

# Copy the built artifact to the Docker context
copy-artifact: build
	cp $(PRODUCER_DIR)/target/app.jar $(DOCKERFILE_DIR)/producer/app.jar
	cp $(CONSUMER_DIR)/target/app.jar $(DOCKERFILE_DIR)/consumer/app.jar

# Build the Docker image
docker-build: copy-artifact
	$(DOCKER) build -t kafka-producer $(DOCKERFILE_DIR)/producer
	$(DOCKER) build -t kafka-consumer $(DOCKERFILE_DIR)/consumer

# Bring up the Docker Compose services
up:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) up -d

# Bring down the Docker Compose services
down:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) down

# Tail logs from Docker Compose services
logs:
	$(DOCKER_COMPOSE) -f $(DOCKER_COMPOSE_FILE) logs -f

# Clean up Docker images
clean-docker:
	$(DOCKER) rmi -f kafka-producer kafka-consumer
