package com.sk.signet.onm.web.service;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.signet.onm.common.grid.GridResultVO;
import com.sk.signet.onm.mapper.auth.LoginMapper;
import com.sk.signet.onm.mapper.code.CodeMapper;
import com.sk.signet.onm.mapper.cpo.CpoMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class CodeService {

    @Autowired
    private CodeMapper codeMapper;

    
    public GridResultVO selectCommonMainCodeList(Map<String, Object> param) {

        GridResultVO result = new GridResultVO();

        if (param.get("page") != null && param.get("rows") != null) {
            result.setPage(Integer.parseInt(String.valueOf( param.get("page"))));
            result.setRowPerPage(Integer.parseInt(String.valueOf( param.get("rows"))));
        }
        result.setRecords(codeMapper.selectCommonMainCodeListCount(param)); // 총row 갯수
        /// 페이징 계산 set
        RowBounds rowBounds = new RowBounds((result.getPage() - 1) * result.getRowPerPage(), result.getRowPerPage());
        result.setRows(codeMapper.selectCommonMainCodeList(param, rowBounds)); // 그리드에 뿌려질 data

        return result;
    }
    
    public GridResultVO selectCommonChildCodeList(Map<String, Object> param) {

        GridResultVO result = new GridResultVO();

        if (param.get("page") != null && param.get("rows") != null) {
            result.setPage(Integer.parseInt(String.valueOf( param.get("page"))));
            result.setRowPerPage(Integer.parseInt(String.valueOf( param.get("rows"))));
        }
        result.setRecords(codeMapper.selectCommonChildCodeListCount(param)); // 총row 갯수
        /// 페이징 계산 set
        RowBounds rowBounds = new RowBounds((result.getPage() - 1) * result.getRowPerPage(), result.getRowPerPage());
        result.setRows(codeMapper.selectCommonChildCodeList(param, rowBounds)); // 그리드에 뿌려질 data

        return result;
    }    

}
