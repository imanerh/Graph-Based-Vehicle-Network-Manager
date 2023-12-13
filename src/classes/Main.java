package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        try {
            // Get the txt file
            File mainFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String parentPath = mainFile.getParent();
            File f = new File(parentPath + File.separator + "data.txt");
            
            Scanner reader = new Scanner(f);

            int number_vertices = Integer.parseInt(reader.nextLine());
            double transmission_range = Double.parseDouble(reader.nextLine());
            ArrayList<Vertex> vertices = new ArrayList<>();

            int id = 0; // Auto-incremented ID
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                List<Double> l = Arrays.stream(data.split(" "))
                        .map(Double::parseDouble)
                        .collect(Collectors.toList());
                Vertex v = new Vertex(id, l.get(0), l.get(1));
                vertices.add(v);
                id++;
            }
            reader.close();

            // Create the graph corresponding to the given file
            Graph graph = new Graph(number_vertices, transmission_range, vertices);

            // Display the menu
            while (true) {

                displayMenu();
                int operation = CheckUserInputInt(1, 8, false);

                if (operation == 8) {
                    // Quit
                    break;
                } else if (operation == 1) {
                    // Display All Edges
                    graph.displayEdges();
                } else if (operation == 2) {
                    // Display Adjacent Vehicles for a given vehicle
                    System.out.println("    Vehicle Id [There are " + number_vertices
                            + " vehicles where vehicle 1 has an ID of 0 and so on]:");
                    int inputId = CheckUserInputInt(0, number_vertices - 1, true);
                    graph.displayAdjacentVehicles(inputId);
                } else if (operation == 3) {
                    // Move a vehicle
                    System.out.println("    Vehicle Id [There are " + number_vertices
                            + " vehicles where vehicle 1 has an ID of 0 and so on]:");
                    int vehicle_id = CheckUserInputInt(0, number_vertices - 1, true);
                    System.out.println("    > Coordinates:");
                    System.out.print("        > x: ");
                    double x = CheckUserInputDouble();
                    System.out.print("        > y: ");
                    double y = CheckUserInputDouble();
                    graph.moveVehicle(vehicle_id, x, y);
                } else if (operation == 4) {
                    // DFS
                    System.out.println("    Which vehicle to start from?");
                    int start_id = CheckUserInputInt(0, number_vertices - 1, true);
                    graph.DFS(start_id);
                } else if (operation == 5) {
                    // BFS
                    System.out.println("    Which vehicle to start from?");
                    int start_id = CheckUserInputInt(0, number_vertices - 1, true);
                    graph.BFS(start_id);
                } else if (operation == 6) {
                    // MST
                    graph.MST();
                } else if (operation == 7) {
                    // Shortest Path
                    System.out.println("      Start: ");
                    int start_id = CheckUserInputInt(0, number_vertices - 1, true);
                    System.out.println("      Destination: ");
                    int dest_id = CheckUserInputInt(0, number_vertices - 1, true);
                    graph.shortest_path(start_id, dest_id);
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void displayMenu() {
        System.out.println("");
        System.out.println("=================Menu=================");
        System.out.println("1. Display All Edges");
        System.out.println("2. Display Adjacent Vehicles for a given vehicle");
        System.out.println("3. Move a vehicle");
        System.out.println("4. DFS (Depth-First Search)");
        System.out.println("5. BFS (Breadth-First Search)");
        System.out.println("6. MST");
        System.out.println("7. Shortest Path");
        System.out.println("8. Quit");
        System.out.println("======================================");
        System.out.println("");
    }

    public static int CheckUserInputInt(int lowerBound, int UpperBound, boolean indentation) {
        // Takes the user input until it is in the range [lowerBound, UpperBound]

        Scanner scanner = new Scanner(System.in);
        int userInput = -1;
        boolean isValid = false;

        do {
            if (indentation) {
                System.out.print("    ");
            }
            System.out.print("> Enter a number from " + lowerBound + " to " + UpperBound + ": ");

            if (scanner.hasNextInt()) { // If the user enters a int value
                userInput = scanner.nextInt();
                if (userInput >= lowerBound && userInput <= UpperBound) {
                    isValid = true;
                } else {
                    System.out.println("\tInvalid input.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        } while (!isValid);

        return userInput;
    }

    public static double CheckUserInputDouble() {
        // Takes the user input until it is in the range [lowerBound, UpperBound]

        Scanner scanner = new Scanner(System.in);
        double userInput = -1;
        boolean isValid = false;

        do {
            if (scanner.hasNextDouble()) { // If the user enters a int value
                userInput = scanner.nextDouble();
                isValid = true;
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        } while (!isValid);

        return userInput;
    }
}
