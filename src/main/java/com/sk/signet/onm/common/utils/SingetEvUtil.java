/* ---------------------------------------------------------------------
 * @(#)Validator.java 
 * @Creator    yhlee (yhlee@handysoft.co.kr)
 * @version    1.0
 * @date       2006-04-05
 * ---------------------------------------------------------------------
 */
package com.sk.signet.onm.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.weaver.ast.Var;

/**
 * 
 * @since 1.0
 * @version 2020-03-04
 */
public final class SingetEvUtil {

	public static boolean isNumeric(String string) {

		if (string.equals("")) {
			// 문자열이 공백인지 확인
			return false;
		}
		return string.matches("-?\\d+(\\.\\d+)?");
	}

}