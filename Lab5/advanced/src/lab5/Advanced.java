package lab5;

import lab5.exceptions.InvalidResourceException;
import lab5.model.Article;
import lab5.model.Book;
import lab5.model.Resource;
import lab5.model.WebResource;
import lab5.repository.ResourceRepository;
import java.util.*;

public class Advanced {

    static final String GRAPH_THEORY = "Graph theory";
    static final String NEURAL_NETWORKS = "Neural Networks";
    static final String ALGORITHM_DESIGN = "Algorithm design techniques";
    static final String OOP = "Object-oriented programming";

    public static void main(String[] args) {
        ResourceRepository repo = new ResourceRepository();

        try {
            Book book1 = new Book("gt-book", "Introduction to Graph Theory", "/books/gt.pdf");
            book1.addKeyword(GRAPH_THEORY);
            book1.addKeyword(ALGORITHM_DESIGN);
            repo.addResource(book1);

            Book book2 = new Book("oop-book", "OOP Principles", "/books/oop.pdf");
            book2.addKeyword(OOP);
            book2.addKeyword(ALGORITHM_DESIGN);
            repo.addResource(book2);

            Article article1 = new Article("nn-article", "Deep Learning Advances", "/articles/nn.pdf");
            article1.addKeyword(NEURAL_NETWORKS);
            article1.addKeyword(ALGORITHM_DESIGN);
            repo.addResource(article1);

            WebResource web1 = new WebResource("gt-web", "Graph Algorithms", "https://example.com/gt");
            web1.addKeyword(GRAPH_THEORY);
            web1.addKeyword(OOP);
            repo.addResource(web1);

            Article article2 = new Article("oop-nn", "Neural OOP", "/articles/oopnn.pdf");
            article2.addKeyword(OOP);
            article2.addKeyword(NEURAL_NETWORKS);
            repo.addResource(article2);

            Book book3 = new Book("all-book", "Computer Science Overview", "/books/cs.pdf");
            book3.addKeyword(GRAPH_THEORY);
            book3.addKeyword(NEURAL_NETWORKS);
            book3.addKeyword(ALGORITHM_DESIGN);
            book3.addKeyword(OOP);
            repo.addResource(book3);

        } catch (InvalidResourceException e) {
            System.err.println("Error: " + e.getMessage());
        }

        Set<String> concepts = new HashSet<>(Arrays.asList(
            GRAPH_THEORY, NEURAL_NETWORKS, ALGORITHM_DESIGN, OOP
        ));

        System.out.println("All resources:");
        for (Resource r : repo.getAllResources()) {
            System.out.println(r);
        }

        System.out.println("\n=== Greedy Set Cover ===");
        long start1 = System.nanoTime();
        Set<Resource> greedyResult = KeywordSetCover.greedySetCover(concepts, repo.getAllResources());
        long end1 = System.nanoTime();
        System.out.println("Greedy solution size: " + greedyResult.size());
        for (Resource r : greedyResult) {
            System.out.println("  - " + r.getId() + ": " + r.getKeywords());
        }
        System.out.println("Time: " + (end1 - start1) / 1000 + " microseconds");

        System.out.println("\n=== Brute Force Minimum Set Cover ===");
        long start2 = System.nanoTime();
        Set<Resource> optimalResult = KeywordSetCover.bruteForceMinimumSetCover(concepts, repo.getAllResources());
        long end2 = System.nanoTime();
        System.out.println("Optimal solution size: " + optimalResult.size());
        for (Resource r : optimalResult) {
            System.out.println("  - " + r.getId() + ": " + r.getKeywords());
        }
        System.out.println("Time: " + (end2 - start2) / 1000 + " microseconds");

        System.out.println("\n=== Performance Test with Random Instances ===");
        testPerformance();

        System.out.println("\nDone.");
    }

    static void testPerformance() {
        String[] keywordPool = {
            GRAPH_THEORY, NEURAL_NETWORKS, ALGORITHM_DESIGN, OOP,
            "Databases", "Operating Systems", "Networks", "Security"
        };
        Random rand = new Random(42);

        for (int size : new int[]{10, 15, 20}) {
            List<Resource> randomResources = new ArrayList<>();
            try {
                for (int i = 0; i < size; i++) {
                    Book b = new Book("rand" + i, "Random " + i, "/rand" + i + ".pdf");
                    int numKeywords = 1 + rand.nextInt(3);
                    for (int j = 0; j < numKeywords; j++) {
                        b.addKeyword(keywordPool[rand.nextInt(keywordPool.length)]);
                    }
                    randomResources.add(b);
                }
            } catch (InvalidResourceException e) {
                e.printStackTrace();
            }

            Set<String> targetConcepts = new HashSet<>(Arrays.asList(
                GRAPH_THEORY, NEURAL_NETWORKS, ALGORITHM_DESIGN
            ));

            long t1 = System.nanoTime();
            Set<Resource> greedy = KeywordSetCover.greedySetCover(targetConcepts, randomResources);
            long t2 = System.nanoTime();

            System.out.println("Size " + size + ": Greedy=" + greedy.size() +
                " in " + (t2 - t1) / 1000 + " us");
        }
    }
}
