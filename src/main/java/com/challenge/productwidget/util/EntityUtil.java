package com.challenge.productwidget.util;

import java.util.List;

public class EntityUtil {

	public static <T> boolean isEmpltyList(List<T> entities) {

		return (entities.isEmpty() || entities.size() == 0);
	}

	public static <T> boolean isValidList(List<T> entities) {

		return !isEmpltyList(entities);
	}

	public static <T> boolean isNullEntity(T entity) {

		return entity == null;
	}

	public static <T> boolean isNotNullEntity(T entity) {

		return entity != null;
	}

	public static boolean isValidString(String str) {

		return (str != null && str.length() > 0);
	}

	public static String makeString(String str) {
		return "'" + str + "'";
	}
}