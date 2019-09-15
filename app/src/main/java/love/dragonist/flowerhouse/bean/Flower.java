package love.dragonist.flowerhouse.bean;

import android.net.Uri;

import java.util.Objects;

public class Flower {
    private int id;
    private String name;
    private String language;
    private String poem;
    private String introduce;
    private String genus;
    private String family;
    private String img;

    public Flower() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPoem() {
        return poem;
    }

    public void setPoem(String poem) {
        this.poem = poem;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", poem='" + poem + '\'' +
                ", introduce='" + introduce + '\'' +
                ", genus='" + genus + '\'' +
                ", family='" + family + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return id == ((Flower) o).getId();
    }
}
