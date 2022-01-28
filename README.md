
# Book Search Server

This is a project of client-server application written in Java.
The server is responsible for creating a database of book phrases in Elasticsearch and
handling client connection. Client logs in with correct credentials and
enters a phrase to search and gets the response on his screen.




## Tech Stack

**Client, Server:** Java

**Docker containers:** Elasticsearch, Kibana


## Run the project

Before running the project you must have Docker installed.
You will create Docker containers with docker-compose.

```bash
  cd project/directory
  docker-compose up -d
```
    
After that Docker will pull the images and create containers and run them.
That might take a bit of time. 
You will see if this is ready when you can access either Elasticsearch or Kibana.

Access Elasticsearch on port 9200

```http
  http://localhost:9200/
```

Access Kibana on port 5601

```http
  http://localhost:5601/
```

If you have a problem with Elasticsearch because of lack of memory for Docker just run this line in command line in Linux.

```bash
  sudo sysctl -w vm.max_map_count=262144
```

Finally run Maven and pull dependencies from Maven repositories.

Last thing to do is to add 'resources' directory as your working directory for your client and server in configuration.
## Features

**Client**

- Login/Register/Forgot password using data from 'database.xml'
- Query phrase to search in Elasticsearch index
- Get response on screen

**Server**

- Create index 'books' in Elasticsearch
- Fill index with lines of different books parsed to JSON format
- Handle client connection and queries.

## Support

This project was written in Intellij and dependencies were injected using Maven.
I used Ubuntu 20.04 and Docker on Linux, but it should work also on Windows, provided you have installed Docker Desktop.

