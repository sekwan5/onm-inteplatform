package com.sk.signet.onm.common.utils;

import java.security.MessageDigest;

/**
 * 암호화 유틸 
 * @packagename : com.sk.signet.onm.common.utils
 * @filename 	: EncryptUtil.java 
 * @since 		: 2022.10.17 
 * @description : 
 * =================================================================
 * Date				Author			Version			Note			
 * -----------------------------------------------------------------
 * 2022.10.17 		Heo, Sehwan		1.0				최초 생성
 * -----------------------------------------------------------------
 */
public class EncryptUtil {

	/**
	 * 문자열을 SHA-512 방식으로 해시암호화.
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText)  {
		try {
			String encString = "";
			StringBuffer sb = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(plainText.getBytes());
			byte[] msgb = md.digest();
			
			for (int i = 0; i < msgb.length; i++) {
				byte temp = msgb[i];
				String str = Integer.toHexString(temp & 0xFF);
				while (str.length() < 2) {
					str = "0" + str;
				}
				str = str.substring(str.length() - 2);
				sb.append(str);
			}
			encString = sb.toString();
			return encString;
		}catch(Exception ext) {
			return "";
		}
	}
}
