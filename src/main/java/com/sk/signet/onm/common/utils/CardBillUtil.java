package com.sk.signet.onm.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

/**
 * @fileName : CardBillUtil.java
 * @date : 2022. 02. 23.
 * @author :
 * @description : 결제카드 빌키 발급 유틸
 *              ================================================
 *              DATE AUTHOR NOTE
 *              2022.02.23 김재균 최초작성
 * 
 */

@Component("cardBillUtil")
public class CardBillUtil {

	static final String propertiName = "locale/op_nicepay";
	static String MID = PropertyConfig.getPropertiesCon(propertiName, "HOMENSERVICE.MID.3");
	static String merchantKey = PropertyConfig.getPropertiesCon(propertiName, "HOMENSERVICE.MERCHANTKEY.3");

	static String SIGNET_MID = PropertyConfig.getPropertiesCon(propertiName, "SIGNETNICEPAY.MID");
	static String SIGNET_merchantKey = PropertyConfig.getPropertiesCon(propertiName, "SIGNETNICEPAY.MERCHANTKEY");

	/**
	 * 결제카드 빌키 발급 프로세서
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectPayCardBillKey(Map param) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		String Moid = "nice123456"; // 주문번호
		String CardNo = StringUtil.toNotNullString(param.get("payCardNumberPop")); // 카드번호
		String ExpYear = StringUtil.toNotNullString(param.get("payCreditYearPop")); // 유효기간(년)
		if (ExpYear != null && ExpYear.length() == 4) {
			ExpYear = ExpYear.substring(2, 4);
		}
		String ExpMonth = StringUtil.toNotNullString(param.get("payCreditMonthPop")); // 유효기간(월)
		String IDNo = StringUtil.toNotNullString(param.get("paybirthDayPop")); // 생년월일/사업자번호
		String CardPw = StringUtil.toNotNullString(param.get("payPassTwoPop")); // 카드비밀번호
		String BuyerName = StringUtil.toNotNullString(param.get("customerNameBillPop")); // 구매자
		String EdiType = StringUtil.toNotNullString(param.get("EdiType")); // 응답전문 유형

		String payCompanyId = StringUtil.toNotNullString(param.get("payCompanyId")); // 서비스사업자

		if ("28".equals(payCompanyId)) { // SK시그넷 일산테크노타운
			MID = SIGNET_MID;
			merchantKey = SIGNET_merchantKey;
		}

		/*
		 *******************************************************
		 * <해쉬암호화> (수정하지 마세요)
		 * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
		 * SignData 생성
		 *******************************************************
		 */
		DataEncrypt sha256Enc = new DataEncrypt();
		String ediDate = DateFormatUtil.getToday();
		String SignData = sha256Enc.encrypt(MID + ediDate + Moid + merchantKey);

		/*
		 *******************************************************
		 * <AES암호화> (수정하지 마세요)
		 * AES 암호화는 결제 카드정보를 암호화 하기 위한 방법입니다.
		 *******************************************************
		 */
		StringBuffer EncDataBuf = new StringBuffer();
		EncDataBuf.append("CardNo=").append(CardNo).append("&");
		EncDataBuf.append("ExpYear=").append(ExpYear).append("&");
		EncDataBuf.append("ExpMonth=").append(ExpMonth).append("&");
		EncDataBuf.append("IDNo=").append(IDNo).append("&");
		EncDataBuf.append("CardPw=").append(CardPw);
		String EncData = encryptAES(EncDataBuf.toString(), merchantKey.substring(0, 16));

		/*
		 ****************************************************************************************
		 * <승인 요청>
		 * 승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다.
		 ****************************************************************************************
		 */
		StringBuffer requestData = new StringBuffer();
		requestData.append("MID=").append(MID).append("&");
		requestData.append("EdiDate=").append(ediDate).append("&");
		requestData.append("Moid=").append(Moid).append("&");
		requestData.append("EncData=").append(EncData).append("&");
		requestData.append("SignData=").append(SignData).append("&");
		requestData.append("BuyerName=").append(URLEncoder.encode(BuyerName, "euc-kr")).append("&");
		/* requestData.append("BuyerEmail=").append(BuyerEmail).append("&"); */
		/* requestData.append("BuyerTel=").append(BuyerTel).append("&"); */
		requestData.append("CharSet=").append("euc-kr").append("&");
		requestData.append("EdiType=").append(EdiType);

		String resultJsonStr = connectToServer(requestData.toString(),
				"https://webapi.nicepay.co.kr/webapi/billing/billing_regist.jsp");
		HashMap resultData = new HashMap();

		if (!"ERROR".equals(resultJsonStr)) {
			if ("KV".equals(EdiType)) {
				resultData = stringToHashMap(resultJsonStr);
			} else {
				resultData = jsonStringToHashMap(resultJsonStr);
			}
			System.out.println(resultData);
			result.put("billKey", StringUtil.toNotNullString(resultData.get("BID")));
			result.put("billCardName", StringUtil.toNotNullString(resultData.get("CardName")));
			result.put("CardCode", StringUtil.toNotNullString(resultData.get("CardCode")));
			result.put("ResultMsg", StringUtil.toNotNullString(resultData.get("ResultMsg")));
			result.put("ResultCode", StringUtil.toNotNullString(resultData.get("ResultCode")));
		}

		return result;
	}

	// SHA-256 형식으로 암호화
	public class DataEncrypt {
		MessageDigest md;
		String strSRCData = "";
		String strENCData = "";
		String strOUTData = "";

		public DataEncrypt() {
		}

		public String encrypt(String strData) {
			String passACL = null;
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-256");
				md.reset();
				md.update(strData.getBytes());
				byte[] raw = md.digest();
				passACL = encodeHex(raw);
			} catch (Exception e) {
				System.out.print("암호화 에러" + e.toString());
			}
			return passACL;
		}

		public String encodeHex(byte[] b) {
			char[] c = Hex.encodeHex(b);
			return new String(c);
		}
	}

	/**
	 * 대외 통신 샘플.
	 * 외부 기관과 URL 통신하는 샘플 함수 입니다.
	 * 샘플소스는 서비스 안정성을 보장하지 않으므로, 가맹점 환경에 맞게 구현 바랍니다.
	 * 샘플소스 이용에 따른 이슈 발생시 NICEPAY에서 책임지지 않습니다.
	 */
	public static String connectToServer(String data, String reqUrl) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader resultReader = null;
		PrintWriter pw = null;
		URL url = null;

		int statusCode = 0;
		StringBuffer recvBuffer = new StringBuffer();
		try {
			url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(25000);
			conn.setDoOutput(true);

			pw = new PrintWriter(conn.getOutputStream());
			pw.write(data);
			pw.flush();

			statusCode = conn.getResponseCode();
			resultReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "euc-kr"));
			for (String temp; (temp = resultReader.readLine()) != null;) {
				recvBuffer.append(temp).append("\n");
			}

			if (!(statusCode == HttpURLConnection.HTTP_OK)) {
				throw new Exception("ERROR");
			}

			return recvBuffer.toString().trim();
		} catch (Exception e) {
			return "ERROR";
		} finally {
			recvBuffer.setLength(0);

			try {
				if (resultReader != null) {
					resultReader.close();
				}
			} catch (Exception ex) {
				resultReader = null;
			}

			try {
				if (pw != null) {
					pw.close();
				}
			} catch (Exception ex) {
				pw = null;
			}

			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception ex) {
				conn = null;
			}
		}
	}

	// json형태의 String을 HashMap으로 변환해주는 샘플
	private static HashMap jsonStringToHashMap(String str) throws Exception {
		HashMap dataMap = new HashMap();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(str);
			JSONObject jsonObject = (JSONObject) obj;

			Iterator<String> keyStr = jsonObject.keySet().iterator();
			while (keyStr.hasNext()) {
				String key = keyStr.next();
				Object value = jsonObject.get(key);

				dataMap.put(key, value);
			}
		} catch (Exception e) {

		}
		return dataMap;
	}

	// key&value 형태(a=1&b=2)의 String을 HashMap으로 변환해주는 샘플
	private static HashMap stringToHashMap(String str) throws Exception {
		HashMap dataMap = new HashMap();
		StringTokenizer token = new StringTokenizer(str, "&");
		String temp = "";

		while (token.hasMoreElements()) {
			try {
				temp = token.nextToken();
				dataMap.put(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1));
			} catch (IndexOutOfBoundsException e) {

			}
		}

		return dataMap;
	}

	// hex aes 암호화
	public static String encryptAES(String input, String key) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < crypted.length; i++) {
				String hex = Integer.toHexString(crypted[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
