# kafka-discord-connector
Will read all messages from a Discord Server and dump them into a single Kafka topic for further processing. 

Launches a custom Docker image built ontop of the Landoop Kafka Development image. Simply loads the file being built in this environment. 

## Pre-reqs
* A (free) discord server
* A bot account setup for that server (and therefore a token)
The information on the above can be learned from the following [Discord4j Link](https://docs.discord4j.com/discord-application-tutorial)

## Building
Run build.sh, assumes you have maven & docker running

## Running
1. docker-compose up
1. Navigate to http://127.0.0.1:3030
1. Open the "Kafka Connect UI" and click "Add"
1. Select "DiscordSourceConnector"
1. Fill in the "topic name" & "token"
1. Click "Create"



## Status
Currently, the connector launches and collects messages from Discord and now submits them into Kafka. AKA, we've reached
MVP for this project. Messages now enter the defined kafka topic. 

### Next Steps
* Move the build process into Docker so that we can run "docker build" and everything just happens.  
* Start moving more message information into Kafka. Currently, we just do the content, we don't actually collect who 
 said it, when they said it, or other meta information
* Focus a more "production" style launch for this image. IE: If we want to build an image which will just run with 
things like token information, likely will require using a different base docker image
* Once we've gotten to a more production style for the source connector

