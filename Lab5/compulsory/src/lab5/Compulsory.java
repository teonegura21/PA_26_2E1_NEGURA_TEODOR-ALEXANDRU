package lab5;

import lab5.exceptions.InvalidResourceException;
import lab5.exceptions.ResourceNotFoundException;
import lab5.model.Book;
import lab5.model.Article;
import lab5.model.WebResource;
import lab5.repository.ResourceRepository;

public class Compulsory {
    public static void main(String[] args) {
        ResourceRepository repo = new ResourceRepository();

        try {
            Book book = new Book("knuth67", "The Art of Computer Programming", "d:/books/tacp.ps");
            book.setYear("1967");
            book.setAuthor("Donald E. Knuth");
            repo.addResource(book);

            WebResource web1 = new WebResource("jvm25", "The Java Virtual Machine Specification", 
                "https://docs.oracle.com/javase/specs/jvms/se25/html/index.html");
            web1.setYear("2025");
            web1.setAuthor("Tim Lindholm");
            repo.addResource(web1);

            WebResource web2 = new WebResource("java25", "The Java Language Specification", 
                "https://docs.oracle.com/javase/specs/jls/se25/jls25.pdf");
            web2.setYear("2025");
            web2.setAuthor("James Gosling");
            repo.addResource(web2);

            Article article = new Article("ai24", "AI Advances", "https://example.com/ai.pdf");
            article.setYear("2024");
            repo.addResource(article);

        } catch (InvalidResourceException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("All resources:");
        repo.printAll();

        System.out.println("\nTesting exceptions:");
        
        try {
            repo.getResource("nonexistent");
        } catch (ResourceNotFoundException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            new Book("", "Title", "loc");
        } catch (InvalidResourceException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\nDone.");
    }
}
