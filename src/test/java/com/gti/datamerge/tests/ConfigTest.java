package com.gti.datamerge.tests;

import java.io.File;
import java.io.FileNotFoundException;
import com.gti.datamerge.Config.Config;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest extends BaseClass {

    @Test
    public void testConstraints() throws FileNotFoundException {
        Config config = new Config(new File(getResource("config_constraints.yml")));
        assertEquals(3, config.getConstraints().size());

        assertEquals("postsmeta", config.getConstraints().get(0).getTable());
        assertEquals("post_id", config.getConstraints().get(0).getColumns().get(0).get("name"));
        assertEquals("id", config.getConstraints().get(0).getColumns().get(0).get("key"));
        assertEquals("posts", config.getConstraints().get(0).getColumns().get(0).get("table"));

        assertEquals("usersmeta", config.getConstraints().get(1).getTable());
        assertEquals("user_id", config.getConstraints().get(1).getColumns().get(0).get("name"));
        assertEquals("id", config.getConstraints().get(1).getColumns().get(0).get("key"));
        assertEquals("users", config.getConstraints().get(1).getColumns().get(0).get("table"));

        assertEquals("category_v_posts", config.getConstraints().get(2).getTable());
        assertEquals("post_id", config.getConstraints().get(2).getColumns().get(0).get("name"));
        assertEquals("id", config.getConstraints().get(2).getColumns().get(0).get("key"));
        assertEquals("posts", config.getConstraints().get(2).getColumns().get(0).get("table"));
        assertEquals("category_id", config.getConstraints().get(2).getColumns().get(1).get("name"));
        assertEquals("cat_id", config.getConstraints().get(2).getColumns().get(1).get("key"));
        assertEquals("categories", config.getConstraints().get(2).getColumns().get(1).get("table"));
    }
}
