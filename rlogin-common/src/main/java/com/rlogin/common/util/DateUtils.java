package com.rlogin.common.util;

import java.util.Date;

import org.joda.time.DateTime;

public class DateUtils {

	public static Date strToDate(String str) {
		try {
			return new DateTime(str).toDate();
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

}
