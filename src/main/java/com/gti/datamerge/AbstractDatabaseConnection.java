/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.util.List;

/**
 *
 * @author Xachman
 */
abstract class AbstractDatabaseConnection implements DatabaseConnectionI{
	private String name;
	private String password;
	private String host;
	private String user;	

	public AbstractDatabaseConnection(String name, String password, String host, String user) {
		this.name = name;
		this.password = password;
		this.host = host;
		this.user = user;
	}	
}
