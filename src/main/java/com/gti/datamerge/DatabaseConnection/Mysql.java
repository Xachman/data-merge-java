/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.DatabaseConnection;

import com.gti.datamerge.Action;
import com.gti.datamerge.DatabaseConnectionI;
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
public class Mysql implements DatabaseConnectionI {
    private String connectionUrl; 
    private List<Action> actions;
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

        while(rs.next()) {
            Row row = new Row();
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                int type = rsmd.getColumnType(i);
                String columnName = rsmd.getColumnName(i);
                 
                row.put(columnName, rs.getString(i));
            }
            rows.add(row);
        }
        return rows;
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
        String primaryKey = null;
        Relationship relationship = getTableRelationship(tableName, conn);
        List<Relationship> relationships = new ArrayList<>();
        if(relationship != null) {
            relationships.add(relationship);
        }
        while(rs.next()) {
            String name = rs.getString(1);
            String key = rs.getString(4);
            String type = rs.getString(2);
            rs.getMetaData();
            if(key.equals("PRI")) {
                primaryKey = name;
            }

            if(type.contains("int")) {
                columns.add(new Column(name, Column.NUMBER));
                continue;
            }
            if(type.contains("datetime")) {
                columns.add(new Column(name, Column.DATETIME));
                continue;
            }
            columns.add(new Column(name, Column.STRING));
        }
        return new Table(tableName, columns, primaryKey,  getTableIncrement(tableName, conn)-1, relationships);
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
            String column = rs.getString("COLUMN_NAME");
            String refTable = rs.getString("REFERENCED_TABLE_NAME");
            String refColumn = rs.getString("REFERENCED_COLUMN_NAME");
            return new Relationship(refTable, column, refColumn);
        }
        return null;
    }
    private int getTableIncrement(String table, Connection conn) throws SQLException {
        String sql = "SELECT `AUTO_INCREMENT`\n" +
                    "FROM  INFORMATION_SCHEMA.TABLES\n" +
                    "WHERE TABLE_SCHEMA = SCHEMA()\n" +
                    "AND   TABLE_NAME   = '"+table+"';";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public void commit() {
        for(Action action: actions){
            executeAction(action);
        }
    }
    private void executeAction(Action action) {
        Connection conn = connection();
        try {
            switch(action.getType()) {
                case Action.INSERT:
                    executeInsert(action, conn);
                    break;
                case Action.UPDATE:
                    executeUpdate(action, conn);
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
    private void executeInsert(Action action, Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(action.getTableName());
        sb.append(" ");
        sb.append(formatRowForInsert(action.getTableName(), action.getData()));
        Statement stmt =  conn.createStatement();
        stmt.execute(sb.toString());
    }

    private void executeUpdate(Action action, Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(action.getTableName());
        sb.append(" SET ");
        sb.append(formatRowForUpdate(action.getTableName(), action.getData()));
        String pk = this.getTable(action.getTableName()).getPrimaryKey();
        sb.append(" WHERE "+pk+"='"+action.getData().getVal(pk)+"'");
        Statement stmt =  conn.createStatement();
        stmt.execute(sb.toString());
    }
    private String formatRowForInsert(String tableName, Row row) {
        StringBuilder columns =  new StringBuilder();
        StringBuilder values = new StringBuilder();
        columns.append("(");
        values.append("(");
        Table table = getTable(tableName);
        int count=0;
        for(Column column: table.getColumns()) {
            count++;
            if(count > 1) {
                values.append(",");
                columns.append(",");
            }
            values.append(formatValue(column, row.getVal(column.getName())));
            columns.append("`"+column.getName()+"`");
        }
        columns.append(") VALUES ");
        values.append(")");
        return columns.toString()+values.toString();
    }
    private String formatRowForUpdate(String tableName, Row row) {
        StringBuilder values = new StringBuilder();
        Table table = getTable(tableName);
        int count=0;
        for(Column column: table.getColumns()) {
            count++;
            if(count > 1) {
                values.append(",");
            }
            values.append(column.getName());
            values.append("=");
            values.append(formatValue(column, row.getVal(column.getName())));
        }
        return values.toString();
    }
    private String formatValue(Column column, String value) {
        switch(column.getType()) {
            case Column.NUMBER:
                return value;
            case Column.DATETIME:
                if(value == null) return "'0000-00-00 00:00:00.0'";
            default:
                return "'"+value.replace("'","''")+"'";
        }
    }

    private Table getTable(String tableName) {
        for(Table table: getAllTables()) {
            if(table.getName().equals(tableName))
                return table;
        }
        return null;
    }
}
