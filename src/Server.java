import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;
import java.lang.Math;


public class Server implements Runnable {
    private static String userDatabase = "databaseInformation.txt";
    private static Database database = new Database();
    private static final String FILENAME = "database.txt";
    private static final String INFORMATION_FILE = "databaseInformation.txt";


    Socket socket;

    public Server(Socket socket) throws FileNotFoundException {
        this.socket = socket;
    }


    private static ArrayList<String> searchDatabase(String search) {
        ArrayList<String> results = new ArrayList<>();
        System.out.println(search);
        for (User user : database.users) {
            if (user.getUsername().toLowerCase().contains(search.toLowerCase())) {
                System.out.println(user.getUsername());
                results.add(user.getUsername());
            }
        }
        return results;
    }

    // Yatharth - adding protected login
    private static User login(String userName, String password) {
        for (User user : database.users) {
            if (user.getUsername().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            // receive input from client
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
                            new ArrayList<User>(), new ArrayList<User>(), new ArrayList<Message>());
                    boolean successful = database.addUser(createUser);
                    if (successful) {
                        writer.write(createUser.getUsername());
                        writer.println();
                        writer.flush();
                        System.out.println("Created new account" + database.users);
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
                    System.out.println(userAdding.getFriendsString());
                    System.out.println(userAdding);
                    database.saveDatabase(FILENAME);
                    database.loadDatabase(FILENAME);
                    database.saveInformation("databaseInformation.txt");
                    System.out.println(database.users);

                } else if (option.equals("Block user")) {

                    User user = database.findUser(reader.readLine());
                    User blocked = database.findUser(reader.readLine());

                    user.blockUser(blocked);
                    database.saveDatabase(FILENAME);
                    database.loadDatabase(FILENAME);
                    database.saveInformation("databaseInformation.txt");
                    System.out.println(database.users);

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

        database.loadDatabase(FILENAME);


        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }


    }


}
