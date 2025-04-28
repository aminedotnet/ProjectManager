package CLass;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFileEmpty() {
        File file = new File(fileName);
        return !file.exists() || file.length() == 0;
    }

    public void saveProjects(ArrayList<Project> projects) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName, false))) {
            oos.writeObject(projects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Project> loadProjects() {
        if (isFileEmpty()) {
            System.out.println("File doesn't exist");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<Project>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}