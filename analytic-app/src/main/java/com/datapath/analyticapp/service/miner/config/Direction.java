package com.datapath.analyticapp.service.miner.config;

public enum Direction {
    IN("<-", "-") ,
    OUT("-", "->");

    private String start;
    private String end;

    Direction(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
