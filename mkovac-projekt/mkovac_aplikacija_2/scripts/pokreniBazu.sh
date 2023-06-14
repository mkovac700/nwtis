#!/bin/bash
cd /opt/hsqldb-2.7.1/hsqldb/data
sudo java -classpath ../lib/hsqldb.jar org.hsqldb.server.Server \
--database.0 file:nwtis_2 --dbname.0 nwtis_2 --port 9001
