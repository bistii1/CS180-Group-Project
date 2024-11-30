import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;


public class Server implements Runnable {
    private static Database database = new Database();
    private static final String DATABASE_OBJECT = "database.txt";


    Socket socket;

    public Server(Socket socket) throws FileNotFoundException {
        this.socket = socket;
    }


    private static ArrayList<String> searchDatabase(String search) {
        ArrayList<String> results = new ArrayList<>();
        System.out.println(search);
        for (User user : Database.users) {
            if (user.getUsername().toLowerCase().contains(search.toLowerCase())) {
                System.out.println(user.getUsername());
                results.add(user.getUsername());
            }
        }
        return results;
    }

    // Yatharth - adding protected login
    private static User login(String userName, String password) {
        for (User user : Database.users) {
            if (user.getUsername().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            // read users from database.txt
            database.loadDatabase(DATABASE_OBJECT);
            System.out.println("Current users " + Database.users);
            //System.out.println(Database.users);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());


            while (socket.isConnected()) {
                String option = reader.readLine();
                System.out.println(option);
                if (option.equals("Login")) {
                    boolean loggedIn = false;
                    while (!loggedIn) {
                        String username = reader.readLine();
                        String password = reader.readLine();
                        User thisUser = login(username, password);
                        if (thisUser != null) {
                            writer.write(thisUser.getUsername());
                            writer.println();
                            writer.flush();
                            loggedIn = true;
                        } else {
                            writer.write("Login failed");
                            writer.println();
                            writer.flush();
                        }
                    }
                } else if (option.equals("Create account")) {
                    String username = reader.readLine();
                    String password = reader.readLine();
                    String profilePicture = reader.readLine();
                    User createUser = new User(username, password, profilePicture,
                            "NA", "NA", new ArrayList<>());
                    boolean successful = database.addUser(createUser);
                    if (successful) {
                        writer.write(createUser.getUsername());
                        writer.println();
                        writer.flush();
                        System.out.println("Created new account" + Database.users);
                    } else {
                        writer.write("User already exists");
                        writer.println();
                        writer.flush();
                    }
                } else if (option.equals("Search")) {
                    boolean searched = false;
                    while (!searched) {
                        String search = reader.readLine();
                        System.out.println("Received search for: " + search);

                        ArrayList<String> results = searchDatabase(search); // somehow have to get inputted parameters -> thru GUI ?
                        String response = String.join(";", results);
                        // this sends results to client
                        if (response.isEmpty()) {
                            System.out.println("Search failed");
                            writer.write("Search failed");
                            writer.println();
                            writer.flush();
                        } else {
                            System.out.println("Successfully sent to client: " + response);
                            writer.write(response);
                            writer.println();
                            writer.flush();
                        }

                        String searchAgain = reader.readLine();
                        if (searchAgain.equals(String.valueOf(JOptionPane.NO_OPTION))) {
                            searched = true;
                        }
                    }
                } else if (option.equals("Add friend")) {
                    String userAddingString = reader.readLine();
                    System.out.println("The string username is " + userAddingString);
                    // finds user object who is ADDING (sending request) using string search
                    User userAdding = database.findUser(userAddingString);

                    User friend = database.findUser(reader.readLine());
                    System.out.println("The string of the friend is " + friend.getUsername());

                    // actually adding friend
                    userAdding.addFriend(friend);
                    Database.users.set(Database.users.indexOf(database.findUser(userAddingString)), userAdding);
                    System.out.println(userAdding.getFriends());
                    System.out.println(userAdding);
                    database.saveDatabase(DATABASE_OBJECT);
                    //database.saveInformation(DATABASE_TEXT);
                    System.out.println(Database.users);

                } else if (option.equals("Block user")) {

                    User user = database.findUser(reader.readLine());
                    User blocked = database.findUser(reader.readLine());

                    user.blockUser(blocked);
                    database.saveDatabase(DATABASE_OBJECT);
                    //database.saveInformation(DATABASE_TEXT);
                    System.out.println(Database.users);

                }
                else if (option.equals("Message")) {
                    boolean allFriends = Boolean.parseBoolean(reader.readLine());
                    User user = database.findUser(reader.readLine());
                    if (allFriends) {
                        user.sendMessageAll(reader.readLine());
                    } else {
                        User receiver = database.findUser(reader.readLine());
                        if (receiver != null) {
                            user.sendMessage(receiver, reader.readLine());
                        }
                    }
                } else if (option.equals("Remove friend")) {
                    String userRemovingString = reader.readLine();
                    System.out.println("The string username is " + userRemovingString);
                    // finds user object who is ADDING (sending request) using string search
                    User userAdding = database.findUser(userRemovingString);

                    User friend = database.findUser(reader.readLine());
                    System.out.println("The string of the friend is " + friend.getUsername());

                    // actually adding friend
                    userAdding.removeFriend(friend);
                    Database.users.set(Database.users.indexOf(database.findUser(userRemovingString)), userAdding);
                    System.out.println(userAdding.getFriends());
                    System.out.println(userAdding);
                    database.saveDatabase(DATABASE_OBJECT);
                    //database.saveInformation(DATABASE_TEXT);
                    System.out.println(Database.users);
                }
            }
            writer.close();
            reader.close();
            System.out.println("Server closed.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // echo server that accepts multiple clients
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2424);

        //database.loadDatabase(FILENAME);


        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }


    }


}
