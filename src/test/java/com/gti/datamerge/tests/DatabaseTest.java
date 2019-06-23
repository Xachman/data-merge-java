/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.tests;

import com.gti.datamerge.Action;
import com.gti.datamerge.Config.Config;
import com.gti.datamerge.Database;
import com.gti.datamerge.mocks.ActionYml;
import com.gti.datamerge.mocks.DataYml;
import com.gti.datamerge.mocks.DatabaseConnection;
import com.gti.datamerge.mocks.ExpectActionYml;
import com.gti.datamerge.mocks.ExpectActionsYml;
import com.gti.datamerge.mocks.TableDataYml;
import com.gti.datamerge.mocks.TablesYml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Xachman
 */
public class DatabaseTest {
	private ClassLoader classLoader;
	private DatabaseConnection dbc1;
	private DatabaseConnection dbc2;
	private DatabaseConnection dbc1NR;
	private DatabaseConnection dbc2NR;

	private String getResource(String name) {
		return classLoader.getResource(name).getFile();
	}
	@Before
	public void setUp() {
		classLoader = getClass().getClassLoader();
		try {
			InputStream tablesInput = new FileInputStream(new File(getResource("tables.yml")));
			InputStream tablesNRInput = new FileInputStream(new File(getResource("tables_no_relation.yml")));
			InputStream dataDb1Input = new FileInputStream(new File(getResource("data_db_1.yml")));
			InputStream dataDb2Input = new FileInputStream(new File(getResource("data_db_2.yml")));

			TablesYml tables = new Yaml().loadAs(tablesInput, TablesYml.class);
			TablesYml tablesNR = new Yaml().loadAs(tablesNRInput, TablesYml.class);
			DataYml db1Data = new Yaml().loadAs(dataDb1Input, DataYml.class);
			DataYml db2Data = new Yaml().loadAs(dataDb2Input, DataYml.class);

			dbc1 = new DatabaseConnection(tables, db1Data);	
			dbc2 = new DatabaseConnection(tables, db2Data);
			dbc1NR = new DatabaseConnection(tablesNR, db1Data);
			dbc2NR = new DatabaseConnection(tablesNR, db2Data);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(DatabaseTest.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Test
	public void testTableMerge() throws FileNotFoundException, Exception {
		Database db1 = new Database(dbc1);
		Database db2 = new Database(dbc2);

		List<Action> actions = db1.mergeTableActions("users",db2);

		InputStream actionsInput = new FileInputStream(new File(getResource("expect_actions.yml")));
		ExpectActionsYml expectActions = new Yaml().loadAs(actionsInput, ExpectActionsYml.class);
        int count = 0;
		
        ExpectActionYml eUsers = expectActions.getTableActions("users");

		for(ActionYml action: eUsers.getActions()) {
            Assert.assertEquals(action.getAction(), actions.get(count));
            count++;
        }        

        ExpectActionYml eUsersMeta = expectActions.getTableActions("users_meta");

		for(ActionYml action: eUsersMeta.getActions()) {
            Assert.assertEquals(action.getAction(), actions.get(count));
            count++;
        }        

	}

	@Test
	public void testTableNRMerge() throws FileNotFoundException, Exception {
		Config config = new Config(new File(getResource("config_constraints_for_test.yml")));

		Database db1 = new Database(dbc1NR, config);
		Database db2 = new Database(dbc2NR, config);

		List<Action> actions = db1.mergeTableActions("users",db2);

		InputStream actionsInput = new FileInputStream(new File(getResource("expect_actions.yml")));
		ExpectActionsYml expectActions = new Yaml().loadAs(actionsInput, ExpectActionsYml.class);
		int count = 0;

		ExpectActionYml eUsers = expectActions.getTableActions("users");

		for(ActionYml action: eUsers.getActions()) {
			Assert.assertEquals(action.getAction(), actions.get(count));
			count++;
		}

		ExpectActionYml eUsersMeta = expectActions.getTableActions("users_meta");

		for(ActionYml action: eUsersMeta.getActions()) {
			Assert.assertEquals(action.getAction(), actions.get(count));
			count++;
		}

	}

	@Test
	public void testTableMergeWithMultipleRelated() throws FileNotFoundException, Exception {
		Database db1 = new Database(dbc1);
		Database db2 = new Database(dbc2);

		List<Action> actions = db1.mergeTableActions("users",db2);

		InputStream actionsInput = new FileInputStream(new File(getResource("expect_actions_multiple_related.yml")));
		ExpectActionsYml expectActions = new Yaml().loadAs(actionsInput, ExpectActionsYml.class);
        int count = 0;
		
        ExpectActionYml eUsers = expectActions.getTableActions("users");

		for(ActionYml action: eUsers.getActions()) {
            Assert.assertEquals(action.getAction(), actions.get(count));
            count++;
        }        

        ExpectActionYml eUsersMeta = expectActions.getTableActions("users_meta");

		for(ActionYml action: eUsersMeta.getActions()) {
            Assert.assertEquals(action.getAction(), actions.get(count));
            count++;
        }        
        ExpectActionYml posts = expectActions.getTableActions("posts");

		for(ActionYml action: posts.getActions()) {
            Assert.assertEquals(action.getAction(), actions.get(count));
            count++;
        }        

	}
	@Test
	public void testTableMergeWithManyToMany() throws FileNotFoundException, Exception {
		Database db1 = new Database(dbc1);
		Database db2 = new Database(dbc2);

        List<Action> actions = db1.mergeTablesActions(db2);

		InputStream actionsInput = new FileInputStream(new File(getResource("expect_actions_multiple_related.yml")));
		ExpectActionsYml expectActions = new Yaml().loadAs(actionsInput, ExpectActionsYml.class);

        System.out.println(actions);
        System.out.println("categories");
        int cat = compareActions(expectActions, actions, "categories");
        System.out.println("categories_v_posts");
        int cat_v_posts = compareActions(expectActions, actions, "categories_v_posts");
	}

    private int compareActions(ExpectActionsYml expectActions, List<Action> actions, String tableName) {
        ExpectActionYml eAction  =  expectActions.getTableActions(tableName);
        List<Action> listActions = new ArrayList<>();
        int aCount = 0;
        int result = 0;
        for(Action action: actions) {
            if(action.getTableName().equals(tableName)) {
                listActions.add(action);
                result = aCount;
            }
            aCount++;
        }
        int count = 0;
        for(ActionYml action: eAction.getActions()) {
            System.out.println(action.getAction());
            System.out.println(listActions.get(count));
            Assert.assertEquals(action.getAction(), listActions.get(count));
            count++;
        }
        return result; 
    }
}
