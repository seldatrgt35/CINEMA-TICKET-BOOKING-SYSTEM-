import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface Admin {
    void addNewFilm() throws IOException;
    void saveMovies(List<Film> films) throws IOException;
    void deleteFilm() throws IOException;
    void addNewMovieSession() throws IOException;
    void deleteSession() throws IOException;
    void reportsofticketsold() throws IOException;



}
