import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static List<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFilename = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("A - Add an item");
            System.out.println("D - Delete an item");
            System.out.println("I - Insert an item");
            System.out.println("M - Move an item");
            System.out.println("V - View the list");
            System.out.println("C - Clear the list");
            System.out.println("O - Open a list file");
            System.out.println("S - Save the list file");
            System.out.println("Q - Quit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            try {
                switch (choice) {
                    case "A" -> addItem();
                    case "D" -> deleteItem();
                    case "I" -> insertItem();
                    case "M" -> moveItem();
                    case "V" -> viewList();
                    case "C" -> clearList();
                    case "O" -> openFile();
                    case "S" -> saveFile();
                    case "Q" -> quit();
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void addItem() {
        System.out.print("Enter the item to add: ");
        String item = scanner.nextLine();
        itemList.add(item);
        needsToBeSaved = true;
        System.out.println("Item added.");
    }

    private static void deleteItem() {
        viewList();
        System.out.print("Enter the index of the item to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < itemList.size()) {
            itemList.remove(index);
            needsToBeSaved = true;
            System.out.println("Item deleted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem() {
        System.out.print("Enter the item to insert: ");
        String item = scanner.nextLine();
        System.out.print("Enter the position to insert at (0 to " + itemList.size() + "): ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index <= itemList.size()) {
            itemList.add(index, item);
            needsToBeSaved = true;
            System.out.println("Item inserted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem() {
        viewList();
        System.out.print("Enter the index of the item to move: ");
        int fromIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the new position: ");
        int toIndex = Integer.parseInt(scanner.nextLine());
        if (fromIndex >= 0 && fromIndex < itemList.size() && toIndex >= 0 && toIndex <= itemList.size()) {
            String item = itemList.remove(fromIndex);
            itemList.add(toIndex, item);
            needsToBeSaved = true;
            System.out.println("Item moved.");
        } else {
            System.out.println("Invalid indices.");
        }
    }

    private static void viewList() {
        if (itemList.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            System.out.println("Current List:");
            for (int i = 0; i < itemList.size(); i++) {
                System.out.println(i + ": " + itemList.get(i));
            }
        }
    }

    private static void clearList() {
        System.out.print("Are you sure you want to clear the list? (Y/N): ");
        String confirmation = scanner.nextLine().toUpperCase();
        if (confirmation.equals("Y")) {
            itemList.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        } else {
            System.out.println("Clear operation canceled.");
        }
    }

    private static void openFile() throws IOException {
        if (needsToBeSaved) {
            promptToSave();
        }

        System.out.print("Enter the filename to open: ");
        String filename = scanner.nextLine();
        Path path = Paths.get(filename);
        if (Files.exists(path)) {
            itemList = Files.readAllLines(path);
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("File loaded.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void saveFile() throws IOException {
        if (currentFilename == null) {
            System.out.print("Enter the filename to save as: ");
            currentFilename = scanner.nextLine();
        }
        Files.write(Paths.get(currentFilename), itemList);
        needsToBeSaved = false;
        System.out.println("File saved.");
    }

    private static void quit() throws IOException {
        if (needsToBeSaved) {
            promptToSave();
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static void promptToSave() throws IOException {
        System.out.print("You have unsaved changes. Save now? (Y/N): ");
        String response = scanner.nextLine().toUpperCase();
        if (response.equals("Y")) {
            saveFile();
        }
    }
}
