package carsharing.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataBase {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static String DB_URL = "jdbc:h2:file:E:\\Git\\Andrei-Biliuti\\Car Sharing\\Car Sharing\\task\\src\\carsharing\\db\\";

    static final String USER = "sa";
    static final String PASS = "";

    Connection conn = null;
    Statement stmt = null;
    public DataBase(String name) {
        try {
            DB_URL +=name;
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public void createCompanyTable() {
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS company" +
                    "(id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR UNIQUE NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public List<Company> companyList() {
        List<Company> companies = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM company ORDER BY id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                companies.add(new Company(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(companies.size() == 0) System.out.println("The company list is empty!");
        else {
            System.out.println("Choose the company:");
            for (Company company: companies) {
                System.out.printf("%d. %s\n", company.getId(), company.getName());
            }
            System.out.println("0. Back");
        }
        return companies;
    }

    public void createCompany() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO company (name) VALUES (?)");
            ps.setString(1, companyName);
            ps.executeUpdate();
            System.out.println("The company was created!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCarTable() {
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS car" +
                    "(id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR UNIQUE NOT NULL," +
                    "company_id INT NOT NULL," +
                    "FOREIGN KEY (company_id) REFERENCES company(id));";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> carList(int companyId) {
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement pr = conn.prepareStatement("SELECT * FROM car " +
                    "WHERE company_id = ? order by id;");
            pr.setInt(1, companyId);
            ResultSet rs = pr.executeQuery();
            while(rs.next()) {
                cars.add(new Car(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cars.size() == 0) System.out.println("The car list is empty!");
        else {
            int i =1;
            for (Car car: cars) {
                System.out.printf("%d. %s\n", i++, car.getName());
            }
        }
        return cars;
    }

    public void createCar(int companyId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the car name:");
        String input = scanner.nextLine();
        try {
            PreparedStatement pr = conn.prepareStatement("INSERT INTO car (name, company_id) VAlUES (? , ?);");
            pr.setString(1,input);
            pr.setInt(2, companyId);
            pr.executeUpdate();
            System.out.println("The car was added!\n");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void createCostumerTable () {
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS customer" +
                    "(id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR UNIQUE NOT NULL," +
                    "rented_car_id INT," +
                    "FOREIGN KEY (rented_car_id) REFERENCES car(id))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getCostumers() {
        List<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM customer ORDER BY id;");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                customers.add(new Customer(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public void createCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        try {
            PreparedStatement pr = conn.prepareStatement("INSERT INTO customer (name) VALUES (?);");
            pr.setString(1,customerName);
            pr.executeUpdate();
            System.out.println("The customer was added!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(int id) {
        Customer customer = null;
        try {
            PreparedStatement pr = conn.prepareStatement("SELECT * FROM customer WHERE id = (?);");
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()) customer = new Customer(rs.getInt(1), rs.getString(2), rs.getInt(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
    public void dropTables () {
        try {
            PreparedStatement pr = conn.prepareStatement("DROP TABlE company, car, customer;");
            pr.executeUpdate();
        } catch (Exception e) {

        }
    }
    public void closeDB() {
        try {
            if(conn!=null) conn.close();
            if(stmt!=null) stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void setRentalStatus(Customer customer, Integer id) {
        try {
            PreparedStatement pr = conn.prepareStatement("UPDATE customer " +
                    "SET rented_car_id = (?) " +
                    "WHERE name = (?);");
            if(id != null) pr.setInt(1,id);
            else pr.setString(1, null);
            pr.setString(2, customer.getName());
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Car getCar(Integer carId) {
        Car car = null;
        try {
            PreparedStatement pr = conn.prepareStatement("SELECT name, company_id FROM car WHERE id = (?);");
            pr.setInt(1, carId);
            ResultSet rs = pr.executeQuery();
            if(rs.next()) {
                car = new Car(0,rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    public String getCompanyNameById(int id) {
        try {
            PreparedStatement pr = conn.prepareStatement("SELECT name FROM company WHERE id = (?);");
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()) return rs.getString(1);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Car> availabelCar(Company company) {
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT car.id, car.name, car.company_id \n" +
                    "                    FROM car LEFT JOIN customer \n" +
                    "                    ON car.id = customer.rented_car_id \n" +
                    "                    WHERE customer.name IS NULL AND car.company_id = (?);");
            ps.setInt(1,company.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cars.add(new Car(rs.getInt(1), rs.getString(2), rs.getInt(3)) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cars.size() == 0) System.out.println("The company list is empty!");
        else {
            System.out.println("Choose the company:");
            for (Car car: cars) {
                System.out.printf("%d. %s\n", car.getId(), car.getName());
            }
            System.out.println("0. Back");
        }
        return cars;
    }
}