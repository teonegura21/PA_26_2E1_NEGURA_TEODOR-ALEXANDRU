package lab5;

import lab5.commands.*;
import lab5.exceptions.InvalidResourceException;
import lab5.exceptions.ResourceException;
import lab5.model.Article;
import lab5.model.Book;
import lab5.model.WebResource;
import lab5.repository.ResourceRepository;

public class Homework {
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

            Article article = new Article("ai24", "AI Advances", "https://example.com/ai.pdf");
            article.setYear("2024");
            repo.addResource(article);

        } catch (InvalidResourceException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("=== Testing Commands ===");

        Command listCmd = new ListCommand(repo);
        try {
            listCmd.execute();
        } catch (ResourceException e) {
            System.err.println("List error: " + e.getMessage());
        }

        Command saveCmd = new SaveCommand(repo, "catalog.dat");
        try {
            saveCmd.execute();
        } catch (ResourceException e) {
            System.err.println("Save error: " + e.getMessage());
        }

        Command reportCmd = new ReportCommand(repo, "report.html");
        try {
            reportCmd.execute();
        } catch (ResourceException e) {
            System.err.println("Report error: " + e.getMessage());
        }

        repo.clear();
        System.out.println("\nAfter clear:");
        try {
            listCmd.execute();
        } catch (ResourceException e) {
            System.err.println("List error: " + e.getMessage());
        }

        Command loadCmd = new LoadCommand(repo, "catalog.dat");
        try {
            loadCmd.execute();
        } catch (ResourceException e) {
            System.err.println("Load error: " + e.getMessage());
        }

        System.out.println("\nAfter load:");
        try {
            listCmd.execute();
        } catch (ResourceException e) {
            System.err.println("List error: " + e.getMessage());
        }

        System.out.println("\nDone.");
    }
}
