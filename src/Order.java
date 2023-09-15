import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Order {
    static final private String[] statusList = {"formed", "paid", "delivered"};

    private final int id;
    private Client customer;
    private HashMap<Product, Integer> products;
    private String status;

    private static int ordersNumber = 0;

    public Order(Client customer, HashMap<Product, Integer> products) throws Exception {
        this.id = ++ordersNumber;
        setCustomer(customer);
        setStatus("formed");
        this.products = new HashMap<>();
        setProducts(products);
    }

    public Order(JSONObject orderJSON, HashMap<Integer, Client> clients, HashMap<Integer, Product> products) throws Exception {
        this.id = orderJSON.getInt("id");
        setCustomer(clients.get(orderJSON.getInt("customer")));
        setStatus(orderJSON.getString("status"));
        this.products = new HashMap<>();
        JSONArray productsJSON = orderJSON.getJSONArray("products");
        for (int i = 0; i < productsJSON.length(); ++i) {
            JSONObject productJSON = (JSONObject) productsJSON.get(i);
            int productID = productJSON.getInt("id");
            if (!products.containsKey(productID)) {
                throw new Exception("No product with such ID: " + Integer.toString(productID));
            }
            int productAmount = productJSON.getInt("number");
            this.products.put(products.get(productID), productAmount);
        }
        ordersNumber++;
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

    public int getId() {
        return id;
    }

    Client getCustomer() {
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
        return String.format("Order:%d\nClient info:\n%s\nProducts info:\n%s\nTotal Amount: %f\nStatus: %s\n", getId(),
                customer.display(), productsInfo, getTotalAmount(), getStatus());
    }

    public JSONObject toJSON() {
        JSONObject order = new JSONObject();
        order.put("id", getId());
        order.put("customer", customer.getId());
        order.put("status", getStatus());
        JSONArray productsInOrder = new JSONArray();
        for (Product product : products.keySet()) {
            JSONObject current = new JSONObject();
            current.put("id", product.getId());
            current.put("number", products.get(product));
            productsInOrder.put(current);
        }
        order.put("products", productsInOrder);
        return order;

    }
}
