package lab5.model;

import lab5.exceptions.InvalidResourceException;

public class WebResource extends Resource {
    public WebResource(String id, String title, String location) throws InvalidResourceException {
        super(id, title, location);
    }

    @Override
    public String getType() { return "WebResource"; }
}
