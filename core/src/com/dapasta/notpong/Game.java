package com.dapasta.notpong;

public class Game {
    private int id;
    private String creator;
    private int size;

    public Game(int id, String creator, int size) {
        this.id = id;
        this.creator = creator;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public int getSize() {
        return size;
    }
}
