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
	private int type;
	private Row data;
	private String table;
	public static final int INSERT = 1;

	public Action(int type, Row data, String table) {
		this.type = type;
		this.data = data;
		this.table = table;
	}

	public int getType() {
		return type;
	}

	public Row getData() {
		return data;
	}
   
	public String getTableName() {
		return table;
	}	

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof Action))return false;

        Action a = (Action) o;

        if(a.getTableName().equals(this.getTableName()) && 
           a.getType() == getType() &&
           a.getData().equals(getData())) return true;
        return false;
    }
}
