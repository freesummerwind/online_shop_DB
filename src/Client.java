import java.util.HashMap;

public class Client extends Person{
    private String address;
    private String cardNumber;
    private String phoneNumber;
    private HashMap<Product, Integer> basket;

    public Client(String name, String surname, int age, String address, String cardNumber, String phoneNumber) throws Exception {
        super(name, surname, age);
        setAddress(address);
        setCardNumber(cardNumber);
        setPhoneNumber(phoneNumber);
        basket = new HashMap<>();
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

    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    @Override
    String display() {
        return String.format("Full name: %s\nPhone: %s\nCard number: %s\nAddress: %s\n", getSurname() + " " + getName(),
                getPhoneNumber(), getCardNumber(), getAddress());
    }
}
