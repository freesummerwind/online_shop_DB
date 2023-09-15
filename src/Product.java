import org.json.JSONObject;

public class Product {
    private final int id;
    private String name;
    private double price;
    private int quantityInStock;
    private static int productsNumber = 0;

    public Product(String name, double price, int quantityInStock) throws Exception {
        id = ++productsNumber;
        setName(name);
        setPrice(price);
        setQuantityInStock(quantityInStock);
    }

    public Product(JSONObject productJSON) throws Exception {
        this.id = productJSON.getInt("id");
        setName(productJSON.getString("name"));
        setPrice(productJSON.getDouble("price"));
        setQuantityInStock(productJSON.getInt("quantityInStock"));
        ++productsNumber;
    }

    public void setName(String name) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("Name can't be empty");
        }
        this.name = name;
    }

    public void setPrice(double price) throws Exception {
        if (price <= 0) {
            throw new Exception("Price should be positive");
        }
        this.price = price;
    }

    public void setQuantityInStock(int quantityInStock) throws Exception {
        if (quantityInStock < 0) {
            throw new Exception("Quantity should be positive or null");
        }
        this.quantityInStock = quantityInStock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void addProductsToStock(int productsNumber) {
        this.quantityInStock += productsNumber;
    }

    public void sellProductsFromStock(int productsNumber) throws Exception {
        if (productsNumber > quantityInStock) {
            throw new Exception("Can't sell more products than there are on stock");
        }
        this.quantityInStock -= productsNumber;
    }

    public String display() {
        return String.format("ID: %d\nname: %s\nPrice: %f\nQuantity on stock: %d\n",
                getId(), getName(), getPrice(), getQuantityInStock());
    }

    public JSONObject toJSON() {
        JSONObject product = new JSONObject();
        product.put("id", getId());
        product.put("name", getName());
        product.put("price", getPrice());
        product.put("quantityInStock", getQuantityInStock());
        return product;
    }
}
