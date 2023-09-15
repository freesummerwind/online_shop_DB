import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Client extends Person {
    private final int id;
    private String address;
    private String cardNumber;
    private String phoneNumber;
    private HashMap<Product, Integer> basket;
    private static int clientsNumber = 0;


    public Client(String name, String surname, int age, String address, String cardNumber, String phoneNumber) throws Exception {
        super(name, surname, age);
        this.id = ++clientsNumber;
        setAddress(address);
        setCardNumber(cardNumber);
        setPhoneNumber(phoneNumber);
        basket = new HashMap<>();
    }

    public Client(JSONObject clientJSON, HashMap<Integer, Product> products) throws Exception {
        super(clientJSON.getString("name"), clientJSON.getString("surname"), clientJSON.getInt("age"));
        this.id = clientJSON.getInt("id");
        setAddress(clientJSON.getString("address"));
        setCardNumber(clientJSON.getString("cardNumber"));
        setPhoneNumber(clientJSON.getString("phoneNumber"));
        basket = new HashMap<>();
        JSONArray basketJSON = clientJSON.getJSONArray("basket");
        for (int i = 0; i < basketJSON.length(); ++i) {
            JSONObject productJSON = (JSONObject) basketJSON.get(i);
            int productID = productJSON.getInt("id");
            if (!products.containsKey(productID)) {
                throw new Exception("No product with such ID: " + Integer.toString(productID));
            }
            int productAmount = productJSON.getInt("number");
            basket.put(products.get(productID), productAmount);
        }
        clientsNumber++;
    }

    public void setAddress(String address) throws Exception {
        if (address.isEmpty()) {
            throw new Exception("Address can't be empty");
        }
        this.address = address;
    }

    public void setCardNumber(String cardNumber) throws Exception {
        if (!cardNumber.matches("[0-9]+")) {
            throw new Exception("Card number should contains only digits");
        }
        if (cardNumber.length() != 16) {
            throw new Exception("Card number should contains 16 digits");
        }
        this.cardNumber = cardNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws Exception {
        if (!phoneNumber.matches("[0-9]+")) {
            throw new Exception("Phone number should contains only digits");
        }
        if (phoneNumber.length() != 11) {
            throw new Exception("Card number should contains 11 digits");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setBasket(HashMap<Product, Integer> basket) {
        this.basket = basket;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public HashMap<Product, Integer> getBasket() {
        return basket;
    }

    public void addProductToBasket(Product product, int number) throws Exception {
        if (basket.containsKey(product) && (basket.get(product) + number) > product.getQuantityInStock()) {
            throw new Exception("There can't be more products in the basket than on the stock");
        }
        if (!basket.containsKey(product) && number > product.getQuantityInStock()) {
            throw new Exception("There can't be more products in the basket than on the stock");
        }
        if (number <= 0) {
            throw new Exception("Number pf products should be positive");
        }
        if (basket.containsKey(product)) {
            basket.put(product, basket.get(product) + number);
        } else {
            basket.put(product, number);
        }
    }

    public void deleteProductFromBasket(Product product, int number) {
        if (basket.containsKey(product)) {
            if (number < basket.get(product)) {
                basket.put(product, basket.get(product) - number);
            } else {
                basket.remove(product);
            }
        }
    }

    public Order createNewOrder() throws Exception {
        Order order = new Order(this, basket);
        basket.clear();
        return order;
    }

    @Override
    String display() {
        return String.format("ID:%d\nFull name: %s\nPhone: %s\nCard number: %s\nAddress: %s\n", getId(),
                getSurname() + " " + getName(), getPhoneNumber(), getCardNumber(), getAddress());
    }

    public JSONObject toJSON() {
        JSONObject client = new JSONObject();
        client.put("id", getId());
        client.put("name", getName());
        client.put("surname", getSurname());
        client.put("age", getAge());
        client.put("address", getAddress());
        client.put("cardNumber", getCardNumber());
        client.put("phoneNumber", getPhoneNumber());
        JSONArray productsInBasket = new JSONArray();
        for (Product product : basket.keySet()) {
            JSONObject current = new JSONObject();
            current.put("id", product.getId());
            current.put("number", basket.get(product));
            productsInBasket.put(current);
        }
        client.put("basket", productsInBasket);
        return client;
    }
}
