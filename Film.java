public class Film {

    private String movieName;
    private String director;
    private int movieTime;
    private String movieType;

    public Film(String movieName , String director , int movieTime , String movieType) {

        this.movieName = movieName;
        this.director = director;
        this.movieTime = movieTime;
        this.movieType = movieType;
    }


    public String getMovieName() {
        return movieName;
    }
    public String getDirector() {
        return director;
    }
    public int getMovieTime() {
        return movieTime;
    }
    public void setFilmName(String filmName) {
        this.movieName = filmName;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setMovieTime(int movieTime) {
        this.movieTime = movieTime;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }
}
