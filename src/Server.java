import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;


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

    private ArrayList<String> loadMessages(User user, String sentOrIncoming) {
        System.out.println("User is " + user.getUsername());
        System.out.println(user.getMessageHistory());
        ArrayList<Message> messages = user.getMessageHistory();
        ArrayList<String> results = new ArrayList<>();
        if (sentOrIncoming.equals("incoming")) {
            for (Message message : messages) {
                if (!message.getSender().getUsername().equals(user.getUsername())) {
                    String newMessage = "From " + message.getSender().getUsername() + ": " + message.getContent();
                    results.add(newMessage);
                }
            }
            if (results.isEmpty()) {
                results.add(user.getUsername() + " has no incoming messages.");
            }
        } else if (sentOrIncoming.equals("friend incoming")) {
            for (Message message : messages) {
                if (!message.getSender().getUsername().equals(user.getUsername()) && message.getSender().friendsWith(message.getRecipient())) {
                    String newMessage = "From " + message.getSender().getUsername() + ": " + message.getContent();
                    results.add(newMessage);
                }
            }
            if (results.isEmpty()) {
                results.add(user.getUsername() + " has no incoming messages from friends.");
            }
        } else if (sentOrIncoming.equals("outgoing")) {
            for (Message message : messages) {
                if (message.getSender().getUsername().equals(user.getUsername())) {
                    String newMessage = "You sent " + message.getContent() + " to " + message.getRecipient().getUsername();
                    results.add(newMessage);
                }
            }
            if (results.isEmpty()) {
                results.add(user.getUsername() + " has no outgoing messages.");
            }
        } else if (sentOrIncoming.equals("delete")) {
            int i = 1;
            for (Message message : messages) {
                if (message.getSender().getUsername().equals(user.getUsername())) {
                    String newMessage = i + ". " + message.getContent() + " to " + message.getRecipient().getUsername();
                    results.add(newMessage);
                    i++;
                }
            }
            if (results.isEmpty()) {
                results.add(user.getUsername() + " has no outgoing messages to delete.");
            }
        }
        return results;
    }

    private Message findMessage(User user, int number) {
        ArrayList<Message> messages = user.getMessageHistory();
        int i = 1;
        for (Message message : messages) {
            if (message.getSender().getUsername().equals(user.getUsername())) {
                if (number == i) {
                    return message;
                }
                i++;
            }
        }
        return null;
    }

    private synchronized void deleteMessage(Message message) {
        for (User user : Database.users) {
            Iterator<Message> iterator = user.getMessageHistory().iterator();
            while (iterator.hasNext()) {
                Message eachMessage = iterator.next();
                if (eachMessage.equals(message)) {
                    iterator.remove();
                    user.deleteMessage(eachMessage);
                }
            }
        }
        database.saveDatabase(DATABASE_OBJECT);
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
                    database.loadDatabase(DATABASE_OBJECT);
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
                    boolean created = false;
                    while (!created) {
                        database.loadDatabase(DATABASE_OBJECT);
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
                            created = true;
                        } else {
                            writer.write("User already exists");
                            writer.println();
                            writer.flush();
                        }
                    }

                } else if (option.equals("Search")) {
                    boolean searched = false;
                    while (!searched) {
                        database.loadDatabase(DATABASE_OBJECT);
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
                    database.loadDatabase(DATABASE_OBJECT);
                    String userAddingString = reader.readLine();
                    System.out.println("The string username is " + userAddingString);
                    // finds user object who is ADDING (sending request) using string search
                    User userAdding = database.findUser(userAddingString);

                    User friend = database.findUser(reader.readLine());
                    if (friend != null) {
                        writer.write("");
                        writer.println();
                        writer.flush();
                        System.out.println("The string of the friend is " + friend.getUsername());
                        // actually adding friend
                        writer.write(userAdding.addFriend(friend));
                        writer.println();
                        writer.flush();
                        Database.users.set(Database.users.indexOf(database.findUser(userAddingString)), userAdding);
                        database.saveDatabase(DATABASE_OBJECT);
                    } else {
                        writer.write("null");
                        writer.println();
                        writer.flush();
                    }
                } else if (option.equals("Block user")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());

                    User blocked = database.findUser(reader.readLine());
                    if (blocked != null) {
                        writer.write("");
                        writer.println();
                        writer.flush();
                        writer.write(user.blockUser(blocked));
                        writer.println();
                        writer.flush();
                        database.saveDatabase(DATABASE_OBJECT);
                    } else {
                        writer.write("null");
                        writer.println();
                        writer.flush();
                    }
                }
                else if (option.equals("Message")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());
                    User receiver = database.findUser(reader.readLine());
                    if (receiver == null) {
                        writer.write("null");
                        writer.println();
                        writer.flush();
                    } else {
                        writer.write("");
                        writer.println();
                        writer.flush();
                        writer.write(user.sendMessage(receiver, reader.readLine()));
                        writer.println();
                        writer.flush();
                    }
                    database.saveDatabase(DATABASE_OBJECT);
                } else if (option.equals("Remove friend")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    String userRemovingString = reader.readLine();
                    System.out.println("The string username is " + userRemovingString);
                    // finds user object who is REMOVING using string search
                    User userRemoving = database.findUser(userRemovingString);
                    User friendToRemove = database.findUser(reader.readLine());
                    if (friendToRemove != null) {
                        writer.write("");
                        writer.println();
                        writer.flush();
                        System.out.println("The string of the friend to remove is " + friendToRemove.getUsername());
                        // actually adding friend
                        writer.write(userRemoving.removeFriend(friendToRemove));
                        writer.println();
                        writer.flush();
                        Database.users.set(Database.users.indexOf(database.findUser(userRemovingString)), userRemoving);
                        database.saveDatabase(DATABASE_OBJECT);
                        //database.saveInformation(DATABASE_TEXT);
                    } else {
                        writer.write("null");
                        writer.println();
                        writer.flush();
                    }

                    System.out.println(Database.users);
                } else if (option.equals("View Incoming Messages")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());
                    System.out.println(user);
                    ArrayList<String> messages = loadMessages(user, "incoming");
                    for (String message : messages) {
                        writer.write(message);
                        writer.println();
                        writer.flush();
                    }
                    writer.write("END");
                    writer.println();
                    writer.flush();
                } else if (option.equals("View Sent Messages")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());
                    System.out.println(user);
                    ArrayList<String> messages = loadMessages(user, "outgoing");
                    for (String message : messages) {
                        writer.write(message);
                        writer.println();
                        writer.flush();
                    }
                    writer.write("END");
                    writer.println();
                    writer.flush();
                } else if (option.equals("Delete Messages")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());
                    System.out.println(user);
                    ArrayList<String> messages = loadMessages(user, "delete");
                    if (messages.getFirst().equals(user.getUsername() + " has no outgoing messages to delete.")) {
                        writer.write("false");
                        writer.println();
                        writer.flush();
                        for (String message : messages) {
                            writer.write(message);
                            writer.println();
                            writer.flush();
                        }
                        writer.write("END");
                        writer.println();
                        writer.flush();
                    } else {
                        writer.write("true");
                        writer.println();
                        writer.flush();
                        for (String message : messages) {
                            writer.write(message);
                            writer.println();
                            writer.flush();
                        }
                        writer.write("END");
                        writer.println();
                        writer.flush();

                        try {
                            int number = Integer.parseInt(reader.readLine());
                            System.out.println(number);
                            deleteMessage(findMessage(user, number));
                            writer.write("true");
                            writer.println();
                            writer.flush();
                        } catch (NumberFormatException e) {
                            writer.write("false");
                            writer.println();
                            writer.flush();
                        }
                    }
                } else if (option.equals("Unblock user")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());

                    User unblocked = database.findUser(reader.readLine());
                    if (unblocked != null) {
                        writer.write("");
                        writer.println();
                        writer.flush();
                        writer.write(user.unblockUser(unblocked));
                        writer.println();
                        writer.flush();
                        database.saveDatabase(DATABASE_OBJECT);
                    } else {
                        writer.write("null");
                        writer.println();
                        writer.flush();
                    }
                } else if (option.equals("View Incoming Friend Messages")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User user = database.findUser(reader.readLine());
                    System.out.println(user);
                    ArrayList<String> messages = loadMessages(user, "friend incoming");
                    for (String message : messages) {
                        writer.write(message);
                        writer.println();
                        writer.flush();
                    }
                    writer.write("END");
                    writer.println();
                    writer.flush();
                } else if (option.equals("View Profile")) {
                    database.loadDatabase(DATABASE_OBJECT);
                    User searcher = database.findUser(reader.readLine());
                    User profile = database.findUser(reader.readLine());
                    if (profile == null) {
                        writer.write("null");
                        writer.println();
                        writer.flush();
                    } else {
                        writer.write("");
                        writer.println();
                        writer.flush();
                        if (searcher.friendsWith(profile)) {
                            writer.write(profile.getProfilePicture());
                            writer.println();
                            writer.flush();
                            writer.write(profile.getUsername() + ": You are friends with this user");
                            System.out.println(profile.getUsername() + ": You are friends with this user");
                            writer.println();
                            writer.flush();
                        } else if (searcher.blocked(profile)) {
                            writer.write(profile.getProfilePicture());
                            writer.println();
                            writer.flush();
                            writer.write(profile.getUsername() + ": You are blocked with this user");
                            System.out.println(profile.getUsername() + ": You are blocked with this user");
                            writer.println();
                            writer.flush();
                        } else {
                            writer.write(profile.getProfilePicture());
                            writer.println();
                            writer.flush();
                            writer.write(profile.getUsername() + ": You have no relation with this user");
                            System.out.println(profile.getUsername());
                            writer.println();
                            writer.flush();
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
        //database.loadDatabase(FILENAME);
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }
    }
}
