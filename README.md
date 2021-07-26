# adi-kafka-poc

## Build connector from Maven archetype
`mvn archetype:generate  -DarchetypeGroupId=org.apache.camel.kafkaconnector.archetypes  -DarchetypeArtifactId=camel-kafka-connector-extensible-archetype  -DarchetypeVersion=0.7.0`

## Kafka + Zookeeper + Camel Kafka Netty HTTP Connector
1. In netty-http-extended: `mvn clean package`
2. Build Docker image: `docker build . -t adi-kafka-poc:latest`
3. Run containers: `docker-compose up -d`

## Run Kafka Connect-standalone:
`$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties /opt/config/CamelNettyhttpSinkConnector.properties`

`$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties /opt/config/CamelNettyhttpSourceConnector.properties`

`$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties /opt/config/CamelNettyhttpSinkConnector.properties /opt/config/CamelNettyhttpSourceConnector.properties`

# Request for Source Connector
`curl --header "Content-Type: application/json" --request POST \--data '{"username":"xyz","password":"xyz"}' http://localhost:8093`

## Jaeger
Build:
`docker run -d --name jaeger \
-e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
-p 5775:5775/udp \
-p 6831:6831/udp \
-p 6832:6832/udp \
-p 5778:5778 \
-p 16686:16686 \
-p 14268:14268 \
-p 14250:14250 \
-p 9411:9411 \
jaegertracing/all-in-one:1.24`

Run:
`docker run -d --name jaeger -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 -p 14250:14250 -p 9411:9411 jaegertracing/all-in-one:1.24`
 
