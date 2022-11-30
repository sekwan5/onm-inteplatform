package com.sk.signet.onm.mapper.code;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import com.sk.signet.onm.mapper.auth.LoginMapper;
import com.sk.signet.onm.mapper.cpo.CpoMapper;

@MapperScan
@SuppressWarnings("rawtypes")
public interface CodeMapper {

	//공통코드관리(대분류)
	List<Map<String, String>> selectCommonMainCodeList(Map param , RowBounds rowbounds);
	int selectCommonMainCodeListCount(Map param);
	int insertCommonMainCode(Map param);
	int updateCommonMainCode(Map param);
	
	
	//공통코드관리(소분류)
	List<Map<String, String>> selectCommonChildCodeList(Map param , RowBounds rowbounds);
	int selectCommonChildCodeListCount(Map param);
	int insertCommonChildCode(Map param);
	int updateCommonChildCode(Map param);
	
//	void insertLoginHist(Map param);
//
//	void updateLogoutHist(Map param);
	
}

