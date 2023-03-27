#!/bin/bash
docker run -it -d \
--name=mkovac_vjezba_04_1S \
--mount source=mkovac_podaci,target=/usr/app/podaci \
mkovac_vjezba_04_1:1.0.0 &
wait
