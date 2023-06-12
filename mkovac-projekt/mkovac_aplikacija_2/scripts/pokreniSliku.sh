#!/bin/bash
NETWORK=mkovac_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=mkovac_payara_micro \
  --hostname=mkovac_payara_micro \
  mkovac_payara_micro:6.2023.4 \
  --deploy /opt/payara/deployments/mkovac_aplikacija_2-1.0.0.war \
  --contextroot mkovac_aplikacija_2 \
  --noCluster &

wait
