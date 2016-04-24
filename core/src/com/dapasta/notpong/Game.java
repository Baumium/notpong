package com.dapasta.notpong;

public class Game {
    private String id;
    private String creator;
    private String name;
    private int size;

    public Game(String id, String creator, String name) {
        this.id = id;
        this.creator = creator;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
