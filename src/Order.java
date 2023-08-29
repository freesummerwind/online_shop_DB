import java.util.HashMap;

public class Order {
    final private String[] statusList = {"formed", "paid", "delivered"};
    private Client customer;
    private HashMap<Product, Integer> products;
    private String status;

    public Order(Client customer, HashMap<Product, Integer> products) throws Exception {
        setCustomer(customer);
        this.products = new HashMap<>();
        setProducts(products);
        setStatus("formed");
    }

    public void setCustomer(Client customer) {
        this.customer = customer;
    }

    public void setProducts(HashMap<Product, Integer> products) throws Exception {
        if (products.isEmpty()) {
            throw new Exception("Order should consist at least one product");
        }
        if (status.equals("paid") || status.equals("delivered")) {
            throw new Exception("Can't change paid or delivered order");
        }
        // clean current products
        cleanProducts();
        // add new products
        for (Product product : products.keySet()) {
            product.sellProductsFromStock(products.get(product));
            this.products.put(product, products.get(product));
        }
    }

    public void setStatus(String status) throws Exception {
        if (!(status.equals(statusList[0]) || status.equals(statusList[1]) || status.equals(statusList[2]))) {
            throw new Exception("Invalid status");
        }
        this.status = status;
    }

    public Client getCustomer() {
        return customer;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }

    public void cleanProducts() {
        for (Product product : this.products.keySet()) {
            product.addProductsToStock(this.products.get(product));
        }
        this.products.clear();
    }

    public double getTotalAmount() {
        double totalAmount = 0.0;
        for (Product product : products.keySet()) {
            totalAmount += product.getPrice() * products.get(product);
        }
        return totalAmount;
    }

    public String display() {
        String productsInfo = "";
        for (Product product : products.keySet()) {
            productsInfo += String.format("Product: %s, price: %f * %d = %f\n", product.getName(), product.getPrice(),
                    products.get(product), product.getPrice() * products.get(product));
        }
        return String.format("Client info:\n%s\nProducts info:\n%s\nTotal Amount: %f\nStatus: %s\n", customer.display(),
                productsInfo, getTotalAmount(), getStatus());
    }
}
