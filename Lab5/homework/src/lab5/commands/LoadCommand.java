package lab5.commands;

import lab5.Catalog;
import lab5.exceptions.ResourceException;
import lab5.model.Resource;
import lab5.repository.ResourceRepository;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoadCommand implements Command {
    private ResourceRepository repository;
    private String filename;

    public LoadCommand(ResourceRepository repository, String filename) {
        this.repository = repository;
        this.filename = filename;
    }

    @Override
    public void execute() throws ResourceException {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Catalog catalog = (Catalog) ois.readObject();
            ois.close();
            repository.clear();
            for (Resource r : catalog.getResources()) {
                repository.addResource(r);
            }
            System.out.println("Catalog loaded from " + filename);
        } catch (Exception e) {
            throw new ResourceException("Load failed: " + e.getMessage());
        }
    }
}
