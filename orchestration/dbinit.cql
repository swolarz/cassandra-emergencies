
create keyspace if not exists emergencies
    with replication = {'class': 'SimpleStrategy', 'replication_factor': '1'} and durable_writes = true;



