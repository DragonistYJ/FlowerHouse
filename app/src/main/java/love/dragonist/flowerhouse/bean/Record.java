package love.dragonist.flowerhouse.bean;

public class Record {
    private String date;
    private String month;
    private Flower flower;

    public Record() {}

    public Record(String date, String month, Flower flower) {
        this.date = date;
        this.month = month;
        this.flower = flower;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Flower getFlower() {
        return flower;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }
}
