FROM azul/zulu-openjdk-alpine:8u292-8.54.0.21

ARG kafka_version=2.7.0
ARG scala_version=2.13
ARG glibc_version=2.33-r0
ARG vcs_ref=unspecified
ARG build_date=unspecified

LABEL org.label-schema.name="kafka" \
      org.label-schema.description="Apache Kafka" \
      org.label-schema.build-date="${build_date}" \
      org.label-schema.vcs-url="https://github.com/wurstmeister/kafka-docker" \
      org.label-schema.vcs-ref="${vcs_ref}" \
      org.label-schema.version="${scala_version}_${kafka_version}" \
      org.label-schema.schema-version="1.0" \
      maintainer="wurstmeister"

ENV KAFKA_VERSION=$kafka_version \
    SCALA_VERSION=$scala_version \
    KAFKA_HOME=/opt/kafka \
    GLIBC_VERSION=$glibc_version

ENV PATH=${PATH}:${KAFKA_HOME}/bin

# Copy connector
RUN mkdir -p /opt/connectors
# Default connector
#COPY ./camel-netty-http-kafka-connector-0.7.0-package.tar.gz /opt/connectors
#RUN tar -xvzf /opt/connectors/camel-netty-http-kafka-connector-0.7.0-package.tar.gz --directory /opt/connectors
#RUN rm /opt/connectors/camel-netty-http-kafka-connector-0.7.0-package.tar.gz

# Extended Camel Kafka Netty HTTP connector
COPY ./netty-http-extended/target/netty-http-extended-0.7.0-package.tar.gz /opt/connectors
RUN tar -xvzf /opt/connectors/netty-http-extended-0.7.0-package.tar.gz --directory /opt/connectors
RUN rm /opt/connectors/netty-http-extended-0.7.0-package.tar.gz

# Custom Sink connector
COPY ./custom-sink-connector/target/custom-sink-connector-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/connectors
COPY ./custom-sink-connector/custom-sink-connector.json /opt/connectors

# Custom Source connector
COPY ./custom-source-connector/target/custom-source-connector-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/connectors
COPY ./custom-source-connector/custom-source-connector.json /opt/connectors

#Copy connector properties
RUN mkdir -p /opt/config
COPY ./CamelNettyhttpSinkConnector.properties /opt/config
COPY ./CamelNettyhttpSourceConnector.properties /opt/config
COPY ./custom-sink-connector/CustomSinkConnector.properties /opt/config
COPY ./custom-source-connector/CustomSourceConnector.properties /opt/config

COPY download-kafka.sh start-kafka.sh broker-list.sh create-topics.sh versions.sh /tmp/

RUN apk add --no-cache bash curl jq docker
RUN chmod a+x /tmp/*.sh
RUN mv /tmp/start-kafka.sh /tmp/broker-list.sh /tmp/create-topics.sh /tmp/versions.sh /usr/bin
RUN sync && /tmp/download-kafka.sh
RUN tar xfz /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /opt
RUN rm /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz
RUN ln -s /opt/kafka_${SCALA_VERSION}-${KAFKA_VERSION} ${KAFKA_HOME}
RUN rm /tmp/*
RUN wget --no-check-certificate https://github.com/sgerrand/alpine-pkg-glibc/releases/download/${GLIBC_VERSION}/glibc-${GLIBC_VERSION}.apk
RUN apk add --no-cache --allow-untrusted glibc-${GLIBC_VERSION}.apk
RUN rm glibc-${GLIBC_VERSION}.apk

COPY overrides /opt/overrides

VOLUME ["/kafka"]

# Update properties
RUN sed -i "$ a plugin.path=/opt/connectors" $KAFKA_HOME/config/connect-standalone.properties

# Use "exec" form so that it runs as PID 1 (useful for graceful shutdown)
CMD ["start-kafka.sh"]
