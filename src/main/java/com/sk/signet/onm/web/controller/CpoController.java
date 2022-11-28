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
import com.sk.signet.onm.web.service.CpoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "플랫폼 고객사(CPO) 관리", description = "CPO API입니다.")
@RestController
@RequestMapping("/cpo")
@RequiredArgsConstructor
@Slf4j
public class CpoController {

    @Autowired
    private CpoService cpoService;

    @PostMapping("/selectCpoList")
    @Operation(summary = "플랫폼 고객사(CPO) 관리(그리드)", description = "플랫폼 고객사(CPO) 리스트")
    public ResponseEntity<Map<String, Object>> selectCpoList(@RequestBody Map<String, Object> param) {

        // System.out.println(param);
        /**
         * pagination 데이터
         * {
         * "page":1,
         * "rows":10
         * }
         */

        GridResultVO result = cpoService.selectCpoList(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);

        log.debug("data:" + data);

        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }

    @PostMapping("/insertCpo")
    @Operation(summary = "플랫폼 고객사(CPO) 등록 ", description = "플랫폼 고객사(CPO) 등록")
    public ResponseEntity<Map<String, Object>> insertCpo(@RequestBody Map<String, Object> param) {

        int result = cpoService.insertCpo(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);

        log.debug("data:" + data);
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }

    @PostMapping("/selectCpo")
    @Operation(summary = "플랫폼 고객사(CPO) 상세보기 ", description = "플랫폼 고객사(CPO) 상세보기")
    public ResponseEntity<Map<String, Object>> selectCpo(@RequestBody Map<String, Object> param) {

        Map<String, Object> result = cpoService.selectCpo(param);
        Map<String, Object> data = new HashMap<>();
        data.put("data", result);

        log.debug("data:" + data);
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }

    @PostMapping("/updateCpo")
    @Operation(summary = "플랫폼 고객사(CPO) 수정 ", description = "플랫폼 고객사(CPO) 수정")
    public ResponseEntity<Map<String, Object>> updateCpo(@RequestBody Map<String, Object> param) {

        Map<String, Object> apply = cpoService.applyUpdateCode(param);

        Map<String, Object> data = new HashMap<>();
        if (apply != null) {

            int result = cpoService.updateCpo(param);
            data.put("data", result);
        } else {
            data.put("data", "fail");
        }

        log.debug("data:" + data);
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }

    @PostMapping("/apllyUpdateCode")
    @Operation(summary = "공통코드 수정 확인 ", description = "공통코드 수정 확인")
    public ResponseEntity<Map<String, Object>> apllyUpdateCode(@RequestBody Map<String, Object> param) {

        Map<String, Object> result = cpoService.applyUpdateCode(param);

        Map<String, Object> data = new HashMap<>();

        if (result != null) {

            // cpoService.removeMaskingHist(result);

            data.put("data", "success");
        } else {
            data.put("data", "fail");
        }

        log.debug("data:" + data);
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
    }

}
