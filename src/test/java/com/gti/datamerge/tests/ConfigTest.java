package com.gti.datamerge.tests;

import java.io.File;
import java.io.FileNotFoundException;
import com.gti.datamerge.config.Config;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest extends BaseClass {

    @Test
    public void testConstraints() throws FileNotFoundException {
        Config config = new Config(new File(getResource("config_relations.yml")));
        assertEquals(3, config.getTables().size());

        assertEquals("postsmeta", config.getTables().get(0).getName());
        assertEquals("post_id", config.getTables().get(0).getRelationships().get(0).getColumn());
        assertEquals("id", config.getTables().get(0).getRelationships().get(0).getParent().getColumn());
        assertEquals("posts", config.getTables().get(0).getRelationships().get(0).getParent().getName());

        assertEquals("users_meta", config.getTables().get(1).getName());
        assertEquals("user_id", config.getTables().get(1).getRelationships().get(0).getColumn());
        assertEquals("id", config.getTables().get(1).getRelationships().get(0).getParent().getColumn());
        assertEquals("users", config.getTables().get(1).getRelationships().get(0).getParent().getName());

        assertEquals("category_v_posts", config.getTables().get(2).getName());
        assertEquals("post_id", config.getTables().get(2).getRelationships().get(0).getColumn());
        assertEquals("id", config.getTables().get(2).getRelationships().get(0).getParent().getColumn());
        assertEquals("posts", config.getTables().get(2).getRelationships().get(0).getParent().getName());
        assertEquals("category_id", config.getTables().get(2).getRelationships().get(1).getColumn());
        assertEquals("cat_id", config.getTables().get(2).getRelationships().get(1).getParent().getColumn());
        assertEquals("categories", config.getTables().get(2).getRelationships().get(1).getParent().getName());

    }
}
