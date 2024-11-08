import java.util.Scanner;

public class salesuser {
    private Manager manager;
    private Product[] products = new Product[3];
    private Sales[] sales = new Sales[50];
    private int saleCounter = 0;
    private Scanner scanner = new Scanner(System.in);

    public salesuser() {
        initializeProductsAndSuppliers();
        displayMenu();
    }

    private void initializeProductsAndSuppliers() {
        Supplier[][] suppliers = new Supplier[3][2];

        suppliers[0][0] = new Supplier("Supplier A", "Ram", "India", 9090);
        suppliers[0][1] = new Supplier("Supplier B", "Harry", "Australia", 9090);
        suppliers[1][0] = new Supplier("Supplier A", "Ram", "India", 9090);
        suppliers[1][1] = new Supplier("Supplier B", "Harry", "Australia", 9090);
        suppliers[2][0] = new Supplier("Supplier A", "Ram", "India", 9090);
        suppliers[2][1] = new Supplier("Supplier B", "Harry", "Australia", 9090);

        products[0] = new Product(1, "Product 1", 10.00F, 50, suppliers[0]);
        products[1] = new Product(2, "Product 2", 9.00F, 50, suppliers[1]);
        products[2] = new Product(3, "Product 3", 8.00F, 50, suppliers[2]);

        manager = new Manager();
    }

    public void displayMenu() {
        System.out.print("Select user type:\n1 MANAGER\n2 SALES STAFF\n3 CUSTOMER\n");
        int userType = scanner.nextInt();
        handleUserType(userType);
    }

    private void handleUserType(int userType) {
        switch (userType) {
            case 1:
                handleManagerLogin();
                break;
            case 2:
                handleSalesStaffLogin();
                break;
            case 3:
                handleCustomerInteraction();
                break;
            default:
                System.out.println("WRONG ENTRY");
                break;
        }
    }

    private void handleManagerLogin() {
        System.out.print("TYPE PASSWORD FOR MANAGER: ");
        String password = scanner.next();
        if (password.equalsIgnoreCase("AUSSIE")) {
            System.out.println("CORRECT");
            managerMenu();
        } else {
            System.out.println("INCORRECT PASSWORD");
        }
    }

    private void managerMenu() {
        while (true) {
            System.out.println("ENTER YOUR CHOICE AS 1, 2, 3, 4, 5");
            System.out.println("1 - DISCOUNT CREATION\n2 - GENERATE SALES REPORT\n3 - SET PRICE\n4 - PRODUCT LIST\n5 - Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    createDiscount();
                    break;
                case 2:
                    generateSalesReport();
                    break;
                case 3:
                    setPrice();
                    break;
                case 4:
                    showProductList();
                    break;
                case 5:
                    return; // Exit manager menu
                default:
                    System.out.println("You entered the wrong choice");
            }
        }
    }

    private void createDiscount() {
        System.out.println("ENTER THE NUMBER OF ITEMS FOR DISCOUNT PERCENTAGE:");
        int itemCount = scanner.nextInt();
        System.out.println(manager.promotionalDiscount(itemCount) + "% discount applied.");
    }

    private void generateSalesReport() {
        System.out.println("Sales report:");
        manager.generateReport(sales, saleCounter);
    }

    private void setPrice() {
        System.out.println("Enter the Product ID:");
        int pid = scanner.nextInt();
        System.out.println("Enter the amount to be promoted/deducted:");
        int amount = scanner.nextInt();

        for (Product product : products) {
            if (product.getPid() == pid) {
                float newPrice = manager.setPrice(product, amount);
                System.out.println("The new price for product " + pid + " is " + newPrice);
                return;
            }
        }
        System.out.println("Product not found.");
    }

    private void showProductList() {
        for (Product product : products) {
            System.out.println("Product ID: " + product.getPid() +
                    ", Name: " + product.getPName() +
                    ", Price: " + product.getPrice() +
                    ", Quantity in hand: " + product.getStock());
        }
    }

    private void handleSalesStaffLogin() {
        System.out.print("TYPE PASSWORD FOR SALES STAFF: ");
        String password = scanner.next();
        if (password.equalsIgnoreCase("SPORTS")) {
            System.out.println("CORRECT");
            // Sales staff menu can be implemented here
        } else {
            System.out.println("INCORRECT PASSWORD");
        }
    }

    private void handleCustomerInteraction() {
        Customer customer = new Customer(products);
        while (true) {
            System.out.println("Enter the product ID to see the price:");
            int productId = scanner.nextInt();
            customer.showPrice(productId);

            System.out.println("Enter the number of items you want to purchase:");
            int items = scanner.nextInt();
            for (Product product : products) {
                if (product.getPid() == productId) {
                    product.setStock(product.getStock() - items);
                    float totalAmount = calculateTotalAmount(product, items);
                    System.out.println("Total Amount is: " + totalAmount);
                    sales[saleCounter++] = new Sales(productId, items, product.getPrice(), product.getSuppliers()[0].getName());
                    break;
                }
            }
            System.out.println("Do you want to purchase more items? (Y/N)");
            String option = scanner.next();
            if (option.equalsIgnoreCase("N")) {
                break;
            }
        }
    }

    private float calculateTotalAmount(Product product, int items) {
        float discount = manager.promotionalDiscount(items) / 100.0F;
        float totalPrice = product.getPrice() * items;
        return totalPrice - (totalPrice * discount);
    }

    public static void main(String[] args) {
         new salesuser();
    }

    // Define other classes below

    static class Supplier {
        private String name;
        private String person;
        private String address;
        private long phoneNo;

        public Supplier(String name, String person, String address, long phoneNo) {
            this.name = name;
            this.person = person;
            this.address = address;
            this.phoneNo = phoneNo;
        }

        public String getName() {
            return name;
        }
    }

    static class Customer {
        private Product[] products;

        public Customer(Product[] products) {
            this.products = products;
            for (Product product : products) {
                System.out.println("Product ID: " + product.getPid() + ", Name: " + product.getPName());
            }
        }

        public void showPrice(int id) {
            for (Product product : products) {
                if (product.getPid() == id) {
                    System.out.println("Product ID: " + product.getPid() +
                            ", Name: " + product.getPName() +
                            ", Price: " + product.getPrice());
                    return;
                }
            }
            System.out.println("Product not found.");
        }
    }

    static class Product {
        private int pid;
        private String pName;
        private float price;
        private int stock;
        private Supplier[] suppliers;

        public Product(int pid, String name, float price, int stock, Supplier[] suppliers) {
            this.pid = pid;
            this.pName = name;
            this.price = price;
            this.stock = stock;
            this.suppliers = suppliers;
        }

        public int getPid() {
            return pid;
        }

        public String getPName() {
            return pName;
        }

        public float getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public Supplier[] getSuppliers() {
            return suppliers;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    static class Sales {
        private int productId;
        private int totalSales;
        private float unitPrice;
        private String supplierName;

        public Sales(int productId, int totalSales, float unitPrice, String supplierName) {
            this.productId = productId;
            this.totalSales = totalSales;
            this.unitPrice = unitPrice;
            this.supplierName = supplierName;
        }
    }

    static class Manager {
        private static final int REP_LEVEL = 5;

        public float setPrice(Product product, int amount) {
            return product.getPrice() - amount;
        }

        public int promotionalDiscount(int itemsSold) {
            if (itemsSold > 5 && itemsSold < 10) {
                return 10;
            }
            if (itemsSold >= 10) {
                return 20;
            }
            return 0;
        }

        public void generateReport(Sales[] sales, int saleCounter) {
            for (int i = 0; i < saleCounter; i++) {
                Sales sale = sales[i];
                System.out.println("Product ID: " + sale.productId +
                        ", Total Units Sold: " + sale.totalSales +
                        ", Total Amount: " + (sale.totalSales * sale.unitPrice));
            }
        }
    }
}
