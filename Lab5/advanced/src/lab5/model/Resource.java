package lab5.model;

import lab5.exceptions.InvalidResourceException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String location;
    private String year;
    private String author;
    private Set<String> keywords = new HashSet<>();

    public Resource(String id, String title, String location) throws InvalidResourceException {
        if (id == null || id.isEmpty()) throw new InvalidResourceException("ID cannot be empty");
        if (title == null || title.isEmpty()) throw new InvalidResourceException("Title cannot be empty");
        if (location == null || location.isEmpty()) throw new InvalidResourceException("Location cannot be empty");
        this.id = id;
        this.title = title;
        this.location = location;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Set<String> getKeywords() { return keywords; }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public abstract String getType();

    @Override
    public String toString() {
        return getType() + "{id='" + id + "', title='" + title + "', keywords=" + keywords + "}";
    }
}
