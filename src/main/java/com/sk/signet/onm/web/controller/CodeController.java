package com.sk.signet.onm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.signet.onm.web.service.CodeService;

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

}
