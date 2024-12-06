import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Team Project -- Database class
 *
 * Database class and corresponding constructors and methods.
 *
 * @author Bisti Potdar, lab sec 31
 *
 * @version November 3, 2024
 *
 */

public class Database extends ArrayList<String> implements DatabaseInterface, Serializable {

    public static ArrayList<User> users; // arraylist of all users created ever
    private static final long serialVersionUID = 1L; // for serializable
    private final String FILENAME = "database.txt";
    private final String INFORMATION_FILE = "databaseInformation.txt";

    public Database() {
        users = new ArrayList<>(); // initializes new database object
        // with an empty arraylist of users
    }

    // Yatharth - adding protected login
    public User login(String userName, String password) {
        for (User user : users) {
            if (user.getUsername().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        JOptionPane.showMessageDialog(null, "Incorrect username or password", "Error",
                JOptionPane.ERROR_MESSAGE);
        return null;
    }

    public User findUser(String username) {

        // iterates through users to find user with the given username
        // will be important for searching users
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    //added extra method - yatharth
    // finds user based on ID
    // this is helpful for locating a user for testcases and other methods.

    public void viewUsers() {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public boolean changeUsername(String username, String newUsername) {
        for (User user : getUsers()) {
            if (user.getUsername().equals(newUsername)) {
                return false;
            }
        }
        findUser(username).setUsername(newUsername);
        return true;
    }


    public boolean updatePassword(String username, String password) {
        User user = findUser(username);
        if (user != null) {
            user.setPassword(password);
            //user.setAttachment(updatedUser.getAttachment());
            return true;
        }
        return false;
    }


    // adds new user
    public synchronized boolean addUser(User user) {
        // check if user exists
        if (findUser(user.getUsername()) == null) {
            users.add(user);
            saveDatabase(FILENAME);
            return true;

            // if user already exists:
        } else {
            System.out.println("User " + user.getUsername() + " already exists");
            return false;
        }
    }

    // saves database object to a file
    public synchronized void saveDatabase(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(Database.users);
            System.out.println("Database saved");
            saveInformation();
        } catch (IOException e) {
            System.out.println("Error saving database");
            e.printStackTrace();
        }
    }

    public void loadDatabase(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            // loads a database object from a file
            // uses ObjectInputStream to load the serialized object from the file
            Database.users = (ArrayList<User>) ois.readObject();


        } catch (EOFException e) {
            System.out.println("Database has no users");
            Database.users = new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("Database file not found! Returning a new empty database.");
            Database.users = new ArrayList<>();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error loading database");
            e.printStackTrace();
        }
    }

    // saves information in a way that is a lot easier to read...
    private void saveInformation() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("databaseInformation.txt"))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
            System.out.println("Readable summary saved to databaseInformation.txt");
        } catch (IOException e) {
            System.out.println("Error saving readable summary");
            e.printStackTrace();
        }
    }




    public ArrayList<User> getUsers() {
        return users;
    }

    /*public void readUsers() {
        // delete all users and read everything back in to the arraylist again
        //users = new ArrayList<User>();
        try (BufferedReader br = new BufferedReader(new FileReader("databaseInformation.txt"))) {
            String line;
            String[] splicedLine;
            while ((line = br.readLine()) != null) {
                splicedLine = line.split(",");
                ArrayList<User> tempFriends = new ArrayList<>();
                ArrayList<User> blockedUsers = new ArrayList<>();
                User tempUser = new User(splicedLine[1], splicedLine[2],
                        splicedLine[3], tempFriends, blockedUsers, null);
                users.add(tempUser);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


    public static void main(String[] args) {

        Database database = new Database();

        database.loadDatabase("database.txt");

        database.saveDatabase("database.txt");

        database.saveInformation();
        database.loadDatabase("databaseInformation.txt");

        database.viewUsers();
    }

} // end of the class
