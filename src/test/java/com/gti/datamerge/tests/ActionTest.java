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

    @Test
    public void testActionToJsonSanitize() {
        Row row = new Row();
        row.setVal("id", "1");
        row.setVal("name", "john is \"Cool!\"");
        row.setVal("number", "555-555-5555");
        Action action = new Action(Action.INSERT, row, "table");

        String jsonString = "{\"table\":\"table\",\"type\":\"insert\",\"row\":{\"number\":\"555-555-5555\",\"name\":\"john is \\\"Cool!\\\"\",\"id\":\"1\"}}";

        assertEquals(jsonString, action.toJson());
    }

    @Test
    public void testActionToJsonLineBreakSanitize() {
        Row row = new Row();
        row.setVal("id", "1");
        row.setVal("name", "john is \n \"Cool!\"");
        row.setVal("number", "555-555-5555");
        Action action = new Action(Action.INSERT, row, "table");

        String jsonString = "{\"table\":\"table\",\"type\":\"insert\",\"row\":{\"number\":\"555-555-5555\",\"name\":\"john is \\n \\\"Cool!\\\"\",\"id\":\"1\"}}";

        assertEquals(jsonString, action.toJson());

        row.setVal("id", "1");
        row.setVal("name", "john is \r\n \"Cool!\"");
        row.setVal("number", "555-555-5555");

        action = new Action(Action.INSERT, row, "table");

        jsonString = "{\"table\":\"table\",\"type\":\"insert\",\"row\":{\"number\":\"555-555-5555\",\"name\":\"john is \\n \\\"Cool!\\\"\",\"id\":\"1\"}}";

        assertEquals(jsonString, action.toJson());
    }
}
