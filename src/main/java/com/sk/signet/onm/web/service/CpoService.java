package com.sk.signet.onm.web.service;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.signet.onm.common.grid.GridResultVO;
import com.sk.signet.onm.mapper.auth.LoginMapper;
import com.sk.signet.onm.mapper.cpo.CpoMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class CpoService {

    @Autowired
    private CpoMapper cpoMapper;

    @Autowired
    private LoginMapper loginMapper;

    //
    public GridResultVO selectCpoList(Map<String, Object> param) {

        GridResultVO result = new GridResultVO();

        if (param.get("page") != null && param.get("rows") != null) {
            result.setPage(Integer.parseInt(String.valueOf(param.get("page")) ));
            result.setRowPerPage(Integer.parseInt(String.valueOf( param.get("rows"))));
        }

        result.setRecords(cpoMapper.selectCpoListCount(param)); // 총row 갯수

        /// 페이징 계산 set
        RowBounds rowBounds = new RowBounds((result.getPage() - 1) * result.getRowPerPage(), result.getRowPerPage());

        result.setRows(cpoMapper.selectCpoList(param, rowBounds)); // 그리드에 뿌려질 data

        return result;
    }

    // cpo등록
    public int insertCpo(Map<String, Object> param) {

        int result = cpoMapper.insertCpo(param);
        log.debug("insert result : " + result);

        return result;
    }

    // cpo 상세보기
    public Map<String, Object> selectCpo(Map<String, Object> param) {

        Map<String, Object> result = cpoMapper.selectCpo(param);
        log.debug("select result : " + result);

        return result;
    }

    // CPO 수정
    public int updateCpo(Map<String, Object> param) {

        int result = cpoMapper.updateCpo(param);
        log.debug("insert result : " + result);

        return result;
    }

    // 공통코드 수정 확인
    public Map<String, Object> applyUpdateCode(Map param) {

        Map<String, Object> result = cpoMapper.applyUpdateCode(param);

        return result;

    }

    //
    @Transactional
    public void removeMaskingHist(Map param) throws Exception {

        loginMapper.insertLoginHist(param);
    }

}
