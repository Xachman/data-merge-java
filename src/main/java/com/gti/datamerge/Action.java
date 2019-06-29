/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Column;
import com.gti.datamerge.database.Row;

import java.util.Set;

/**
 *
 * @author Xachman
 */
public class Action {
	private int type;
	private Row data;
	private String table;
	public static final int INSERT = 1;
    public static final int UPDATE = 2;

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

    public String getTypeAsString() {
        switch(getType()) {
            case Action.INSERT:
                return "insert";
            case Action.UPDATE:
                return "update";
            default:
                return "none";
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Table=");
        sb.append(getTableName());
        sb.append(" ");
        sb.append("Type=");
        sb.append(getTypeAsString());
        sb.append(" ");
        sb.append("Row=[ ");
        sb.append(getData().toString());
        sb.append(" ]");
        
        return sb.toString();
    }

    public String toJson() {
	    StringBuilder sb = new StringBuilder();

        sb.append("{\"table\":");
	    sb.append("\""+table+"\",");

        sb.append("\"type\":");
        sb.append("\""+getTypeAsString()+"\",");

        sb.append("\"row\":{");
        int count = 0;
        int totalCount = data.getColumns().size();
        for(String column: data.getColumns()) {
            sb.append("\""+column+"\":");
            if(data.getVal(column) == null) {
                sb.append("null");
            } else {
                sb.append("\""+data.getVal(column).replace("\"", "\\\"")+"\"");
            }
            count++;
            if(count < totalCount) {
                sb.append(",");
            }
        }
        sb.append("}}");
	    return sb.toString();
    }
}
