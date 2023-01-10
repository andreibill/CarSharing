package carsharing.db;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    DataBase db;
    Scanner scanner = new Scanner(System.in);

    CustomerMenu(DataBase db) {
        this.db = db;
    }

    protected void loginAsCustomer() {
        List<Customer> customers = db.getCostumers();
        if(customers.size() == 0) {
            System.out.println("The customer list is empty!\n");
            return;
        }
        System.out.println("Customer list:");
        for (Customer customer : customers) {
            System.out.printf("%d. %s\n", customer.getId(), customer.getName());
        }
        System.out.println("0. Back");
        int input = scanner.nextInt();
        if(input == 0) return;
        customer(input);
    }

    private void customer(int id) {
        Customer customer = db.getCustomerById(id);
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
        int input = scanner.nextInt();
        switch (input) {
            case 1 -> rentACar(customer);
            case 2 -> returnCar(customer);
            case 3 -> myRentedCar(customer.getRentedCarId());
            case 0 -> {
                return;
            }
        }
        customer(id);
    }

    private void returnCar(Customer customer) {
        if(customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            return;
        }
        db.setRentalStatus(customer, null);
        System.out.println("You've returned a rented car!");
    }

    private void myRentedCar(Integer carId) {
        if(carId == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }
        Car car = db.getCar(carId);
        String companyName = db.getCompanyNameById(car.getCompanyId());
        System.out.printf("Your rented car:\n" +
                "%s\n" +
                "Company:\n" +
                "%s\n" , car.getName(), companyName);
    }

    private void rentACar(Customer customer) {
        if(customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            return;
        }
        List<Company> companies = db.companyList();
        int input = scanner.nextInt();
        List<Car> cars = db.availabelCar(companies.get(input-1));
        System.out.println("0. Back");
        scanner.nextInt();
        System.out.printf("You rented '%s'\n", cars.get(input-1).getName());
        db.setRentalStatus(customer, cars.get(input-1).getId());
    }
}
/// rent a car function