/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import com.gti.datamerge.Action;
import com.gti.datamerge.database.Row;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Xachman
 */
public class ExpectActionYml {
    private String table;
    private List<ActionYml> actions;
    
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<ActionYml> getActions() {
        return actions;
    }

    public void setActions(List<ActionYml> actions) {
        this.actions = actions;
    }

}
