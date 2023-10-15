# Java assessment

### Maven project which has 2 modules
core = contains services and main logic   
rest = rest api, contains rest controller and entry point for SpringApplication

### API Endpoints
````
GET     /api/folders:                    Get a list of folders.
GET     /api/filesizes?filetype=xml:     Get file sizes by filetype(optional) in bytes.
DELETE  /api/purge:                      Purge all data.
````

Added a few unit tests

## Configs
Scheduling for re-scan is enabled(drop and reinsert) default: 60secs, can be configured in application.properties together with root path to be scanned:
````
file.scan.directory=.
file.scan.frequency=60000
````

## Notes
Project was built and tested with Java 17, there might be incompatibilities when using older Java versions as default SDK(change in sys. env. variables) e.g Java 8.
For Testing H2 Database and Postman was used. 
