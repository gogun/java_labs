package com.sample.model;

public class Good {
    private String name;
    private int cost;

    public Good(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
}
