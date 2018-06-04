# DFT BLUE BADGE BETA - BADGEMANAGEMENT-SERVICE

## Getting Started in few minutes
From command line:
```
git clone git@github.com:uk-gov-dft/badgemanagement-service.git
cd badgemanagement-service
gradle wrapper
gradle build
gradle bootRun
```

## INSTALLATION
From command line:
```
brew install postgres
```
[Download and install pgadmin](https://www.pgadmin.org/download/pgadmin-4-macos/)

To Start postgresql:
```
pg_ctl start -D /usr/local/var/postgres
```

Create User:
```
createuser -W developer -P
createdb bb_dev
psql bb_dev -U developer
```

You may need to create the database using the (database-schemas project)[https://github.com/uk-gov-dft/database-schemas], please read the 
[database-schemes Readme](https://github.com/uk-gov-dft/database-schemas/blob/develop/migrations/README.md)

Some useful commands to test things in posgresql
```
psql bb_dev -U developer
\l to show all database
use database_name --- to change database
\dt to list tables 
```


## DEPLOYING ARTIFACTS TO LOCAL MAVEN REPOSITORY
```
cd model
gradle install
cd ..
cd client
gradle install
```

## PLAY WITH THE API
* [Retrieve badge 1 (GET)](http://localhost:8280/uk-gov-dft/badgemanagement-api/1.0.0/badges/AB123)

