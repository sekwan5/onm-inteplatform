package com.sk.signet.onm.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
@SuppressWarnings("rawtypes")
public interface MessageMapper {

    Map<String, String> selectMessageTemplate(Map param);

    List<Map<String, String>> selectSmsLogList(Map param);

    List<Map<String, String>> selectMailLogList(Map param);

    int insertMailLog(Map param);

    int updateMailLog(Map param);

    String selectStatementOfServiceFile(Map param);

    int insertSms(Map param);

    // 메시지 관리
    int selectMessageListCount(Map param);

    List<Map<String, String>> selectMessageList(Map param, RowBounds rowBounds);

    Map<String, String> selectMessageDetail(Map param);

    int insertMessage(Map param);

    int updateMessage(Map param);

    // 사용자 알림 메시지
    List<Map<String, String>> selectUserMessageList(Map param, RowBounds rowBounds);

    List<Map<String, String>> selectUserMessageList(Map paramMap);

    int insertUserMessage(Map param);

    int checkUserMessage(Map param);

    int updateUserMessageCheck(Map param);

    int insertNotiTalkSendLog(Map param);

    Map<String, Object> selectReserveParam(Map param);

    Map<String, Object> selectReserveName(Map param);

    int selectNoticeTalkSeq(Map param);

    String selectSystemGbn(Map param);
}
