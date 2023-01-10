package carsharing.db;

import java.util.List;
import java.util.Scanner;

public class CompanyMenu {
    DataBase db;
    Scanner scanner = new Scanner(System.in);

    CompanyMenu (DataBase db) {
        this.db = db;
    }
    protected void managerMenu() {
        int input;
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
        input = scanner.nextInt();
        System.out.println();
        if(input == 0) return;
        switch (input) {
            case 1:
                companiesList();
                break;
            case 2:
                db.createCompany();
                break;
            case 3:
                db.dropTables();
                System.out.println("tables have been droped");
                return;
        }
        managerMenu();
    }

    private void companiesList() {
        List<Company> companies = db.companyList();
        if(companies.size() > 0){
            int input = scanner.nextInt();
            System.out.println();
            if(input == 0) return;
            System.out.printf("'%s' company\n", companies.get(input-1).getName());
            companyMenu(companies.get(input-1));
        }
    }

    private void companyMenu(Company company) {
        System.out.printf("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back\n", company.getName());
        int input = scanner.nextInt();
        switch (input) {
            case 1:
                db.carList(company.getId());
                break;
            case 2:
                db.createCar(company.getId());
                break;
            case 0:
                return;
        }
        companyMenu(company);
    }
}
