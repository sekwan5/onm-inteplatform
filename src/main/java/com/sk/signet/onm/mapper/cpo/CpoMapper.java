package com.sk.signet.onm.mapper.cpo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
@SuppressWarnings("rawtypes")
public interface CpoMapper {
    // CPO 리스트 조회(그리드)
    List<Map<String, String>> selectCpoList(Map param, RowBounds rowBounds);

    // CPO 리스트 목록 total 갯수
    int selectCpoListCount(Map param);

    // CPO 등록
    int insertCpo(Map param);

    // CPO 상세보기
    Map<String, Object> selectCpo(Map param);

    // CPO 수정
    int updateCpo(Map param);

    // 마스킹제거요청
    Map<String, Object> removeMasking(Map param);

    // 마스킹제거이력 추적
    Map<String, Object> removeMaskingHist(Map param);

}
