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
. docker-compose up
. Navigate to http://127.0.0.1:3030
. Open the "Kafka Connect UI" and click "Add"
. Select "DiscordSourceConnector"
. Fill in the "topic name" & "token"
. Click "Create"



## Status
Currently, the connector launches and collects messages from Discord but is not actually dropping them into Kafka. This is likely due to the Discord4j API blocking the thread. 

