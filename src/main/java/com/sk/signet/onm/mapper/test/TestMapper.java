package com.sk.signet.onm.mapper.test;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

@MapperScan
@SuppressWarnings("rawtypes")
public interface TestMapper {
	
	/**
	 * 테스트 충전기 상태 리스트 조회
	 */
	List<Map<String, Object>> selecTestList();

}
