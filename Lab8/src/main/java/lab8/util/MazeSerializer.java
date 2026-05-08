package lab8.util;

import lab8.model.Maze;

import java.io.*;

/* HOMEWORK - Object serialization for save/load */
public class MazeSerializer {
    
    public static void save(Maze maze, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(maze);
        }
    }
    
    public static Maze load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Maze) ois.readObject();
        }
    }
}
