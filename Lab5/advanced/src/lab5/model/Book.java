package lab5.model;

import lab5.exceptions.InvalidResourceException;

public class Book extends Resource {
    private String publisher;

    public Book(String id, String title, String location) throws InvalidResourceException {
        super(id, title, location);
    }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    @Override
    public String getType() { return "Book"; }
}
