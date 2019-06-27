package com.gti.datamerge.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class Config {
    private File file;
    private List<Table> tables;
    private Yaml yaml;
    private ConfigYML configYML;

    public Config(File file) {
        this.file = file;

        try {
            InputStream IS = new FileInputStream(file);
            yaml = new Yaml(new Constructor(ConfigYML.class));
            configYML = yaml.load(IS);
            tables = configYML.getTables();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Table> getTables() {
        return tables;
    }
}
