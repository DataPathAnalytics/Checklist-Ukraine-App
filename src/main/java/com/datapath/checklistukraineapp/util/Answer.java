package com.datapath.checklistukraineapp.util;

public enum Answer {

    YES(1, "ТАК"),
    NO(2, "Ні"),
    INAPPLICABLE(3, "НЗ");

    private int value;
    private String name;

    Answer(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
