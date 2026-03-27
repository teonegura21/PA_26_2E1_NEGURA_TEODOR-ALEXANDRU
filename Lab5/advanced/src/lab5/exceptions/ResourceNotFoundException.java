package lab5.exceptions;

public class ResourceNotFoundException extends ResourceException {
    public ResourceNotFoundException(String id) {
        super("Resource not found: " + id);
    }
}
