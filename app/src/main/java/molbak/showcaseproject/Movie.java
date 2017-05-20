package molbak.showcaseproject;

public class Movie {
    private String id;
    private String title;
    private String description;
    private int year;

    public Movie() {

    }

    public Movie(String title, String description, int year) {
        this.title = title;
        this.description = description;
        this.year = year;
    }

    public Movie(String id, String title, String description, int year) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
