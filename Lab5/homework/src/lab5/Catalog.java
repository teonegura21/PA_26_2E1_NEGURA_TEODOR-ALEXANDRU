package lab5;

import lab5.model.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Catalog implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Resource> resources = new ArrayList<>();

    public Catalog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }
}
