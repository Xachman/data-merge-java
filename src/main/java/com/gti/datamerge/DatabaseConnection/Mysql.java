/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.DatabaseConnection;

import com.gti.datamerge.AbstractDatabaseConnection;
import com.gti.datamerge.Action;
import com.gti.datamerge.database.Column;
import com.gti.datamerge.database.Relationship;
import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Types;

/**
 *
 * @author Xachman
 */
public class Mysql extends AbstractDatabaseConnection {
    private String connectionUrl; 
    public Mysql(String conn) {
       this.connectionUrl = conn; 
    }

    private Connection connection() {

        Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
            conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;

    }
   
    private String columnToString(int type, ResultSet rs, int id) throws SQLException {
       switch(type) {
            case Types.INTEGER:
                return Integer.toString(rs.getInt(id));
            default:
                return rs.getString(id);

       } 
    }
    
    @Override
    public Row get(Table table, int id) {
        String sql = "SELECT * FROM "+table.getName()+" WHERE "+table.getPrimaryKey()+"="+id;
        return executeQuery(sql).get(0);
    }
    private List<Row> executeQuery(String sql) {
        Connection conn = connection();
        Statement stmt;
        List<Row> rows = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rows = resultSetToRows(rs);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rows;
    }
    private List<Row> resultSetToRows(ResultSet rs) throws SQLException {
        List<Row> rows = new ArrayList<>();

        ResultSetMetaData rsmd = rs.getMetaData();
        Row row = new Row();

        while(rs.next()) {
            for(int i = 0; i < rsmd.getColumnCount(); i++) {
                int type = rsmd.getColumnType(i);
                String columnName = rsmd.getColumnName(i);
                
                row.put(columnName, columnToString(type, rs, i));
                rows.add(row);
            }
        }
        return rows;
    }
    @Override
    public List<Row> getAll(Table table) {
        String sql = "SELECT * FROM "+table.getName();
        return executeQuery(sql);
    }

    @Override
    public List<Row> getAll(String tableName) {
        String sql = "SELECT * FROM "+tableName;
        return executeQuery(sql);
    }

    @Override
    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        Connection conn = null;
        try {
            conn = connection();
            String sql = "SHOW TABLES";
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs.getMetaData().getColumnName(1));
            while(rs.next()) {
                String tableName = rs.getString(1);
                Table table = createTable(tableName, conn);

                tables.add(table);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null)
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return tables;
    }

    private Table createTable(String tableName, Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "DESCRIBE "+tableName; 
        ResultSet rs = stmt.executeQuery(sql);
        List<Column> columns = new ArrayList<>();
        System.out.println(sql);
        String primaryKey = null;
        Relationship relationship = getTableRelationship(tableName, conn);
        while(rs.next()) {
            String name = rs.getString(1);
            String key = rs.getString(4);
            if(key.equals("PRI")) {
                primaryKey = name;
            }
            
            columns.add(new Column(name));
        }
        return new Table(tableName, columns, primaryKey,  0, relationship);
    }

    private Relationship getTableRelationship(String tableName, Connection conn) throws SQLException {
        
        String sql = "SELECT \n" +
                    "  `TABLE_SCHEMA`,                          -- Foreign key schema\n" +
                    "  `TABLE_NAME`,                            -- Foreign key table\n" +
                    "  `COLUMN_NAME`,                           -- Foreign key column\n" +
                    "  `REFERENCED_TABLE_SCHEMA`,               -- Origin key schema\n" +
                    "  `REFERENCED_TABLE_NAME`,                 -- Origin key table\n" +
                    "  `REFERENCED_COLUMN_NAME`                 -- Origin key column\n" +
                    "FROM\n" +
                    "  `INFORMATION_SCHEMA`.`KEY_COLUMN_USAGE`  -- Will fail if user don't have privilege\n" +
                    "WHERE\n" +
                    "  `TABLE_SCHEMA` = SCHEMA()                -- Detect current schema in USE \n" +
                    "  AND `TABLE_NAME`=\""+tableName+"\""+
                    "  AND `REFERENCED_TABLE_NAME` IS NOT NULL; -- Only tables with foreign keys";
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {

            System.out.println("-------");
            System.out.println(rs.getString("TABLE_NAME"));
            String column = rs.getString("COLUMN_NAME");
            String refTable = rs.getString("REFERENCED_TABLE_NAME");
            String refColumn = rs.getString("REFERENCED_COLUMN_NAME");
            System.out.println("-------");
            return new Relationship(refTable, column, refColumn);
        }
        return null;
    }

    @Override
    public int getNextIncrement(String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setActions(List<Action> actions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
