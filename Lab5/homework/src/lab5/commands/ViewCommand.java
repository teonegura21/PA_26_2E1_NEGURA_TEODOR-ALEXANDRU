package lab5.commands;

import lab5.exceptions.ResourceException;
import lab5.repository.ResourceRepository;

public class ViewCommand implements Command {
    private ResourceRepository repository;
    private String resourceId;

    public ViewCommand(ResourceRepository repository, String resourceId) {
        this.repository = repository;
        this.resourceId = resourceId;
    }

    @Override
    public void execute() throws ResourceException {
        repository.openResource(resourceId);
    }
}
