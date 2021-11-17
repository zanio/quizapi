# Getting Started

### Reference Documentation
This is a project to demonstrate RESTful API with spring boot . The task is to build a  Quiz Api.
you can find the hosted documentation @ [QuizApi](https://documenter.getpostman.com/view/7638519/UVCB94RP)

### DEVELOPMENT

#### ENVIRONMENT VARIABLE.
The required environment variables can be gotten from the env.example file. Please note that all properties in
the env.example file must be set. Simply create a new file called .env and copy past the content of `env.example` into the `.env` file.
If you are not runing the app with docker then use OPTION 1 below to set up your database,  If you are using OPTION 2 
then you only need to run the `docker-compose up` command

#### Database setup (POSTGRES)

#### OPTION 1

TO CREATE A DATABASE, USER AND GRANT ALL PRIVILEGE TO THAT USER ON THE CREATED DATABASE RUN THE FOLLOWING CODE:

If you are just setting up your environment then Run The below code on the project root directory of the application--

 ```sql
export PGPASSWORD='<root-postgres-db-password>'; psql -h localhost -p 5432 -U <root-user> -f initializer.sql -d <root-database>
```
for example you can type this:
```sql
export PGPASSWORD='password1234'; psql -h localhost -p 5432 -U postgres -f initializer.sql -d postgres
```

Where `<root-postgres-db-password>` is the root password of postgres You should set multiple database, i.e for test and
dev. The test environment would handle integration testing All you have to do is to edit the initialize.sql with your own
database name, database password and database  username . 
<br />
<br />
### OPTION 2

Pre-requisites:

- Docker for Desktop

Run `docker-compose up` in the root of the project.

It will bring up Postgres and the Express application server in development mode.

It binds the application server to `localhost:${PORT}`, Where `port` is any value set in your .env file

Postgres is exposed on port `35432`. The connection string is `postgres://user:pass@localhost:35432/db` (username, password and database name are defined in [./docker-compose.yaml](./docker-compose.yaml)).

You can connect to Postgres using the psql client:

```sh
psql postgres://user:pass@localhost:35432/db
```
<br />
<br />

### EXTRA NOTE.
 if you are using option 1 and intellij you can simply set this env values by
clicking on the play button at the top right and click on edit configuration, a new configuration box would appear that looks like the
image below:
![Image one](image-1.png)
<br/>
<br/>
From the image above you can see the environment variables section:
![Image Two](image-2.png)

<br/>
<br/>

click on the dollar sign and another modal would appear. from this modal you can set your environment variables as key/value pairs:
![Image Two](image-3.png)

<br/>
<br/>

#### Starting up the application:
make sure you resolve all dependencies first.
To start the application based on intellij you can just use the play button on intellij.
OR You can just compile and start the application from the command line while on the app directory using the following command
- mvn -B clean package -DskipTests
  <br/>
  <br/>
and start the application using the following command:
- java -jar  target/quizapi-0.0.1-SNAPSHOT.jar 

note: make sure you set all required environment variables as required by the env.example file using the below command if you are on linux
`export KEY="value"`



#### Testing up the application:

- from intellij you can run the test files individually or within each folder.

# quizapi
