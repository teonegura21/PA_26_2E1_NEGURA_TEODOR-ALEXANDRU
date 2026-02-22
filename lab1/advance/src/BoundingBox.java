/*
 * Autor: Negura Teodor-Alexandru
 * Grupa: 2E1
 */
public class BoundingBox {

    // Coordonatele dreptunghiului de delimitare
    private int minRow, maxRow;  // Randul minim si maxim
    private int minCol, maxCol;  // Coloana minima si maxima

    // Matricea de intrare
    private int[][] matrix;

    // Culoarea formei (ex: 255 pentru alb, 0 pentru negru)
    private int shapeColor;

    public BoundingBox(int[][] matrix, int shapeColor) {
        this.matrix = matrix;
        this.shapeColor = shapeColor;
        findBoundingBox();
    }

    private void findBoundingBox() {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Initializam cu valori extreme (opusul valorilor valide)
        // rows-1 este ultimul rand valid, cols-1 este ultima coloana valida
        minRow = rows;
        maxRow = -1;
        minCol = cols;
        maxCol = -1;

        // Parcurgem intreaga matrice
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Verificam daca pixelul apartine formei
                if (matrix[i][j] == shapeColor) {
                    // Actualizam coordonatele minime
                    if (i < minRow) minRow = i;
                    if (j < minCol) minCol = j;

                    // Actualizam coordonatele maxime
                    if (i > maxRow) maxRow = i;
                    if (j > maxCol) maxCol = j;
                }
            }
        }
    }

    // Metode getter pentru a accesa coordonatele
    public int getMinRow() { return minRow; }
    public int getMaxRow() { return maxRow; }
    public int getMinCol() { return minCol; }
    public int getMaxCol() { return maxCol; }

    public int getWidth() { return maxCol - minCol + 1; }

    public int getHeight() { return maxRow - minRow + 1; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Parcurgem fiecare pixel si il afisam corespunzator
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Verificam daca pixelul este pe granita formei
                // Granita = pe marginea dreptunghiului de delimitare
                boolean isBoundary = (i == minRow || i == maxRow || j == minCol || j == maxCol)
                                  && matrix[i][j] == shapeColor;

                if (isBoundary) {
                    sb.append('*');  // Granita
                } else if (matrix[i][j] == shapeColor) {
                    sb.append('█');  // Interior
                } else {
                    sb.append('░');  // Fundal
                }
            }
            sb.append("\n");
        }

        // Afisam informatiile despre dreptunghiul de delimitare
        sb.append("\nBounding Box (Dreptunghiul de delimitare):\n");
        sb.append("  Rand minim: ").append(minRow).append("\n");
        sb.append("  Rand maxim: ").append(maxRow).append("\n");
        sb.append("  Coloana minima: ").append(minCol).append("\n");
        sb.append("  Coloana maxima: ").append(maxCol).append("\n");
        sb.append("  Latime: ").append(getWidth()).append("\n");
        sb.append("  Inaltime: ").append(getHeight());

        return sb.toString();
    }

    public static void main(String[] args) {
        int n = 20;
        int[][] circle = new int[n][n];
        double center = n / 2.0;
        double radius = n / 4.0;

        // Generam cercul
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double dist = Math.sqrt(
                    (i - center) * (i - center) +
                    (j - center) * (j - center)
                );
                circle[i][j] = (dist <= radius) ? 255 : 0;
            }
        }

        System.out.println("=== Test 1: Cerc ===");
        BoundingBox bb = new BoundingBox(circle, 255);
        System.out.println(bb);

        System.out.println("\n=== Test 2: Dreptunghi ===");
        int[][] rect = new int[n][n];
        int rectSize = n / 3;
        int start = (n - rectSize) / 2;

        // Generam dreptunghiul
        for (int i = start; i < start + rectSize; i++) {
            for (int j = start; j < start + rectSize; j++) {
                rect[i][j] = 255;
            }
        }

        BoundingBox bb2 = new BoundingBox(rect, 255);
        System.out.println(bb2);
    }
}
