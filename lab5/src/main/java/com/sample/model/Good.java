package com.sample.model;

public class Good {
    private String name;
    private int cost;

    public Good(String name, Integer cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public Integer getCost() {
        return cost;
    }
}
