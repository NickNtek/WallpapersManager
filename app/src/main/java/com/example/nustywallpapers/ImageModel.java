package com.example.nustywallpapers;

public class ImageModel {
    private int id;

    private String path;

    private String name;

    private int hash;

    private boolean current;

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


    //CONSTRUCTORS
    public ImageModel(String path, String name, int hash, boolean current) {
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.current = current;
    }

    public ImageModel(int id, String path, String name, int hash, boolean current) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.current = current;
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
                ", hash='" + hash + '\'' +
                ", current=" + current +
                '}';
    }
}
