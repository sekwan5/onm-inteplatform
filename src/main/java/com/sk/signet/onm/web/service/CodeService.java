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
    
    @Transactional
    public String insertCommonMainCode(Map<String, Object> param) {
    	int result = 0;
    	result = codeMapper.insertCommonMainCode(param);
    	return (result == 0 ? "FALIL":"SUCCESS");
    }
    
    @Transactional
    public String insertCommonChildCode(Map<String, Object> param) {
    	int result = 0;
    	result = codeMapper.insertCommonChildCode(param);
    	return (result == 0 ? "FALIL":"SUCCESS");
    }

    
    @Transactional
    public String updateCommonMainCode(Map<String, Object> param) {
    	int result = 0;

    	result = codeMapper.updateCommonMainCode(param);

    	/* 2022.12.01 -> 기획에 의논후 결정
    	if(result > 0) {
        	//하위코드의 mainCode를 변경
    		result = codeMapper.othUpdateCommonChildCode(param);
    	}*/
    	   	
    	return (result == 0 ? "FALIL":"SUCCESS");
    }
    
    @Transactional
    public String updateCommonChildCode(Map<String, Object> param) {
    	int result = 0;
    	result = codeMapper.updateCommonChildCode(param);
    	return (result == 0 ? "FALIL":"SUCCESS");
    }
}
