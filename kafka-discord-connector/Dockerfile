FROM lensesio/fast-data-dev
COPY pom.xml ./
CMD ["mkdir", "/opt/landoop/connectors/third-party/kafka-connect-discord"]
COPY target/discord-connect-0.1.9-SNAPSHOT-jar-with-dependencies.jar /opt/landoop/connectors/third-party/kafka-connect-discord/discord-connect-0.1.9.jar
