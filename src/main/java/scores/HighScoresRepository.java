package scores;

import java.io.IOException;
import java.util.List;

public interface HighScoresRepository {
    boolean create() throws IOException;

    List<PlayerScore> read() throws IOException;

    void update(List<PlayerScore> scores) throws IOException;
}
