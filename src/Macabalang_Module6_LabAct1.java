import java.util.ArrayList;
import java.util.Scanner;

class Graph {
    private ArrayList<String> names = new ArrayList<>();
    private int[][] am = new int[0][0];

    public int getV() {
        return names.size();
    }

    public int getE() {
        int count = 0;
        for (int i = 0; i < am.length; i++) {
            for (int j = i + 1; j < am.length; j++) {
                if (am[i][j] != 0) count++;
            }
        }
        return count;
    }

    private int indexOf(String name) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    public boolean addVertex(String name) {
        if (name.isEmpty() || indexOf(name) != -1) return false;
        int n = names.size();
        int[][] bigger = new int[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bigger[i][j] = am[i][j];
            }
        }
        am = bigger;
        names.add(name);
        return true;
    }

    public boolean deleteVertex(String name) {
        int k = indexOf(name);
        if (k == -1) return false;
        int n = names.size();
        int[][] smaller = new int[n - 1][n - 1];
        for (int i = 0, r = 0; i < n; i++) {
            if (i == k) continue;
            for (int j = 0, c = 0; j < n; j++) {
                if (j == k) continue;
                smaller[r][c++] = am[i][j];
            }
            r++;
        }
        am = smaller;
        names.remove(k);
        return true;
    }

    public boolean renameVertex(String oldName, String newName) {
        int k = indexOf(oldName);
        if (k == -1 || newName.isEmpty() || indexOf(newName) != -1) return false;
        names.set(k, newName);
        return true;
    }

    public int addEdge(String a, String b, int w) {
        int i = indexOf(a), j = indexOf(b);
        if (i == -1 || j == -1) return 1;
        if (i == j) return 2;
        if (w <= 0) return 4;
        if (am[i][j] != 0) return 3;
        am[i][j] = w;
        am[j][i] = w;
        return 0;
    }

    public int editEdge(String a, String b, int w) {
        int i = indexOf(a), j = indexOf(b);
        if (i == -1 || j == -1) return 1;
        if (i == j || am[i][j] == 0) return 2;
        if (w <= 0) return 4;
        am[i][j] = w;
        am[j][i] = w;
        return 0;
    }

    public int deleteEdge(String a, String b) {
        int i = indexOf(a), j = indexOf(b);
        if (i == -1 || j == -1) return 1;
        if (i == j || am[i][j] == 0) return 2;
        am[i][j] = 0;
        am[j][i] = 0;
        return 0;
    }

    public void showAM() {
        int n = names.size();
        if (n == 0) {
            System.out.println("The graph is empty.");
            return;
        }

        int labelW = 4;
        for (String s : names) {
            labelW = Math.max(labelW, s.length() + 2);
        }
        int colW = 4;
        for (String s : names) {
            colW = Math.max(colW, s.length() + 2);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                colW = Math.max(colW, String.valueOf(am[i][j]).length() + 2);
            }
        }

        System.out.println();
        System.out.print(" ".repeat(labelW));
        for (String s : names) {
            System.out.print(pad(s, colW));
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.print(pad(names.get(i), labelW));
            for (int j = 0; j < n; j++) {
                System.out.print(pad(String.valueOf(am[i][j]), colW));
            }
            System.out.println();
        }
    }

    private String pad(String s, int width) {
        return s + " ".repeat(Math.max(0, width - s.length()));
    }
}

public class Macabalang_Module6_LabAct1 {
    private static Scanner sc = new Scanner(System.in);
    private static Graph g = new Graph();

    public static void main(String[] args) {
        String choice;
        do {
            System.out.println("\n===== GRAPH MENU =====");
            System.out.println("1. # of V (vertices)");
            System.out.println("2. # of E (edges)");
            System.out.println("3. Add");
            System.out.println("4. Delete");
            System.out.println("5. Edit");
            System.out.println("6. Show AM");
            System.out.println("7. Exit");
            choice = readText("Choose: ");

            switch (choice) {
                case "1":
                    System.out.println("Number of vertices: " + g.getV());
                    break;
                case "2":
                    System.out.println("Number of edges: " + g.getE());
                    break;
                case "3":
                    addMenu();
                    break;
                case "4":
                    deleteMenu();
                    break;
                case "5":
                    editMenu();
                    break;
                case "6":
                    g.showAM();
                    break;
                case "7":
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid choice. Pick 1 to 7.");
            }
        } while (!choice.equals("7"));
    }

    private static void addMenu() {
        String c = readText("Add (V)ertex or (E)dge? ").toLowerCase();
        if (c.equals("v")) {
            int count = readInt("How many vertices to add? ");
            if (count <= 0) {
                System.out.println("Nothing to add.");
                return;
            }
            System.out.print("Enter " + count + " vertex name(s), separated by spaces or new lines: ");
            int got = 0, added = 0;
            while (got < count) {
                String line = sc.nextLine().trim();
                for (String t : line.split("\\s+")) {
                    if (t.isEmpty() || got >= count) continue;
                    got++;
                    if (g.addVertex(t)) added++;
                    else System.out.println("Skipped " + t + " (duplicate).");
                }
            }
            System.out.println(added + " vertex(es) added.");
        } else if (c.equals("e")) {
            System.out.println("Enter all edges on ONE line as: from to, separated by commas");
            System.out.println("Example: A B, A C, B D   (add a number after the pair for a weight, like A B 3)");
            edgeBatch("add", "Edges: ", "added");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void deleteMenu() {
        String c = readText("Delete (V)ertex or (E)dge? ").toLowerCase();
        if (c.equals("v")) {
            String name = readText("Vertex name: ");
            System.out.println(g.deleteVertex(name) ? "Vertex deleted." : "Vertex not found.");
        } else if (c.equals("e")) {
            System.out.println("Enter the edges to delete on ONE line as: from to, separated by commas");
            edgeBatch("delete", "Edges: ", "deleted");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void editMenu() {
        String c = readText("Edit (E)dge weight or (V)ertex name? ").toLowerCase();
        if (c.equals("e")) {
            System.out.println("Enter edges as: from to newWeight, separated by commas");
            System.out.println("Example: F H 3, A B 2");
            edgeBatch("edit", "Edges: ", "updated");
        } else if (c.equals("v")) {
            String oldName = readText("Current name: ");
            String newName = readText("New name: ");
            System.out.println(g.renameVertex(oldName, newName)
                    ? "Vertex renamed." : "Vertex not found, or new name is invalid or taken.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void edgeBatch(String mode, String prompt, String doneWord) {
        String line = readText(prompt);
        int ok = 0;
        for (String part : line.split(",")) {
            String label = part.trim();
            if (label.isEmpty()) continue;
            String[] t = label.split("\\s+");
            int code;
            try {
                if (mode.equals("add")) {
                    if (t.length < 2 || t.length > 3) throw new IllegalArgumentException();
                    int w = t.length == 3 ? Integer.parseInt(t[2]) : 1;
                    code = g.addEdge(t[0], t[1], w);
                } else if (mode.equals("delete")) {
                    if (t.length != 2) throw new IllegalArgumentException();
                    code = g.deleteEdge(t[0], t[1]);
                } else {
                    if (t.length != 3) throw new IllegalArgumentException();
                    code = g.editEdge(t[0], t[1], Integer.parseInt(t[2]));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Skipped \"" + label + "\" (wrong format).");
                continue;
            }
            if (code == 0) ok++;
            else System.out.println("Skipped \"" + label + "\": " + errorText(code));
        }
        System.out.println(ok + " edge(s) " + doneWord + ".");
    }

    private static String errorText(int code) {
        switch (code) {
            case 1:
                return "vertex not found.";
            case 2:
                return "edge not found (or same vertex twice).";
            case 3:
                return "edge already exists.";
            case 4:
                return "weight must be greater than 0.";
            default:
                return "error.";
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a whole number.");
            }
        }
    }
}

