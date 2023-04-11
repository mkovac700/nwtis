#!/bin/bash
NETWORK=mkovac_mreza_1
docker run -it -d \
-p 8090:8080 \
--network=$NETWORK \
--ip 200.20.0.2 \
--name=mkovac_tomcat \
--hostname=mkovac_tomcat \
mkovac_tomcat:10.1.7 &
wait
