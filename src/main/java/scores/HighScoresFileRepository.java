package scores;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoresFileRepository implements HighScoresRepository {
    private final String filePath;

    public HighScoresFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean create() throws IOException {
        try {
            if (new File(filePath).createNewFile()) {
                return true;
            }
        } catch (IOException exception) {
            throw new IOException("HighScoresFileRepository: Unable to create file");
        }
        return false;
    }

    @Override
    public List<PlayerScore> read() throws IOException {
        try (
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)
        ) {
            List<PlayerScore> readData = new ArrayList<>();
            while (fileInputStream.available() != 0) {
                readData.add((PlayerScore) inputStream.readObject());
            }
            return readData;
        } catch (FileNotFoundException exception) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException exception) {
            throw new IOException("HighScoresFileRepository: Unable to read data from file");
        }
    }

    @Override
    public void update(List<PlayerScore> scores) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (PlayerScore score : scores) {
                outputStream.writeObject(score);
            }
        } catch (IOException exception) {
            throw new IOException("HighScoresFileRepository: Unable to write data to file");
        }
    }
}
