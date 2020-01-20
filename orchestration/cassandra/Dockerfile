FROM cassandra:3.11

WORKDIR /usr/app

COPY dbinit.cql datainit.sh ./

ENTRYPOINT ["./datainit.sh"]

