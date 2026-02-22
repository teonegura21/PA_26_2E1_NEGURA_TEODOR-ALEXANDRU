/*
* Clasa Matrix2D - genereaza o matrice 2D de pixeli (0-255) care reprezinta
 * Utilizare: java Matrix2D <n> <rectangle|circle>
 * Autor: Negura Teodor-Alexandru
 * Grupa: E1
 */
public class Matrix2D {

    // Matricea care stocheaza valorile pixelilor (0-255)
    private int[][] matrix;

    // Dimensiunea matricii (n x n)
    private int n;

    // Tipul imaginii: "rectangle" sau "circle"
    private String imageType;

    public Matrix2D(int n, String imageType) {
        this.n = n;
        this.imageType = imageType.toLowerCase();
        // Alocam spatiu pentru matricea n x n -> int (4 bytes)
        this.matrix = new int[n][n];

        // Cream forma geometrica in functie de tipul solicitat
        if (imageType.equals("rectangle")) {
            createDarkRectangle();
        } else if (imageType.equals("circle")) {
            createWhiteCircle();
        }
    }

    private void createDarkRectangle() {
        // Pasul 1: Umplem fundalul cu alb (255)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 255;
            }
        }

        // Pasul 2: Calculam dimensiunea dreptunghiului (1/3 din dimensiunea imaginii)
        int rectSize = n / 3;

        // Pasul 3: Calculam pozitia de start pentru a pozitiona dreptunghiul in centru
        int start = (n - rectSize) / 2;

        // Pasul 4: Setam pixelii dreptunghiului la negru (0)
        for (int i = start; i < start + rectSize; i++) {
            for (int j = start; j < start + rectSize; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    private void createWhiteCircle() {
        // Pasul 1: Umplem fundalul cu negru (0)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = 0;
            }
        }

        // Pasul 2: Calculam centrul si raza cercului
        double center = n / 2.0;    // Centrul cercului
        double radius = n / 4.0;    // Raza = 1/4 din dimensiune

        // Pasul 3 & 4: Parcurgem fiecare pixel si verificam daca este in interiorul cercului
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Calculam distanta euclidiana de la pixel la centru
                double distance = Math.sqrt(
                    (i - center) * (i - center) +
                    (j - center) * (j - center)
                );

                // Daca distanta este mai mica sau egala cu raza, pixelul apartine cercului
                if (distance <= radius) {
                    matrix[i][j] = 255;  // Alb
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Caracterele Unicode pentru diferite niveluri de gri
        String[] chars = {"░", "▒", "▓", "█"};

        // Parcurgem fiecare rand si coloana
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = matrix[i][j];

                // Selectam caracterul corespunzator valorii pixelului
                if (value < 64) {
                    sb.append(chars[0]);  // ░
                } else if (value < 128) {
                    sb.append(chars[1]);  // ▒
                } else if (value < 192) {
                    sb.append(chars[2]);  // ▓
                } else {
                    sb.append(chars[3]);  // █
                }
            }
            sb.append("\n");  // Trecem la randul urmator
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Validam argumentele din linia de comanda
        if (args.length < 2) {
            System.out.println("Utilizare: java Matrix2D <n> <rectangle|circle>");
            System.out.println("Exemplu: java Matrix2D 100 circle");
            System.out.println("");
            System.out.println("Pentru valori mari ale lui n (ex: 50000), programul va afisa");
            System.out.println("timpul de executie in loc de matricea completa.");
            return;
        }

        // Parsam argumentele
        int n = Integer.parseInt(args[0]);
        String type = args[1];

        // Masuram timpul de executie in nanosecunde
        long start = System.nanoTime();

        // Cream matricea cu forma geometrica solicitata
        Matrix2D m = new Matrix2D(n, type);

        long duration = System.nanoTime() - start;

        // Pentru dimensiuni mici (n <= 50), afisam matricea
        // Pentru dimensiuni mari, afisam doar timpul de executie
        if (n <= 50) {
            System.out.println(m.toString());
        } else {
            System.out.println("Timp de executie: " + duration + " ns (" +
                             (duration / 1_000_000.0) + " ms)");
            System.out.println("Dimensiune matrice: " + n + " x " + n);
            System.out.println("Tip imagine: " + type);
        }
    }
}
