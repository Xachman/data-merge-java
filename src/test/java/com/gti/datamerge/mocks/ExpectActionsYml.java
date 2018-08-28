/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import java.util.List;

/**
 *
 * @author Xachman
 */
public class ExpectActionsYml {
    private List<ExpectActionYml> actions;   

    public ExpectActionsYml() {
    }

    public List<ExpectActionYml> getActions() {
        return actions;
    }
    public ExpectActionYml getTableActions(String tableName) {
        for(ExpectActionYml e: actions) {
            if(e.getTable().equals(tableName)) {
                return e;
            }
        }
        return null;
    }
    public void setActions(List<ExpectActionYml> actions) {
        this.actions = actions;
    }
}
