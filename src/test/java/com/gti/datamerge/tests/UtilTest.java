package com.gti.datamerge.tests;

import com.gti.datamerge.Action;
import com.gti.datamerge.Util;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UtilTest {

    @Test
    public void testCreateActionsFromJson() {
        String json = "["+
                "{\"table\": \"tablename\", \"type\":\"insert\", \"row\": {\"name\": \"tom\", \"id\": \"3\"} },"+
                "{\"table\": \"tablename\", \"type\":\"insert\", \"row\": {\"name\": \"jan\", \"id\": \"4\"} },"+
                "{\"table\": \"tablename\", \"type\":\"insert\", \"row\": {\"name\": \"jim\", \"id\": \"5\"} }"+
                "]";

        List<Action> actions = Util.parseJsonActions(json);

        Assert.assertEquals(3, actions.size());
    }
}
