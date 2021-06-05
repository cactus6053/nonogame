package com.example.pa2;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class image {
    private String lastBuildDate;
    private String total;
    private String start;
    private String display;
    private List<Map> items;

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

}
