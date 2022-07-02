package com.example.nustywallpapers;

public class ImageModel {
    private int id;

    private String path;

    private String name;

    private int hash;

    private boolean current;

    private boolean first;

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


    //CONSTRUCTORS
    public ImageModel(String path, String name, int hash, boolean current, boolean first) {
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.current = current;
        this.first = first;
    }

    public ImageModel(int id, String path, String name, int hash, boolean current, boolean first) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.current = current;
        this.first = first;
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
                '}';
    }
}
