package lab5.model;

import lab5.exceptions.InvalidResourceException;

public class Article extends Resource {
    private String journal;

    public Article(String id, String title, String location) throws InvalidResourceException {
        super(id, title, location);
    }

    public String getJournal() { return journal; }
    public void setJournal(String journal) { this.journal = journal; }

    @Override
    public String getType() { return "Article"; }
}
