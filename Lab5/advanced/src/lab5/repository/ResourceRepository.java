package lab5.repository;

import lab5.exceptions.ResourceNotFoundException;
import lab5.exceptions.ResourceOpenException;
import lab5.model.Resource;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResourceRepository {
    private Map<String, Resource> resources = new HashMap<>();

    public void addResource(Resource resource) {
        if (resources.containsKey(resource.getId())) {
            throw new IllegalArgumentException("Duplicate ID: " + resource.getId());
        }
        resources.put(resource.getId(), resource);
    }

    public Resource getResource(String id) throws ResourceNotFoundException {
        Resource r = resources.get(id);
        if (r == null) throw new ResourceNotFoundException(id);
        return r;
    }

    public void openResource(String id) throws ResourceNotFoundException, ResourceOpenException {
        Resource r = getResource(id);
        if (!Desktop.isDesktopSupported()) {
            throw new ResourceOpenException("Desktop not supported");
        }
        try {
            Desktop desktop = Desktop.getDesktop();
            String loc = r.getLocation();
            if (loc.startsWith("http://") || loc.startsWith("https://")) {
                desktop.browse(new URI(loc));
            } else {
                desktop.open(new File(loc));
            }
        } catch (Exception e) {
            throw new ResourceOpenException(e.getMessage());
        }
    }

    public Collection<Resource> getAllResources() {
        return resources.values();
    }

    public void clear() {
        resources.clear();
    }
}
