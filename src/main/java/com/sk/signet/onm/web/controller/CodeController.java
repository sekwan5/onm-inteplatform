package com.sk.signet.onm.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.signet.onm.common.grid.GridResultVO;
import com.sk.signet.onm.web.service.CodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "공통코드 관리", description = "공통코드 API입니다.")
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
@Slf4j
public class CodeController {

    @Autowired
    private CodeService codeService;
    
    
    @PostMapping("/selectCommonMainCode")
    @Operation(summary = "공통코드 관리(그리드)", description = "공통코드 관리(대분류) 리스트")
    public ResponseEntity<Map<String, Object>> selectCommonMainCode(@RequestBody Map<String, Object> param) {
    	System.out.println(param);
        /**
         * pagination 데이터
         * {
         * "page":1,
         * "rows":10
         *  ~~ 검색조건
         * }
         */

        GridResultVO result = codeService.selectCommonMainCodeList(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);

        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }
    
    @PostMapping("/selectCommonChildCode")
    @Operation(summary = "공통코드 관리(그리드)", description = "공통코드 관리(소분류) 리스트")
    public ResponseEntity<Map<String, Object>> selectCommonChildCode(@RequestBody Map<String, Object> param) {
        /**
         * pagination 데이터
         * {
         * "page":1,
         * "rows":10
         *  ~~ 검색조건
         * }
         */
        GridResultVO result = codeService.selectCommonChildCodeList(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);
        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }    
    
    @PostMapping("/insertCommonMainCode")
    @Operation(summary = "공통코드 관리(그리드)", description = "공통코드 관리(대분류) 등록")
    public ResponseEntity<Map<String, Object>> insertCommonMainCode(@RequestBody Map<String, Object> param) {
    	
        String result = codeService.insertCommonMainCode(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);
        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }           
    
    @PostMapping("/insertCommonChildCode")
    @Operation(summary = "공통코드 관리(그리드)", description = "공통코드 관리(소분류) 등록")
    public ResponseEntity<Map<String, Object>> insetCommonChildCode(@RequestBody Map<String, Object> param) {
        String result = codeService.insertCommonChildCode(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);
        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }        
    
    
    @PostMapping("/updateCommonMainCode")
    @Operation(summary = "공통코드 관리(그리드)", description = "공통코드 관리(대분류) 수정")
    public ResponseEntity<Map<String, Object>> updateCommonMainCode(@RequestBody Map<String, Object> param) {
        String result = codeService.updateCommonMainCode(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);
        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }     
    
    @PostMapping("/updateCommonChildCode")
    @Operation(summary = "공통코드 관리(그리드)", description = "공통코드 관리(소분류) 수정")
    public ResponseEntity<Map<String, Object>> updateCommonChildCode(@RequestBody Map<String, Object> param) {
        String result = codeService.updateCommonChildCode(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);
        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }      
    
    
    
     

}
