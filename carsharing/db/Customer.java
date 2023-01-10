package carsharing.db;

public class Customer {
    int id;
    String name;
    Integer rentedCarId;

    Customer(int id, String name, int rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }
}
