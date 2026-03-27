package lab5.commands;

import lab5.Catalog;
import lab5.exceptions.ResourceException;
import lab5.model.Resource;
import lab5.repository.ResourceRepository;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SaveCommand implements Command {
    private ResourceRepository repository;
    private String filename;

    public SaveCommand(ResourceRepository repository, String filename) {
        this.repository = repository;
        this.filename = filename;
    }

    @Override
    public void execute() throws ResourceException {
        try {
            Catalog catalog = new Catalog("MyCatalog");
            for (Resource r : repository.getAllResources()) {
                catalog.addResource(r);
            }
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(catalog);
            oos.close();
            System.out.println("Catalog saved to " + filename);
        } catch (Exception e) {
            throw new ResourceException("Save failed: " + e.getMessage());
        }
    }
}
