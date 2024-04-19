## Push to docker on Ubuntu:

- chú ý: ko để thông số #server.port=8888 trong application.properties
- mvn clean package
- copy file target/root-0.0.1-SNAPSHOT.jar to host: /home/project_name/target/
- copy file Dockerfile, docker-compose.yml (if changes) to host: /home/project_name/

```bash
# maven clearn and package
mvn clean package
```
```bash 
docker compose up -d mysqldb
```
```bash 
docker compose up -d backend
```
```bash
docker compose build
```

## Update image and rebuild container

```
mvn clean package
```
```bash 
clear
```
```bash
docker compose down backend
```
```bash
docker image rm project2-backend:latest
```
```bash
docker volume rm project2_backend
```
```bash
docker compose up -d backend
```

## Update image and rebuild container on VPS

## mysqldb container:

```bash 
docker compose up -d mysqldb
```

### backend container:
- copy file target/root-0.0.1-SNAPSHOT.jar to host: /home/project_name/target/

```bash
docker compose down backend
docker image rm project2-backend:latest
docker volume rm project2_backend
docker compose up -d backend

```

### Info
```bash
docker logs backend #logs container backend
```
```bash
docker ps # list container running
```
```bash
docker image ls # list all image
```
```bash
docker volume ls # list all volume
```
```bash
# login to mysql container
docker exec -it mysqldb mysql -uroot -p
```
```bash
# remove all images without at least one container
docker image prune
```

```bash
docker rm -vf $(docker ps -aq); docker rmi -f $(docker images -aq)
```
```bash
docker volume rm $(docker volume ls -q --filter dangling=true)
```

