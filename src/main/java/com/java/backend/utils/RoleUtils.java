package com.java.backend.utils;

public class RoleUtils {
	public static boolean hasPermission(int currentLevel, int requiredLevel) {
		return currentLevel >= requiredLevel;
	}
}
