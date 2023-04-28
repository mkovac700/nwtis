#!/bin/bash
echo "---POKRETAC DOCKER SERVER---"
echo "DOCKER STOP:"
docker stop mkovac_payara_micro
echo "DOCKER REMOVE:"
docker rm mkovac_payara_micro
echo "DOCKER PRIPREMI:"
./scripts/pripremiSliku.sh
echo "DOCKER POKRENI:"
./scripts/pokreniSliku.sh
echo "DOCKER PS:"
docker ps
