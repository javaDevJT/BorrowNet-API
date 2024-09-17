# BorrowNet-API

## Starting a local PostgreSQL DB for the first time
You should only do this in this manner the first time such that you create your DB container. 
1. Download and install Docker Desktop
   1. You may need to create an account and/or login
2. `docker pull postgres:15.8-alpine`
3. `docker run --name borrownet-api-db -p 5432:5432 -e POSTGRES_PASSWORD=borrownetapi -e POSTGRES_USER=borrownetapi -d postgres:15.8-alpine`
4. In subsequent runs of the DB, relaunch the created container from the `Containers` tab in Docker Desktop.
   1. This is also where you can stop the container when you are done working. Do not delete the container or your persistent data will be lost, and you will need to complete step 3 again.
