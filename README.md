# Java assessment

## Core features

### Build a Maven project with at least 2 modules and a parent
core = contains services
rest = rest api

### Build a Spring Boot Rest API
run as self-containing single Spring Boot jar with e.g. jetty embedded

### Design a service
reading/scanning all files and subfolders of a specific root folder (configure in application.properties/yaml)
writing file metadata (path, filename, filetype=extension, filesize, modification date, scan date=now) into a persisted database (h2, postgreSQL, ...)
run on startup

Rest API:
GET /folders
delivers a list of folders and subfolders sorted by name.
GET /filesizes?filetype=xxx
delivers folders and subfolders with aggregated filesize sorted by size,
filtered by filetype/extension=xxx if available.
purge all files and folders from db via rest API.

Add some unit tests

## Nice to have
docker-compose running service and database  
scheduling all x minutes and rescan the folder and rewrite/update files or drop and reinsert (x .. configurable)
 

## Notes

You can use Spring Data, Hibernate, any other framework.
Make as much as you can, build stubs or write some comments if you cannot solve something.
upload the source code to a GitLab/GitHub repo and send us the URI.
