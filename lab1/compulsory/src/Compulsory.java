/*
 * Autor: Negura Teodor-Alexandru
 * Grupa: 2E1
 */

public class Compulsory {

    public static void main(String[] args) {

        System.out.println("Hello World");


        String[] languages = {
            "C", "C++", "C#", "Python", "Go",
            "Rust", "JavaScript", "PHP", "Swift", "Java"
        };

        int n = (int) (Math.random() * 1_000_000);

        n = n * 3 + 0b1001 + 0xFF;
        n = n * 6;

        int result = (n % 9 == 0) ? 9 : 0;

        System.out.println("Willy-nilly, this semester I will learn " + languages[result]);
    }
}
