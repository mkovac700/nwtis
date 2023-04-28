#!/bin/bash
echo "---POKRETAC LOCALHOST SERVER---"
export JAVA_HOME=/usr/lib/jvm/jdk-17.0.2
export PATH=/usr/lib/jvm/jdk-17.0.2/bin:$PATH
java -version
cd /opt/payara-web-6.2023.1/glassfish/bin
./asadmin start-domain
