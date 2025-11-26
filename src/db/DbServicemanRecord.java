package db;

public class DbServicemanRecord {
    private long id;
    private String type;     // "S" - Soldier, "O" - Officer
    private String name;
    private String rank;
    private int years;
    private String position; // може бути порожнім для солдата

    public DbServicemanRecord() {
    }

    public DbServicemanRecord(long id, String type, String name, String rank, int years, String position) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.rank = rank;
        this.years = years;
        this.position = position;
    }

    public DbServicemanRecord(String type, String name, String rank, int years, String position) {
        this(0, type, name, rank, years, position);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
