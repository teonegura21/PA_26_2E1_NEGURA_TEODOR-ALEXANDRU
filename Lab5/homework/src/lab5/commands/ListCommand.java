package lab5.commands;

import lab5.repository.ResourceRepository;
import lab5.model.Resource;

public class ListCommand implements Command {
    private ResourceRepository repository;

    public ListCommand(ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        System.out.println("Catalog resources:");
        for (Resource r : repository.getAllResources()) {
            System.out.println(r);
        }
    }
}
