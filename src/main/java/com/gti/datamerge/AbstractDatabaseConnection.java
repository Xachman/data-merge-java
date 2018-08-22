/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xachman
 */
public abstract class AbstractDatabaseConnection implements DatabaseConnectionI{
    @Override
    public List<Table> getRelatedTables(Table table) {
        List<Table> result = new ArrayList<>();
        for(Table tItem: getAllTables())  {
            if(tItem.hasRelationship(table)) {
                result.add(tItem);
            }
        }

        return result;
    }
}
