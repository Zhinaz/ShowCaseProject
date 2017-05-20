package molbak.showcaseproject;

import java.util.UUID;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private int year;

    public Movie() {

    }

    public Movie(String title, String genre, int year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public Movie(String id, String title, String genre, int year) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
