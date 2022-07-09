package com.example.nustywallpapers;

public class ImageModel {
    private int id;

    private String path;

    private String name;

    private int hash;

    private boolean current;

    private boolean first;

    private boolean last;

    //GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFisrt(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    //CONSTRUCTORS
    public ImageModel(String path, String name, int hash, boolean current, boolean first, boolean last) {
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.current = current;
        this.first = first;
        this.last = last;
    }

    public ImageModel(int id, String path, String name, int hash, boolean current, boolean first, boolean last) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.current = current;
        this.first = first;
        this.last = last;
    }

    public ImageModel() {
    }

    //toString


    @Override
    public String toString() {
        return "ImageModel{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", hash=" + hash +
                ", current=" + current +
                ", first=" + first +
                ", last=" + last +
                '}';
    }
}
