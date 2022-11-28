package com.sk.signet.onm.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sk.signet.onm.web.service.MessageService;

public class NoticeTalkUtil {
	protected static Logger logger = LoggerFactory.getLogger(NoticeTalkUtil.class);

	// @Autowired
	// @Inject
	@Resource(name = "messageService")
	private static MessageService messageService;

	public static Map<String, Object> processSendTalk(HashMap<String, Object> param) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			logger.info("makeMessage param 전 ===== ");
			logger.info("makeMessage param ===== " + param.toString());
			resultMap = makeMessage(param);
			try {
				/* SqlSession session = MariaDbStatement.getSqlSession(); */
				/**
				 * TODO
				 * 1. ECOS DB 이력적제
				 */
				/* session.insert( "insertNotiTalkSendLog", resultMap ); */

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("processSendSms result : " + resultMap);
		} catch (Exception ex) {
			logger.error("Error in parsing processSendSms Data.", ex);
		}

		return resultMap;
	}

	public static Map<String, Object> makeMessage(HashMap<String, Object> param) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		logger.info("메세지 param 들어올때" + param.toString());
		String template = (String) param.get("template");
		// 모바일도 올려주어야한다.
		String phone = "";
		String message = null;

		/*
		 * ##############################
		 * 예약완료 안내(관리자)
		 * ##############################
		 */
		if ("10006".equals(template)) {
			message = (String) param.get("var1") + "고객님, 충전시스템 관리자에 의해 예약이 완료되었습니다. \r\n";
			message += "예약일시 : " + (String) param.get("var2") + "\r\n";
			message += "충전소 : " + (String) param.get("var3");
			message += "충전기 : " + (String) param.get("var4") + "\r\n";
			message += "커넥터 : " + (String) param.get("var5") + "\r\n";
			message += "해당 예약시점 10분 경과 후 미사용시 예약이 자동 취소됩니다. 감사합니다.";
		}

		/*
		 * ##############################
		 * 예약완료 안내(사용자)
		 * ##############################
		 */
		if ("10007".equals(template)) {
			message = (String) param.get("var1") + "고객님, 충전 예약이 완료되었습니다 \r\n";
			message += "예약일시 : " + (String) param.get("var2") + "\r\n";
			message += "충전소 : " + (String) param.get("var3");
			message += "충전기 : " + (String) param.get("var4") + "\r\n";
			message += "커넥터 : " + (String) param.get("var5") + "\r\n";
			message += "해당 예약시점 10분 경과 후 미사용시 예약이 자동 취소됩니다. 감사합니다.";
		}

		/*
		 * ##############################
		 * 예약취소 안내(관리자)
		 * ##############################
		 */
		if ("10008".equals(template)) {
			message = (String) param.get("var1") + "고객님, 충전 예약이 관리자에 의해 취소되었습니다 \r\n";
			message += "예약일시 : " + (String) param.get("var2") + "\r\n";
			message += "충전소 : " + (String) param.get("var3");
			message += "충전기 : " + (String) param.get("var4") + "\r\n";
			message += "커넥터 : " + (String) param.get("var5") + "\r\n";
		}

		/*
		 * ##############################
		 * 예약취소 안내(사용자)
		 * ##############################
		 */
		if ("10009".equals(template)) {
			message = (String) param.get("var1") + "고객님, 충전 예약이 취소되었습니다 \r\n";
			message += "예약일시 : " + (String) param.get("var2") + "\r\n";
			message += "충전소 : " + (String) param.get("var3");
			message += "충전기 : " + (String) param.get("var4") + "\r\n";
			message += "커넥터 : " + (String) param.get("var5") + "\r\n";
		}

		/*
		 * ##############################
		 * 예약취소 안내(미사용)
		 * ##############################
		 */
		if ("10010".equals(template)) {
			// LOGGER.info(param);
			message = (String) param.get("var1") + "고객님, 고객님, 충전 예약 후 미사용으로 예약취소되었습니다. \r\n";
			message += "예약일시 : " + (String) param.get("var2") + "\r\n";
			message += "충전소 : " + (String) param.get("var3");
			message += "충전기 : " + (String) param.get("var4") + "\r\n";
			message += "커넥터 : " + (String) param.get("var5") + "\r\n";
		}

		/*
		 * ##############################
		 * 예약사용 안내
		 * ##############################
		 */
		if ("10011".equals(template)) {
			// LOGGER.info(param);
			message = (String) param.get("var1") + "고객님, 고객님, 예약하신 충전시간이 10분 남았습니다. \r\n";
			message += "예약일시 : " + (String) param.get("var2") + "\r\n";
			message += "충전소 : " + (String) param.get("var3");
			message += "충전기 : " + (String) param.get("var4") + "\r\n";
			message += "커넥터 : " + (String) param.get("var5") + "\r\n";
			message += "해당 예약시점 이후 10분 경과되어 충전기를 사용하지 않으실 경우 예약이 자동 취소됩니다.";
		}

		/*
		 * ##############################
		 * 공지사항 안내
		 * ##############################
		 */
		if ("미확정".equals(template)) {
			// LOGGER.info(param);
			message = (String) param.get("var1") + "고객님, 신규 공지사항이 있습니다. \r\n";
			message += (String) param.get("var2") + "\r\n";
			message += "바로가기 : " + (String) param.get("var3");
		}

		// 알림톡 발송 데이터 설정
		resultMap.put("phoneNumber", phone); // 전화번호
		resultMap.put("templateCode", template);
		resultMap.put("message", message);

		logger.info("resultMap =====" + resultMap.toString());

		return resultMap;
	}

}