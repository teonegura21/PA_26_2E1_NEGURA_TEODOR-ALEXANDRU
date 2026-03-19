
import java.util.Random;

public class Matrix2D {
    private int n;
    private int[][] matrix;
    private Random rand;

    public Matrix2D(int n) {
        this.n = n;
        this.matrix = new int[n][n];
        this.rand = new Random();
    }

    public void generate() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = rand.nextInt(10);
            }
        }
    }

    public void display() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int n = 5;
        Matrix2D m = new Matrix2D(n);
        m.generate();
        m.display();
    }
}
