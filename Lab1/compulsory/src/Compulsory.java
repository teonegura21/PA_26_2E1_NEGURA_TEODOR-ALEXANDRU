
public class Compulsory {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "Java", "Swift", "Ruby"};
        
        int n = (int) (Math.random() * 1_000_000);
        
        n *= 3;
        n += 0b10101;
        n += 0xFF;
        n *= 6;
        
        int result = n;
        while (result >= 10) {
            int sum = 0;
            while (result > 0) {
                sum += result % 10;
                result /= 10;
            }
            result = sum;
        }
        
        System.out.println("Willy-nilly, this semester I will learn " + languages[result]);
    }
}
