#!/bin/bash -e

# Kafka 0.9.x.x has a 'listeners' config by default. We need to remove this
# as the user may be configuring via the host.name / advertised.host.name properties
echo "Removing 'listeners' from server.properties pre-bootstrap"
sed -i -e '/^listeners=/d' "$KAFKA_HOME/config/server.properties"

#echo "Defining the plugin.path"
#sed -i -e '$ a plugin.path=/opt/connectors' "$KAFKA_HOME/config/connect-standalone.properties"
#sed -i '$ a plugin.path=/opt/connectors' $KAFKA_HOME/config/connect-standalone.properties