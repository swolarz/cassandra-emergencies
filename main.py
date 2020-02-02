if __name__ == "__main__":
    print("CREATE KEYSPACE IF NOT EXISTS emergencies ")
    print("  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };")
    print("  USE emergencies;")

    print(" CREATE TABLE FireTrucks ( " +
          " BrigadeId int, " +
          " TypeId int, " +
          " Assigned boolean, " +
          " TruckId int, " +
          " PRIMARY KEY ((BrigadeId, TypeId), Assigned));")

    print(" CREATE TABLE Assignments ( " +
          " TruckId int, " +
          " Timestamp timestamp, " +
          " RequestId text, " +
          " PRIMARY KEY ((TruckId), Timestamp, RequestId);")

    truck_id = 1
    for i in range(1, 1001):
        for j in range(1, 7):
            print("insert into FireTrucks (BrigadeId, TypeId, Assigned, TruckId) values "
                  + str.format(" ({0}, {1}, false, {2});", i, j, truck_id))
            truck_id += 1

