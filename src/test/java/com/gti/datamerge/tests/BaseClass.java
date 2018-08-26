/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.tests;

/**
 *
 * @author Xachman
 */
public abstract class BaseClass {
	protected String getResource(String name) {
		return getClass().getClassLoader().getResource(name).getFile();
	}
}
