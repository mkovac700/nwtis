#!/bin/bash
echo "---POKRETAC DOCKER BAZA---"
echo "DOCKER NETWORK START:"
./scripts/pripremiMrezu.sh
echo "DOCKER STOP:"
docker stop nwtishsqldb_2
echo "DOCKER REMOVE:"
docker rm nwtishsqldb_2
echo "DOCKER PRIPREMI:"
./scripts/pripremiSliku.sh
echo "DOCKER POKRENI:"
./scripts/pokreniSliku.sh
echo "DOCKER PS:"
docker ps
