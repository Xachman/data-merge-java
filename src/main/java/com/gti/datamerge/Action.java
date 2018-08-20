/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Column;
import com.gti.datamerge.database.Row;

/**
 *
 * @author Xachman
 */
public class Action {
	private String type;
	private Row data;
	private String table;

	public Action(String type, Row data, String table) {
		this.type = type;
		this.data = data;
		this.table = table;
	}

	public String getType() {
		return type;
	}

	public Row getData() {
		return data;
	}

	public String getTableName() {
		return table;
	}	
}
