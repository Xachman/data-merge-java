package com.gti.datamerge.config;

import java.util.List;
import java.util.Map;

public class Constraint {
    private String table;
    private List<Map<String,String>> columns;

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }
}
