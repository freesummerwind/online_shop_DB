import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static HashMap<Integer, Client> clients;
    static HashMap<Integer, Product> products;
    static HashMap<Integer, Order> orders;

    public static void main(String[] args) {
        try {
            getDBFromFiles();
            Scanner sc = new Scanner(System.in);
            while (true) {
                String command = sc.nextLine();
                if (command.startsWith("добавить клиента")) {
                    System.out.print("Введите имя: ");
                    String name = sc.nextLine().strip();
                    System.out.print("Введите фамилию: ");
                    String surname = sc.nextLine().strip();
                    System.out.print("Введите возраст: ");
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.print("Введите адрес: ");
                    String address = sc.nextLine().strip();
                    System.out.print("Введите номер карты: ");
                    String card = sc.nextLine().strip();
                    System.out.print("Введите номер телефона: ");
                    String phone = sc.nextLine().strip();
                    Client newClient = new Client(name, surname, age, address, card, phone);
                    clients.put(newClient.getId(), newClient);
                } else if (command.startsWith("добавить товар")) {
                    System.out.print("Введите наименование товара: ");
                    String name = sc.nextLine().strip();
                    System.out.print("Введите цену: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print("Введите количество товара на складе: ");
                    int quantity = Integer.parseInt(sc.nextLine());
                    Product newProduct = new Product(name, price, quantity);
                    products.put(newProduct.getId(), newProduct);
                } else if (command.startsWith("создать заказ")) {
                    System.out.print("Введите id клиента: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Order newOrder = clients.get(id).createNewOrder();
                    orders.put(newOrder.getId(), newOrder);
                } else if (command.startsWith("изменить клиента")) {
                    System.out.print("Введите id клиента: ");
                    int clientID = Integer.parseInt(sc.nextLine());
                    if (!clients.containsKey(clientID)) {
                        System.out.println("нет клиента с таким id " + clientID);
                        continue;
                    }
                    System.out.print("Введите название изменяемого поля: ");
                    String field = sc.nextLine().strip();
                    System.out.print("Введите новое значение: ");
                    String newValue = sc.nextLine().strip();
                    switch (field) {
                        case "имя" -> clients.get(clientID).setName(newValue);
                        case "фамилия" -> clients.get(clientID).setSurname(newValue);
                        case "возраст" -> clients.get(clientID).setAge(Integer.parseInt(newValue));
                        case "адрес" -> clients.get(clientID).setAddress(newValue);
                        case "карта" -> clients.get(clientID).setCardNumber(newValue);
                        case "телефон" -> clients.get(clientID).setPhoneNumber(newValue);
                        default -> System.out.println("неизвестная команда");
                    }
                } else if (command.startsWith("изменить товар")) {
                    System.out.print("Введите id товара: ");
                    int productID = Integer.parseInt(sc.nextLine());
                    if (!products.containsKey(productID)) {
                        System.out.println("нет товара с таким id " + productID);
                        continue;
                    }
                    System.out.print("Введите название изменяемого поля: ");
                    String field = sc.nextLine().strip();
                    System.out.print("Введите новое значение: ");
                    String newValue = sc.nextLine().strip();
                    switch (field) {
                        case "название" -> products.get(productID).setName(newValue);
                        case "цена" -> products.get(productID).setPrice(Double.parseDouble(newValue));
                        case "количество" ->
                                products.get(productID).setQuantityInStock(Integer.parseInt(newValue));
                        default -> System.out.println("неизвестная команда");
                    }
                } else if (command.startsWith("изменить заказ")) {
                    System.out.print("Введите id заказа: ");
                    int orderID = Integer.parseInt(sc.nextLine());
                    if (!orders.containsKey(orderID)) {
                        System.out.println("нет заказа с таким id " + orderID);
                        continue;
                    }
                    System.out.print("Введите название изменяемого поля: ");
                    String field = sc.nextLine().strip();
                    System.out.print("Введите новое значение: ");
                    String newValue = sc.nextLine().strip();
                    if (field.equals("статус")) {
                        orders.get(orderID).setStatus(newValue);
                    } else {
                        System.out.println("неизвестная команда");
                    }
                } else if (command.startsWith("изменить корзину")) {
                    System.out.print("Введите id клиента: ");
                    int clientID = Integer.parseInt(sc.nextLine());
                    if (!clients.containsKey(clientID)) {
                        System.out.println("нет клиента с таким id " + clientID);
                        continue;
                    }
                    System.out.print("Введите id товара: ");
                    int productID = Integer.parseInt(sc.nextLine());
                    if (!products.containsKey(productID)) {
                        System.out.println("нет товара с таким id " + productID);
                        continue;
                    }
                    System.out.print("Введите количество добавляемого товара: ");
                    int productAmount = Integer.parseInt(sc.nextLine());
                    clients.get(clientID).addProductToBasket(products.get(productID), productAmount);
                } else if (command.startsWith("вывод информации о клиенте")) {
                    System.out.print("Введите id клиента: ");
                    int clientID = Integer.parseInt(sc.nextLine());
                    if (!clients.containsKey(clientID)) {
                        System.out.println("нет клиента с таким id " + clientID);
                        continue;
                    }
                    System.out.print(clients.get(clientID).display());
                } else if (command.startsWith("вывод информации о товаре")) {
                    System.out.print("Введите id товара: ");
                    int productID = Integer.parseInt(sc.nextLine());
                    if (!products.containsKey(productID)) {
                        System.out.println("нет товара с таким id " + productID);
                        continue;
                    }
                    System.out.print(products.get(productID).display());
                } else if (command.startsWith("вывод информации о заказе")) {
                    System.out.print("Введите id заказа: ");
                    int orderID = Integer.parseInt(sc.nextLine());
                    if (!orders.containsKey(orderID)) {
                        System.out.println("нет заказа с таким id " + orderID);
                        continue;
                    }
                    System.out.print(orders.get(orderID).display());
                } else if (command.startsWith("сохранить")) {
                    saveDBToFiles();
                } else if (command.startsWith("выход")) {
                    break;
                } else {
                    System.out.println("""
                            неизвестная команда
                            список возможных команд:
                            добавить клиента
                            добавить товар
                            создать заказ
                            изменить клиента
                            изменить товар
                            изменить заказ
                            изменить корзину
                            вывод информации о клиенте
                            вывод информации о товаре
                            вывод информации о заказе
                            сохранить
                            выход
                            """);
                }
            }
            saveDBToFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void getDBFromFiles() throws Exception {
        products = new HashMap<>();
        JSONArray productsJSON = getJSONFromFile("/db/products.json");
        for (int i = 0; i < productsJSON.length(); ++i) {
            JSONObject productJSON = (JSONObject) productsJSON.get(i);
            Product product = new Product(productJSON);
            products.put(product.getId(), product);
        }
        clients = new HashMap<>();
        JSONArray clientsJSON = getJSONFromFile("/db/clients.json");
        for (int i = 0; i < clientsJSON.length(); ++i) {
            JSONObject clientJSON = (JSONObject) clientsJSON.get(i);
            Client client = new Client(clientJSON, products);
            clients.put(client.getId(), client);
        }
        orders = new HashMap<>();
        JSONArray ordersJSON = getJSONFromFile("/db/orders.json");
        for (int i = 0; i < ordersJSON.length(); ++i) {
            JSONObject orderJSON = (JSONObject) ordersJSON.get(i);
            Order order = new Order(orderJSON, clients, products);
            orders.put(order.getId(), order);
        }
    }

    public static JSONArray getJSONFromFile(String filename) throws IOException {
        InputStream is = Main.class.getResourceAsStream(filename);
        if (is == null) {
            return new JSONArray();
        }
        JSONTokener tokener = new JSONTokener(is);
        return new JSONArray(tokener);
    }

    public static void saveDBToFiles() {
        JSONArray productsJSON = new JSONArray();
        for (int i : products.keySet()) {
            productsJSON.put(products.get(i).toJSON());
        }
        writeJSONToFile("src/db/products.json", productsJSON);

        JSONArray clientsJSON = new JSONArray();
        for (int i : clients.keySet()) {
            clientsJSON.put(clients.get(i).toJSON());
        }
        writeJSONToFile("src/db/clients.json", clientsJSON);

        JSONArray ordersJSON = new JSONArray();
        for (int i : orders.keySet()) {
            ordersJSON.put(orders.get(i).toJSON());
        }
        writeJSONToFile("src/db/orders.json", ordersJSON);
    }

    public static void writeJSONToFile(String filename, JSONArray array) {
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            String resultJSON = array.toString();
            resultJSON = resultJSON.replace("[", "[\n");
            resultJSON = resultJSON.replace("]", "\n]");
            resultJSON = resultJSON.replace("{", "{\n");
            resultJSON = resultJSON.replace("}", "\n}");
            resultJSON = resultJSON.replace(":", ": ");
            writer.write(resultJSON);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}