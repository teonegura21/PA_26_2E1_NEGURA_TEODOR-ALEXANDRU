package lab5.commands;

import lab5.exceptions.ResourceException;

public interface Command {
    void execute() throws ResourceException;
}
