package com.sk.signet.onm.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sk.signet.onm.common.grid.GridResultVO;
import com.sk.signet.onm.mapper.MessageMapper;

@Service("messageService")
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class MessageService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    @Qualifier("propertyService")
    private Properties properties;

    /********************************************************************************************************************
     * 메시지 관리
     ******************************************************************************************************************
     * /**
     * 메시지 관리 목록 조회
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public GridResultVO selectMessageList(Map param) throws Exception {
        GridResultVO result = new GridResultVO();
        if (param.get("page") != null && param.get("rows") != null) {
            result.setPage(Integer.parseInt((String) param.get("page")));
            result.setRowPerPage(Integer.parseInt((String) param.get("rows")));
        }
        result.setRecords(messageMapper.selectMessageListCount(param)); // Total Count

        // 조회
        RowBounds rowBounds = new RowBounds((result.getPage() - 1) * result.getRowPerPage(), result.getRowPerPage());
        result.setRows(messageMapper.selectMessageList(param, rowBounds)); // Data
        return result;
    }

    public List<Map<String, String>> selectUserMessageList(Map paramMap) throws Exception {
        return messageMapper.selectUserMessageList(paramMap);
    }

    public Map<String, String> selectMessageDetail(Map param) throws Exception {
        return messageMapper.selectMessageDetail(param);
    }

    public void insertMessage(Map param) throws Exception {
        messageMapper.insertMessage(param);
    }

    public void updateMessage(Map param) throws Exception {
        messageMapper.updateMessage(param);
    }

    /********************************************************************************************************************
     * 사용자 메시지
     ********************************************************************************************************************/
    public void insertUserMessage(Map param) throws Exception {
        LOGGER.info("########## insertUserMessage param=========>" + param);
        messageMapper.insertUserMessage(param);
    }

    public void checkUserMessage(Map param) throws Exception {
        messageMapper.checkUserMessage(param);
    }

    /********************************************************************************************************************
     * 알림톡 잔송
     ********************************************************************************************************************/
    public Map<String, String> selectSmsTemplate(String strMsgId) {
        Map templateParamMap = new HashMap();
        templateParamMap.put("msgId", strMsgId);
        templateParamMap.put("msgTypeCd", "ta");

        return messageMapper.selectMessageTemplate(templateParamMap);
    }

    public int sendSmsFromTemplate(Map<String, Object> paramMap) {
        String strUsercode = properties.getProperty("api.sms.usercode");
        String strYellowidkey = properties.getProperty("api.sms.yellowidkey");
        String strSendername = properties.getProperty("api.sms.sender.name");
        String strSenderNumber = properties.getProperty("api.sms.sender.number");

        List<Map<String, String>> sendList = (List<Map<String, String>>) paramMap.get("list");

        int succCnt = 0;
        for (Map<String, String> sendMap : sendList) {
            String msgId = sendMap.get("msgId");
            String msgTypeCd = "AT";

            Map replacedMap = getReplacedTemplate(msgId, msgTypeCd, sendMap);
            replacedMap.putAll(sendMap);
            replacedMap.put("msgFk", paramMap.get("sessionUserId"));

            // PROPERTY값 INSERT
            replacedMap.put("usercode", strUsercode);
            replacedMap.put("yellowidkey", strYellowidkey);
            replacedMap.put("sendername", strSendername);
            replacedMap.put("sendernumber", strSenderNumber);

            succCnt += messageMapper.insertSms(replacedMap);
        }
        return succCnt;
    }

    /**
     * 템플릿 조회 후 replaceDataMap의 데이터로 템플릿 내용을 대치한다.
     * 
     * @param strMsgId
     * @param strMsgTypeCd
     * @param replaceDataMap
     * @return title, msg, resendCd, resendMsg, memo
     */
    private Map<String, String> getReplacedTemplate(String strMsgId, String strMsgTypeCd,
            Map<String, String> replaceDataMap) {
        Map templateParamMap = new HashMap();
        templateParamMap.put("msgId", strMsgId);
        templateParamMap.put("msgTypeCd", strMsgTypeCd);
        Map<String, String> templateMap = messageMapper.selectMessageTemplate(templateParamMap);

        // REPLACE
        if (templateMap != null) {
            String strTitle = templateMap.get("title");
            String strMsg = templateMap.get("msg");
            String strResendMsg = templateMap.get("resendMsg");

            for (Map.Entry<String, String> entry : replaceDataMap.entrySet()) {
                strTitle = strTitle.replace("#{" + entry.getKey() + "}", entry.getValue());
                strMsg = strMsg.replace("#{" + entry.getKey() + "}", entry.getValue());
                strResendMsg = strResendMsg.replace("#{" + entry.getKey() + "}", entry.getValue());
            }

            templateMap.put("title", strTitle);
            templateMap.put("msg", strMsg);
            templateMap.put("resendMsg", strResendMsg);

        }

        return templateMap;
    }

    public List<Map<String, String>> selectMailLogList(Map<String, String> paramMap) throws Exception {
        return messageMapper.selectMailLogList(paramMap);
    }

    // 메일발송 전 로그부터 쌓는다
    public int insertSendMailLog(Map<String, String> paramMap) throws Exception {
        String strMsgId = paramMap.get("msgId");
        String strMsgTypeCd = paramMap.get("msgTypeCd");
        String strMsgFk = paramMap.get("msgFk");
        String strTo = paramMap.get("to");

        if (isNull(strMsgId) || isNull(strMsgTypeCd) || isNull(strMsgFk) || isNull(strTo)) {
            // throw new JsonMessageException("필수 정보가 존재하지 않아 메일 발송이 실패하였습니다.");
        }

        return messageMapper.insertMailLog(paramMap);
    }

    public int sendMail(Map<String, String> paramMap) throws Exception {
        String strMsgId = paramMap.get("msgId");
        String strMsgTypeCd = paramMap.get("msgTypeCd");
        String strMsgFk = paramMap.get("msgFk");
        String strTo = paramMap.get("to");

        /*-----------------------------------------------
         * 맵핑 데이터 GET
         *-----------------------------------------------*/
        Map<String, String> dataParamMap = new HashMap<String, String>();
        Map<String, String> dataMap = null;
        // 서비스 내역서
        if ("MAIL_001".equals(strMsgId)) {
            // 템플릿 데이터 조회
            dataParamMap.put("acceptNo", strMsgFk);
            // dataMap = repairMapper.selectRepairDetail(dataParamMap);
            // REPLACE Template
            Map<String, String> replacedMap = getReplacedTemplate(strMsgId, strMsgTypeCd, dataMap);

            // 서비스내역서 첨부 조회
            dataParamMap.put("reportTypeCd", "RPT01");
            dataParamMap.put("statusCd", "RSS"); // 서명 상태
            String strFilePath = messageMapper.selectStatementOfServiceFile(dataParamMap);

            // 발송
            /*
             * int succ = mailMail.sendAttachMail(strTo , replacedMap.get("title") ,
             * replacedMap.get("msg") , strFilePath);
             */

            return messageMapper.updateMailLog(paramMap);
        }
        return 0;
    }

    /**
     * 
     * @param Map                paramMap
     * @param paramMap.msgId     : MSG_ID - 필수
     * @param paramMap.msgTypeCd : MSG_TYPE_CD - 필수
     * @param paramMap.msgFk     - MSG_FK - - 필수
     * @throws Exception
     */
    public int sendSms(Map<String, String> paramMap) throws Exception {
        String strMsgId = paramMap.get("msgId");
        String strMsgTypeCd = paramMap.get("msgTypeCd");
        String strMsgFk = paramMap.get("msgFk");

        String strUsercode = properties.getProperty("api.sms.usercode");
        String strYellowidkey = properties.getProperty("api.sms.yellowidkey");
        String strSendername = properties.getProperty("api.sms.sender.name");
        String strSenderNumber = properties.getProperty("api.sms.sender.number");

        /*-----------------------------------------------
         * 맵핑 데이터 GET
         *-----------------------------------------------*/
        Map<String, String> dataParamMap = new HashMap<String, String>();
        Map<String, String> dataMap = null;
        // 접수완료, 고객호출
        if ("ANTZ_012".equals(strMsgId) || "ANTZ_006".equals(strMsgId)) {
            // 데이터 조회
            String[] arrMsgFk = strMsgFk.split("_");
            dataParamMap.put("deptCd", arrMsgFk[0]);
            dataParamMap.put("regYmd", arrMsgFk[1]);
            dataParamMap.put("regSeq", arrMsgFk[2]);
            // dataMap = repairMapper.selectReceiptDetailForSms(dataParamMap);

            // 상세정보에서 수신자명, 번호를 PARAMETER에 세팅
            paramMap.put("toNumber", dataMap.get("smsToNumber"));
            paramMap.put("toName", dataMap.get("smsToName"));

            // 제품배송예정, 고객인도 완료, 배송알림, 도착완료
        } else if ("ANTZ_002".equals(strMsgId) || "ANTZ_007".equals(strMsgId) || "ANTZ_008".equals(strMsgId)
                || "ANTZ_009".equals(strMsgId) || "ANTZ_011".equals(strMsgId)) {
            // 데이터 조회
            dataParamMap.put("acceptNo", strMsgFk);
            // dataMap = repairMapper.selectRepairDetail(dataParamMap);

            // 상세정보에서 수신자명, 번호를 PARAMETER에 세팅
            paramMap.put("toNumber", dataMap.get("smsToNumber"));
            paramMap.put("toName", dataMap.get("smsToName"));
        }
        // ANTZ_011

        if (isNull(strMsgId) || isNull(strMsgTypeCd) || isNull(strMsgFk) || isNull(paramMap.get("toNumber"))
                || isNull(paramMap.get("toName"))) {
            LOGGER.error("Failed to insert SMS Table :::: Empty Parameter Value(s) ");
            // throw new JsonMessageException("필수 정보가 존재하지 않아 SMS 발송이 실패하였습니다.");
        }

        // REPLACE Template
        Map replacedMap = getReplacedTemplate(strMsgId, strMsgTypeCd, dataMap);
        paramMap.putAll(replacedMap);

        // PROPERTY값 INSERT
        paramMap.put("usercode", strUsercode);
        paramMap.put("yellowidkey", strYellowidkey);
        paramMap.put("sendername", strSendername);
        paramMap.put("sendernumber", strSenderNumber);

        return messageMapper.insertSms(paramMap);
    }

    private boolean isNull(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    public int updateUserMessageCheck(Map param) throws Exception {
        return messageMapper.updateUserMessageCheck(param);
    }

    public int insertNotiTalkSendLog(Map param) throws Exception {
        LOGGER.info("알림톡 param ===== " + param.toString());
        return messageMapper.insertNotiTalkSendLog(param);
    }

}
