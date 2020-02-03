FROM cassandra:3.11

WORKDIR /usr/app

COPY dbinit.cql datainit.sh ./

ENV CASSY_HOST=localhost

ENTRYPOINT ["/bin/bash", "-c", "./datainit.sh ${CASSY_HOST}"]

