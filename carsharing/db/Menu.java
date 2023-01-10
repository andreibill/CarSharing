package carsharing.db;

import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    DataBase db;
    CompanyMenu companyMenu;
    CustomerMenu customerMenu;
    public Menu(DataBase db) {
        this.db = db;
        companyMenu = new CompanyMenu(db);
        customerMenu = new CustomerMenu(db);
        mainMenu();
    }
    private void mainMenu() {
        int input;

        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
        input = scanner.nextInt();
        System.out.println();
        switch (input) {
            case 1:
                companyMenu.managerMenu();
                break;
            case 2:
                customerMenu.loginAsCustomer();
                break;
            case 3:
                db.createCustomer();
                break;
            case 0:
                return;
        }
        mainMenu();
    }
}