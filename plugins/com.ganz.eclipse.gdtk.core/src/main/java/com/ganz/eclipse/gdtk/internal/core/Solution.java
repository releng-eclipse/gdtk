package com.ganz.eclipse.gdtk.internal.core;

import com.ganz.eclipse.gdtk.internal.util.Logger;

public class Solution {
	private static Solution instance = new Solution();
	public static Solution getInstance() {
		return instance;
	}
	
	public void open() {
		Logger.getInstance().info("Opening solution");
	}
	
	public void close() {
		Logger.getInstance().info("Closing solution");
	}

}
