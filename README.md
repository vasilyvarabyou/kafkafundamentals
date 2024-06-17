# Kafka Fundamentals course tasks


## Makefile Targets

- `all`: Default target to build the Maven project, copy the artifact, and build the Docker image.
- `build`: Runs `mvn clean install` on the `producer` module.
- `clean`: Runs `mvn clean` on the `producer` module.
- `copy-artifact`: Copies the built artifact to the Docker context.
- `docker-build`: Builds the Docker image.
- `up`: Brings up the Docker Compose services.
- `down`: Brings down the Docker Compose services.
- `logs`: Tails the logs from the Docker Compose services.
- `clean-docker`: Removes docker images

## Usage

### Build the Project and Docker Image

To build the Maven project, copy the artifact, and build the Docker image, run:

```sh
make
```
### Bring Up the Docker Compose Services

To start the Docker Compose services, run:

```sh
make up
```
### Bring Down the Docker Compose Services

To stop the Docker Compose services, run:
```sh
make down
```
### Tail Logs from Docker Compose Services

To tail the logs from the Docker Compose services, run:
```sh
make logs
```

### Remove Docker images

TO clean up docker images
```sh
make clean-docker
```
