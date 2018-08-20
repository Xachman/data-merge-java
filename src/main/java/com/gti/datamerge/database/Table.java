/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

import java.util.List;

/**
 *
 * @author Xachman
 */
public class Table {
	private List<Column> columns;
	private String name;
	
	public Table(String name, List<Column> cols) {
		columns = cols;	
		this.name = name;
	}

	public String getName() {
		return name;
	}


}
