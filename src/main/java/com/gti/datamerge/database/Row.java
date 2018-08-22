/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Xachman
 */
public class Row {
	private Map<String, String> entries = new HashMap<String,String>();

	public void put(String column, String value) {
		entries.put(column, value);
	}

	public Map<String, String> getMap() {
		return entries;
	}
	
	public String getVal(String column) {
		return entries.get(column);
	}
    
    public  Set<String> getColumns() {
        return entries.keySet();
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Row)) return false;

        Row r = (Row) o;
        System.out.println(r.getMap());
        System.out.println(getMap());
        if(r.getMap().size() != r.getMap().size()) return false;
        for(String column: r.getColumns()) {
            if(!r.getVal(column).equals(getVal(column))) return false;
        }


        return true;

    }
}
