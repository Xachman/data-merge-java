/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author xach
 */
public class DataMerge {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
	
		// TODO code application logic here
		Options options	= new Options();

		Option database1Name = new Option("d1", "database-1-name", true, "First database name");
		database1Name.setRequired(true);
		Option database1Password = new Option("d1p", "database-1-password", true, "First database password");
		Option database1Host = new Option("d1h", "database-1-host", true, "First database hostname or ip of host");
		database1Host.setRequired(true);
		Option database1Username = new Option("d1u", "database-1-username", true, "First database username");

		options.addOption(database1Name);
		options.addOption(database1Password);
		options.addOption(database1Host);
		options.addOption(database1Username);

		Option database2Name = new Option("d2", "database-2-name", true, "Second database name");
		database2Name.setRequired(true);
		Option database2Password = new Option("d2p", "database-2-password", true, "Second database password");
		Option database2Host = new Option("d2h", "database-2-host", true, "Second database hostname or ip of host");
		database2Host.setRequired(true);
		Option database2Username = new Option("d2u", "database-2-username", true, "Second database username");

		options.addOption(database2Name);
		options.addOption(database2Password);
		options.addOption(database2Host);
		options.addOption(database2Username);

		Option connectionType = new Option("t", "type", true, "Type of connection ex: mysql");

		options.addOption(connectionType);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException ex) {
			System.out.println(ex.getMessage());
			formatter.printHelp("database-merge", options);
			System.exit(1);
		}


	}
	
	public void mergeData() {
		
	}	
}
