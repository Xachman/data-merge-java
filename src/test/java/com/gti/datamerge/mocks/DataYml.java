/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Xachman
 */
public class DataYml {
	private List<TableDataYml> tables;	

	public DataYml() {
		
	}

	public List<TableDataYml> getTables() {
		return tables;
	}

	public void setTables(List<TableDataYml> tables) {
		this.tables = tables;
	}
}
