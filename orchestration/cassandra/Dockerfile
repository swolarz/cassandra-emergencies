FROM cassandra:3.11

WORKDIR /usr/app

COPY create_schema.cql load_data.cql drop_schema.cql datainit.sh ./

ENTRYPOINT ["./datainit.sh"]

