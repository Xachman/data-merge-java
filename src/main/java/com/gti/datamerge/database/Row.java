/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

import java.util.Map;

/**
 *
 * @author Xachman
 */
public class Row {
	private Map<String, String> entries;	

	public void add(String column, String value) {
		entries.put(column, value);
	}

	public Map getMap() {
		return entries;
	}
	
	public String getVal(String column) {
		return entries.get(column);
	}

}
