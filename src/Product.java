public class Product {
    private String name;
    private double price;
    private int quantityInStock;

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
}
