package molbak.showcaseproject;

public class Player {
    private int id;
    private String name;
    private String placement;

    public Player(int id, String name, String placement) {
        this.id = id;
        this.name = name;
        this.placement = placement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
}
