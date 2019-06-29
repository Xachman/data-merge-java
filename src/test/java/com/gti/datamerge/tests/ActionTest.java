package com.gti.datamerge.tests;

import com.gti.datamerge.Action;
import com.gti.datamerge.database.Row;
import static org.junit.Assert.*;
import org.junit.Test;

public class ActionTest {

    @Test
    public void testActionToJson() {
        Row row = new Row();
        row.setVal("id", "1");
        row.setVal("name", "john");
        row.setVal("number", "555-555-5555");
        Action action = new Action(Action.INSERT, row, "table");

        String jsonString = "{\"table\":\"table\",\"type\":\"insert\",\"row\":{\"number\":\"555-555-5555\",\"name\":\"john\",\"id\":\"1\"}}";

        assertEquals(jsonString, action.toJson());
    }
}
