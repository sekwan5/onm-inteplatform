package com.sk.signet.onm.common.excel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.signet.onm.common.exception.ExcelImportException;
import com.sk.signet.onm.common.grid.GridResultVO;
import com.sk.signet.onm.common.utils.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 수정예정
 */
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
@Slf4j
public class ExcelController {

    private final int excelDwonLoopCnt = 20000; // 엑셀다운로드시 한번에 조회가능한 건수

    @Resource(name = "excelService")
    private ExcelService excelService;

    @Autowired
    private MessageUtil messageUtil;

    /**
     * 엑셀 다운로드
     *
     * @param paramMap
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/excelDownload")
    public void downloadExcel(@RequestParam Map<String, Object> paramMap, HttpServletResponse res,
            HttpServletRequest req) throws Exception {

        log.info("########## downloadExcel ########## paramMap : " + paramMap);

        // Cookie setFileDown = new Cookie("fileDownYn", "N");
        // res.addCookie(setFileDown);

        String paramExcelHeader = (String) paramMap.get("excelHeader");
        String paramExcelKey = (String) paramMap.get("excelKey");
        String paramExcelWidth = (String) paramMap.get("excelWidth");
        String pageId = (String) paramMap.get("pageId");
        log.info("########## downloadExcel pageId : " + pageId);
        // paramMap.put("languageCd", sessionVO.getLanguageCd());

        int girdWithChg = 35; // 엑셀 with계산시 곱할 수

        String[] headerList = paramExcelHeader.toString().split(",");
        String[] keyList = paramExcelKey.toString().split(",");
        String[] widthList = null;

        String excelDataType = (String) paramMap.get("excelDataType");
        String[] excelDataTypeArr = excelDataType.split(",");

        if (paramExcelWidth != null) {
            widthList = paramExcelWidth.toString().split(",");
        }

        List<Map<String, String>> excelList = null;

        int row = 3;
        // paramMap.put("authUnit", sessionVO.getAuthUnit());
        // paramMap.put("companyId", sessionVO.getCompanyId());
        // paramMap.put("userId", sessionVO.getUserId());
        // paramMap.put("authId", sessionVO.getAuthId());
        // paramMap.put("supperAdminYn", sessionVO.getSupperAdminYn());
        // paramMap.put("excelGb", "excel");
        // if ("Y".equals(sessionVO.getSupperAdminYn())) {
        // paramMap.put("searchYn", "Y");
        // } else {
        // paramMap.put("searchYn", "N");
        // }
        GridResultVO gridList = null;

        if ("serviceCompany".equals(pageId)) { // 서비스사업자관리
            // gridList = serviceService.selectServiceCompanyList(paramMap);
            excelList = gridList.getRows();

        }

        log.info("############## excelList.size() : " + excelList.size());

        if ("logMessageList".equals(pageId) ? gridList.getRecords() > 0 : excelList.size() > 0) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("List");

            // 제목 폰트
            XSSFFont nameFont = workbook.createFont();
            nameFont.setBold(true);
            nameFont.setFontHeightInPoints((short) 16);

            // 제목 스타일
            CellStyle nameStyle = workbook.createCellStyle();
            nameStyle.setAlignment(HorizontalAlignment.CENTER);
            nameStyle.setFont(nameFont);

            // 데이터 헤더 스타일
            XSSFCellStyle headerStyle = setHeaderStyle(workbook);

            // 데이터 스타일
            XSSFCellStyle bodyStyle = setBodyStyle(workbook);

            // 데이터 입력
            int idxCreateRow = 0;
            int maxCol = 0;
            int maxColTitle = 0;

            // 헤더그리기
            Row bodyRow = null;
            idxCreateRow = row;
            bodyRow = sheet.createRow(idxCreateRow);

            // 엑셀의 헤더 생성 분기처리
            // setHeight 헤더의 높이 지정, setWrapText 헤더의 문자 개행처리 유무 \n 으로 개행함.
            if ("chargeHistKepcoPayType".equals(pageId)) {
                bodyRow.setHeight((short) 700);
                headerStyle.setWrapText(true);
            }
            // 1st 헤더를 그리자
            for (int i = 0; i < headerList.length; i++) {
                Cell cell = null;
                int clounmWidth = 200;
                clounmWidth = Integer.parseInt(widthList[i]) * girdWithChg;
                sheet.setColumnWidth(i, clounmWidth); // 컬럼 width 조정
                cell = bodyRow.createCell(i);
                cell.setCellValue(headerList[i]);
                cell.setCellStyle(headerStyle);

            }

            row++;

            int col = 0;

            // 해당데이타가 int형인지 string 배열값이 해당 열과 길이가 같은지.
            Boolean excelTp = keyList.length == excelDataTypeArr.length ? true : false;

            if (!"logMessageList".equals(pageId)) {
                for (int i = 0; i < excelList.size(); i++) {
                    col = 0;

                    Row bodyRow1 = sheet.createRow(row + i);
                    Map<String, String> excelData = excelList.get(i);

                    for (int j = 0; j < keyList.length; j++) {

                        Cell cell = bodyRow1.createCell(col++);
                        cell.setCellStyle(bodyStyle);

                        String excelVal = excelData.get(keyList[j]);
                        if (excelTp && "int".equals(excelDataTypeArr[j])) {
                            cell.setCellValue(NumberUtils.toDouble(excelVal));
                        } else {
                            cell.setCellValue(excelVal);
                        }

                    }

                    if (maxCol < col) {
                        maxCol = col;
                    }

                }

            } else { // 이력정보관리 > 통신이력
                int totalCount = gridList.getRecords();
                int pageCnt = 0;
                int rowCnt = row;
                for (int id = 0; id < totalCount; id += excelDwonLoopCnt) {

                    pageCnt++;
                    // System.out.println("id ::: "+id);
                    // System.out.println("pageCnt ::: "+pageCnt);
                    rowCnt = row + id;
                    paramMap.put("page", pageCnt + "");
                    paramMap.put("rows", excelDwonLoopCnt + "");
                    // gridList = communicationService.selectLogMessageList(paramMap);
                    excelList = gridList.getRows();
                    for (int xx = 0; xx < excelList.size(); xx++) {
                        col = 0;
                        Row bodyRow1 = sheet.createRow(rowCnt + xx);
                        Map<String, String> excelData = excelList.get(xx);

                        for (int j = 0; j < keyList.length; j++) {

                            Cell cell = bodyRow1.createCell(col++);
                            cell.setCellStyle(bodyStyle);

                            String excelVal = excelData.get(keyList[j]);
                            if (excelTp && "int".equals(excelDataTypeArr[j])) {
                                cell.setCellValue(NumberUtils.toDouble(excelVal));
                            } else {
                                cell.setCellValue(excelVal);
                            }

                        }

                        if (maxCol < col) {
                            maxCol = col;
                        }
                    }
                }
            }

            maxColTitle = maxCol;

            String excelNm = (String) paramMap.get("excelNm");

            // 제목 MERGE
            sheet.addMergedRegion(new CellRangeAddress(1, // 시작 행번호
                    1, // 마지막 행번호
                    0, // 시작 열번호
                    maxColTitle - 1 // 마지막 열번호
            ));

            Row nameRow = sheet.createRow(1);
            Cell nameCell = nameRow.createCell(0);
            nameCell.setCellValue(excelNm);
            nameCell.setCellStyle(nameStyle);

            // 시간 표기
            Row dateRow = sheet.createRow(2);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            // 다운로드
            download(workbook, res, req, excelNm);

        } else {
            res.setContentType("text/html;charset:UTF-8");
            res.setCharacterEncoding("UTF-8");
            res.setHeader("Cache-Control", "no-cache");

            String vMessage = messageUtil.getMessage("common.message.failToDownloadExcel"); // 엑셀 다운로드할 데이터가 없습니다.
            log.info("############## vMessage : " + vMessage);// document.getElementById('asxLoading').hide()

            throw new ExcelImportException(
                    "successMsg " + messageUtil.getMessage("excelUtil.message.incorrectColumnCnt"));
            // res.getWriter().print("<script>alert('데이터가 조회되지 않아 다운로드 할 수
            // 없습니다.');</script>");asxLoading
        }
    }

    @RequestMapping(value = "/upload.json")
    public @ResponseBody Map upload(@RequestParam("file") MultipartFile mf, @RequestParam Map<String, String> paramMap,
            HttpServletResponse res) throws Exception {
        System.out.println("upload mf = " + mf);
        System.out.println("upload paramMap = " + paramMap);
        log.info("########## Excel Upload ##########");

        // 대표 메세지
        String strRequired = messageUtil.getMessage("common.message.required"); // {0}은(는) 필수사항입니다.
        String strModify = messageUtil.getMessage("common.message.modifyForUpload"); // 수정 후 다시 업로드 진행해 주세요.

        Map<String, String> rtnMap = new HashMap<String, String>();
        Map<String, Object> optionParamMap = new HashMap<String, Object>();

        // 컬럼 가져오기
        String excelType = paramMap.get("excelType");
        String excelPopNm = paramMap.get("excelPopNm");
        String optionParam = paramMap.get("optionParam");

        ObjectMapper mapper = new ObjectMapper();
        // ptionParamMap = mapper.readValue(optionParam, new TypeReference<Map<String,
        // String>>() {});

        String optionDeptCd = (String) optionParamMap.get("deptCd");
        String optionStdYmd = (String) optionParamMap.get("stdYmd");

        String optionPopUpGroupCd = (String) optionParamMap.get("popUploadGroupCd");
        String optionPopUploadTypeCd = (String) optionParamMap.get("popUploadTypeCd");
        String optionPopUploadTypeNm = (String) optionParamMap.get("popUploadTypeNm");

        log.info("########## optionParam     : " + optionParam);
        log.info("########## optionParamMap  : " + optionParamMap.toString());
        log.info("########## option DeptCd   : " + optionDeptCd);
        log.info("########## option StdYmd   : " + optionStdYmd);
        log.info("########## option popUploadGroupCd : " + optionPopUpGroupCd);
        log.info("########## option popUploadTypeCd  : " + optionPopUploadTypeCd);
        log.info("########## option popUploadTypeNm  : " + optionPopUploadTypeNm);

        Map<String, String> codeParamMap = new HashMap<String, String>();

        // String companyId = sessionVO.getCompanyId();
        // String languageCd = sessionVO.getLanguageCd();
        // String timeDiff = sessionVO.getTimeDiff();

        // codeParamMap.put("companyId", companyId);
        // codeParamMap.put("languageCd", languageCd);

        if ("fileUploadList".equals(excelPopNm) && (excelType == null || "".equals(excelType))) {

            codeParamMap.put("groupCd", optionPopUpGroupCd);
            codeParamMap.put("cd", optionPopUploadTypeCd);

            // List<Map<String, String>> codeExcelTypeList =
            // codeService.selectCodeListInUse(codeParamMap);
            // excelType = codeExcelTypeList.get(0).get("coRefVal1");
        }

        log.info("########## excelType  : " + excelType);

        codeParamMap.put("groupCd", excelType);
        codeParamMap.remove("cd");

        List<Map<String, String>> codeColList = null;
        List<Map<String, String>> codeColRawList = null;
        List<Map<String, String>> codeColModList = null;

        if ("EXCELUP08".equals(excelType)) {
            codeParamMap.put("coRefVal2", "RAW");
            // codeColRawList = codeService.selectCodeListInUse(codeParamMap);
            codeColList = codeColRawList;

            codeParamMap.remove("coRefVal2");
            codeParamMap.put("coRefVal3", "MOD");
            // = codeService.selectCodeListInUse(codeParamMap);
        } else {
            // codeColList = codeService.selectCodeListInUse(codeParamMap);
        }

        Workbook workbook = null;

        try {
            workbook = new HSSFWorkbook(mf.getInputStream());
        } catch (OfficeXmlFileException xlsFormatEx) {
            workbook = new XSSFWorkbook(mf.getInputStream());
        }

        int totalSheetCnt = 1;
        int totalAreaCnt = 1;

        // 무상송장
        if ("EXCELUP08".equals(excelType)) {
            totalSheetCnt = workbook.getNumberOfSheets();
            totalAreaCnt = 2;
        }

        for (int sheetIndex = 0; sheetIndex < totalSheetCnt; sheetIndex++) {

            Sheet sheet = workbook.getSheetAt(0);
            String sheetName = ""; // sheet 이름

            if ("EXCELUP05".equals(excelType)) {
                sheet = workbook.getSheetAt(1);
            } else if ("EXCELUP08".equals(excelType)) {
                sheet = workbook.getSheetAt(sheetIndex);
                sheetName = workbook.getSheetName(sheetIndex); // sheet 이름
            }

            int headerDataRow = 0;
            int firstDataRow = 0;

            // areaIndex = 1 인 경우 사용
            int headerDataRow2 = 0;
            int firstDataRow2 = 0;

            for (int areaIndex = 0; areaIndex < totalAreaCnt; areaIndex++) {

                log.info("########## totalSheetCnt  : " + totalSheetCnt + " // sheetIndex : " + sheetIndex
                        + " // areaIndex : " + areaIndex);

                if (areaIndex == 0) {

                    if ("EXCELUP07".equals(excelType)) {
                        headerDataRow = sheet.getFirstRowNum() + 3;
                    } else if ("EXCELUP08".equals(excelType)) {
                        headerDataRow = sheet.getFirstRowNum() + 5;
                    } else {
                        headerDataRow = 0;
                    }

                } else {
                    if ("EXCELUP08".equals(excelType)) {
                        headerDataRow = headerDataRow2;
                    }
                }

                firstDataRow = headerDataRow + 1;
                int lastRow = sheet.getLastRowNum();

                // 엑셀 헤더와 코드목록의 코드값이 일치하면 코드Map에 CELL NUM을 추가.
                // Row headerRow = sheet.getRow(sheet.getFirstRowNum());
                Row headerRow = sheet.getRow(headerDataRow);

                String STD_YMD_COL = "";
                String STD_YMD_VAL = "";
                String DEPT_CD_COL = "";
                String DEPT_CD_VAL = "";

                List<String> listSQLHeaderCd1 = new ArrayList<String>();
                List<String> listSQLHeaderNm1 = new ArrayList<String>();
                List<String> listSQLHeaderCd2 = new ArrayList<String>();
                List<String> listSQLHeaderNm2 = new ArrayList<String>();
                List<String> listSQLRefVal2 = new ArrayList<String>();
                List<String> listSQLRefVal3 = new ArrayList<String>();
                List<String> listSQLRefVal6 = new ArrayList<String>();
                List<String> listSQLNn = new ArrayList<String>();
                List<String> listSQLYmd = new ArrayList<String>();
                List<String> listSQLInt = new ArrayList<String>();
                List<String> listSQLNumeric = new ArrayList<String>();

                short vCnt = 0;

                int vLastCellNum = headerRow.getLastCellNum();

                log.info("########## vCnt : " + vCnt + " // headerRow.getLastCellNum  : "
                        + headerRow.getLastCellNum() + " // vLastCellNum : " + vLastCellNum + " // firstDataRow : "
                        + firstDataRow + " // lastRow : " + lastRow);

                for (vCnt = vCnt; vCnt < vLastCellNum; vCnt++) {

                    Cell cell = headerRow.getCell(vCnt);
                    String excelValue = cell.getStringCellValue();

                    if ("EXCELUP08".equals(excelType)) {
                        if (areaIndex > 0) {
                            codeColList = codeColModList;
                        } else {
                            codeColList = codeColRawList;
                        }
                    }

                    log.info("########## @@ codeColList : " + codeColList);

                    for (Map<String, String> codeMap : codeColList) {

                        if (!"".equals(codeMap.get("coRefVal1")) && codeMap.get("coRefVal1") != null) {

                            log.info("########## vCnt : " + vCnt + " // excelValue  : " + excelValue + " // cdNm : "
                                    + codeMap.get("cdNm"));
                            log.info("########## coRefVal2 : " + codeMap.get("coRefVal2") + " // coRefVal3 : "
                                    + codeMap.get("coRefVal3") + " // coRefVal4 : " + codeMap.get("coRefVal4")
                                    + " // coRefVal5 : " + codeMap.get("coRefVal5") + " // coRefVal6 : "
                                    + codeMap.get("coRefVal6"));

                            if (codeMap.get("cellNum") == null || "".equals(codeMap.get("cellNum"))) {
                                if (excelValue.equalsIgnoreCase(codeMap.get("cdNm"))) {
                                    codeMap.put("cellNum", String.valueOf(vCnt));
                                }
                            }

                            if ("EXCELUP07".equals(excelType) && vCnt == 3) {

                                if ("INVOICE_NO".equals(codeMap.get("coRefVal1"))) {
                                    listSQLHeaderCd2.add(codeMap.get("coRefVal1"));
                                    listSQLHeaderNm2.add(codeMap.get("cdNm"));
                                }

                                if ("TAP_CHARGE_INVOICE_PART".equals(codeMap.get("coRefVal2"))) {
                                    listSQLHeaderCd2.add(codeMap.get("coRefVal1"));
                                    listSQLHeaderNm2.add(codeMap.get("cdNm"));
                                } else {
                                    listSQLHeaderCd1.add(codeMap.get("coRefVal1"));
                                    listSQLHeaderNm1.add(codeMap.get("cdNm"));
                                }

                                listSQLRefVal2.add(codeMap.get("coRefVal2"));
                                listSQLRefVal3.add(codeMap.get("coRefVal3"));
                                listSQLRefVal6.add(codeMap.get("coRefVal6"));

                            } else if ("EXCELUP08".equals(excelType)) {

                                log.info("########## areaIndex   : " + areaIndex + " // vCnt : " + vCnt
                                        + " // headerDataRow2 : " + headerDataRow2);

                                if (vCnt == 7) {

                                    log.info("########## @@@ here ~~~");
                                    listSQLHeaderCd1.add(codeMap.get("coRefVal1"));
                                    listSQLHeaderNm1.add(codeMap.get("cdNm"));

                                    listSQLRefVal2.add(codeMap.get("coRefVal2"));
                                    listSQLRefVal3.add(codeMap.get("coRefVal3"));
                                    listSQLRefVal6.add(codeMap.get("coRefVal6"));
                                }

                            } else if (!("EXCELUP07".equals(excelType) || "EXCELUP08".equals(excelType)) && vCnt == 0) {

                                // STD_YMD 컬럼명 추출
                                if ("".equals(STD_YMD_COL) && "STD_YMD".equals(codeMap.get("coRefVal2"))) {
                                    STD_YMD_COL = codeMap.get("coRefVal1");
                                }
                                // DEPT_CD 컬럼명 추출
                                if ("".equals(DEPT_CD_COL) && "DEPT_CD".equals(codeMap.get("coRefVal2"))) {
                                    DEPT_CD_COL = codeMap.get("coRefVal1");
                                }

                                listSQLHeaderCd1.add(codeMap.get("coRefVal1"));
                                listSQLHeaderNm1.add(codeMap.get("cdNm"));

                                listSQLRefVal2.add(codeMap.get("coRefVal2"));
                                listSQLRefVal3.add(codeMap.get("coRefVal3"));
                                listSQLRefVal6.add(codeMap.get("coRefVal6"));
                            }

                            if (vCnt == headerRow.getLastCellNum() - 1) {
                                // 필수 check
                                if (codeMap.get("coRefVal4").equals("NN")) {
                                    listSQLNn.add(codeMap.get("cellNum"));
                                }
                                ;

                                // YMD check
                                if (codeMap.get("coRefVal5").equals("YMD")) {
                                    listSQLYmd.add(codeMap.get("cellNum"));
                                }
                                ;

                                // INT check
                                if (codeMap.get("coRefVal5").equals("INT")) {
                                    listSQLInt.add(codeMap.get("cellNum"));
                                }
                                ;

                                // NUMERIC check
                                if (codeMap.get("coRefVal5").equals("NUMERIC")) {
                                    listSQLNumeric.add(codeMap.get("cellNum"));
                                }
                                ;
                            }
                        }
                    }
                }

                log.info("########## listSQLHeaderCd1 : " + listSQLHeaderCd1 + " // listSQLHeaderNm1 : "
                        + listSQLHeaderNm1);
                log.info("########## listSQLHeaderCd2 : " + listSQLHeaderCd2 + " // listSQLHeaderNm2 : "
                        + listSQLHeaderNm2);
                log.info("########## listSQLRefVal2   : " + listSQLRefVal2 + " // listSQLRefVal3   : "
                        + listSQLRefVal3 + " // listSQLRefVal6 : " + listSQLRefVal6);
                log.info("########## STD_YMD_COL      : " + STD_YMD_COL + " // DEPT_CD_COL : " + DEPT_CD_COL);
                log.info("########## listSQLNn        : " + listSQLNn);
                log.info("########## listSQLYmd       : " + listSQLYmd);
                log.info("########## listSQLInt       : " + listSQLInt);
                log.info("########## listSQLNumeric   : " + listSQLNumeric);

                // 컬럼 개수 체크
                /*
                 * 셀병합된 양식이 있으므로 cellNum으로 비교하는 것은 제외
                 * Row tmpRow = sheet.getRow(0);
                 * 
                 * if( tmpRow.getLastCellNum() != listSQLHeaderCd1.size() ) {
                 * workbook.close();
                 * //throw new ExcelImportException("업로드 하려는 엑셀의 컬럼 개수가 일치하지 않습니다.");
                 * throw new ExcelImportException(messageUtil.getMessage(
                 * "excelUtil.message.incorrectColumnCnt"));
                 * }
                 */

                DataFormatter formatter = new DataFormatter();
                List<List<String>> listSQLData1 = new ArrayList<List<String>>();
                List<List<String>> listSQLData2 = new ArrayList<List<String>>();

                int chkNextArea = 0;
                for (int rowNum = firstDataRow; rowNum <= lastRow; rowNum++) {

                    log.info("########## rowNum : " + rowNum + " // firstDataRow : " + firstDataRow
                            + " // lastRow : " + lastRow);

                    Row currRow = sheet.getRow(rowNum);
                    Cell checkValidRow = null;

                    // EXCELUP01 : 이패킹, EXCELUP02 : 페가, EXCELUP07 : 파일업로드관리(유상송장)
                    if ("EXCELUP01".equals(excelType) || "EXCELUP02".equals(excelType)
                            || "EXCELUP07".equals(excelType)) {
                        try {
                            checkValidRow = currRow.getCell(4);
                        } catch (NullPointerException e) {
                            log.info("########## NullPointerException Exception ==> " + e);
                        }
                    } else if ("EXCELUP08".equals(excelType)) {
                        try {
                            checkValidRow = currRow.getCell(7);
                        } catch (NullPointerException e) {
                            log.info("########## NullPointerException Exception ==> " + e);
                        }
                    } else {
                        checkValidRow = currRow.getCell(0);
                    }

                    String vProcYn = "N";

                    if (checkValidRow == null) {
                        chkNextArea = chkNextArea + 1;
                    }

                    if (chkNextArea == 0) {
                        vProcYn = "Y";
                    } else {

                        if (headerDataRow2 == 0 && checkValidRow != null) {
                            headerDataRow2 = rowNum;
                        }
                    }
                    log.info("########## rowNum : " + rowNum + " // checkValidRow : " + checkValidRow
                            + " // chkNextArea : " + chkNextArea + " // headerDataRow2 : " + headerDataRow2
                            + " // vProcYn : " + vProcYn);

                    if (currRow != null && checkValidRow != null && !"".equals(checkValidRow.getStringCellValue())
                            && vProcYn.equals("Y")) {
                        List<String> rowData1 = new ArrayList<String>();
                        List<String> rowData2 = new ArrayList<String>();

                        if ("EXCELUP08".equals(excelType)) {
                            if (areaIndex > 0) {
                                codeColList = codeColModList;
                            } else {
                                codeColList = codeColRawList;
                            }
                        }

                        for (Map<String, String> codeMap : codeColList) {

                            log.info("########## coRefVal1 : " + codeMap.get("coRefVal1"));

                            if (!"".equals(codeMap.get("coRefVal1")) && codeMap.get("coRefVal1") != null) {

                                short cellNum = Short.parseShort(codeMap.get("cellNum"));
                                Cell tmpCell = currRow.getCell(cellNum);

                                String strFormatCellValue = "";
                                strFormatCellValue = formatter.formatCellValue(tmpCell);

                                log.info("# ");
                                log.info("########## cellNum : " + cellNum + " // cdNm : " + codeMap.get("cdNm"));
                                log.info("########## coRefVal2 : " + codeMap.get("coRefVal2") + " // coRefVal3 : "
                                        + codeMap.get("coRefVal3") + " // coRefVal4 : " + codeMap.get("coRefVal4")
                                        + " // coRefVal5 : " + codeMap.get("coRefVal5") + " // coRefVal6 : "
                                        + codeMap.get("coRefVal6"));
                                log.info("########## CellType : " + tmpCell.getCellType());

                                // NUMERIC 유형
                                if (codeMap.get("coRefVal5").equals("NUMERIC")) {

                                    if (tmpCell.getCellType().toString().equals("NUMERIC")) {

                                        log.info("0 NUMERIC VALUE getNumericCellValue : "
                                                + tmpCell.getNumericCellValue());

                                        double fddata = tmpCell.getNumericCellValue();
                                        DecimalFormat df = new DecimalFormat();
                                        strFormatCellValue = df.format(fddata);

                                        log.info("1 NUMERIC VALUE getStringCellValue  : " + strFormatCellValue + "");

                                        strFormatCellValue = strFormatCellValue + "";
                                        strFormatCellValue = strFormatCellValue.replaceAll(",", "");

                                        log.info("2 NUMERIC VALUE getStringCellValue  : " + strFormatCellValue);
                                    }
                                }

                                log.info("########## tmpCell   : " + tmpCell + " // strFormatCellValue : "
                                        + strFormatCellValue);

                                // 필수 check : Null 허용(불가시 NN)
                                if (codeMap.get("coRefVal4").equals("NN")) {

                                    if (tmpCell == null || tmpCell.equals("") || strFormatCellValue.trim().equals("")) {
                                        /*
                                         * [ {0} ] 은(는) 필수사항입니다.
                                         * 수정 후 다시 업로드 진행해 주세요.
                                         */
                                        throw new ExcelImportException(
                                                strRequired.replace("{0}", "[ " + codeMap.get("cdNm") + " ] ") + "\n"
                                                        + strModify);
                                    }
                                }

                                // DATA 유형별 체크
                                // YMD 유형
                                if (codeMap.get("coRefVal5").equals("YMD")) {

                                    strFormatCellValue = strFormatCellValue.replace("-", "").replace("/", "")
                                            .replace("99999999", "99991231");

                                    int vLengthChk1 = strFormatCellValue.length();

                                    log.info("####### strFormatCellValue : " + strFormatCellValue);
                                    log.info("####### vLengthChk1 : " + vLengthChk1);

                                    if ("EXCELUP08".equals(excelType)) {
                                        strFormatCellValue = strFormatCellValue.substring(4, 8)
                                                + strFormatCellValue.substring(0, 4);

                                    } else {

                                        if (vLengthChk1 != 8) {
                                            /*
                                             * strFormatCellValue = "19.3.12 PM 9:00";
                                             * SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd a hh:mm",
                                             * Locale.US);
                                             * Date dateYmd = sdf.parse(strFormatCellValue);
                                             * 
                                             * //SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
                                             * SimpleDateFormat dt1 = new SimpleDateFormat(codeMap.get("coRefVal6"));
                                             * 
                                             * log.info("########## dt1.format(dateYmd) : " + dt1.format(dateYmd) );
                                             */

                                            SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.US);
                                            Date dateYmd = sdf.parse(strFormatCellValue);

                                            // SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
                                            SimpleDateFormat formatYmd = new SimpleDateFormat(codeMap.get("coRefVal6"));

                                            strFormatCellValue = formatYmd.format(dateYmd);

                                            int vLengthChk2 = strFormatCellValue.length();

                                            if (vLengthChk2 != 8) {
                                                /*
                                                 * [ {0} ] 항목은 숫자만 입력 가능합니다.
                                                 * (format : YYYYMMDD)
                                                 * 수정 후 다시 업로드 진행해 주세요.
                                                 */
                                                throw new ExcelImportException("[ " + codeMap.get("cdNm") + " ] "
                                                        + messageUtil.getMessage("common.message.inputNumber")
                                                        + "\n( format : YYYYMMDD )\n" + strModify);
                                            }
                                        }
                                    }
                                }

                                // INT 유형
                                if (codeMap.get("coRefVal5").equals("INT")) {

                                    String vIntValue = "";

                                    strFormatCellValue = strFormatCellValue.replace(",", "");

                                    if (tmpCell == null || tmpCell.equals("") || strFormatCellValue.trim().equals("")) {
                                        strFormatCellValue = "0";
                                    } else if (tmpCell.equals("N/A") || strFormatCellValue.trim().equals("N/A")) {
                                        strFormatCellValue = "1.00";
                                    }

                                    int vIndex = strFormatCellValue.indexOf(".");

                                    log.info("##########@ vIndex : " + vIndex + " // strFormatCellValue : "
                                            + strFormatCellValue);

                                    if (vIndex > -1) {
                                        if (!"EXCELUP08".equals(excelType)) {
                                            vIntValue = strFormatCellValue.substring(0, vIndex);
                                            strFormatCellValue = vIntValue;
                                        }
                                    }

                                    log.info("########## vIntValue : " + vIntValue + " // strFormatCellValue : "
                                            + strFormatCellValue);

                                    if (!("EXCELUP08".equals(excelType) && codeMap.get("cdNm").equals("Score"))) {
                                        if (strFormatCellValue != null && !isNumber(strFormatCellValue)) {
                                            /*
                                             * [ {0} ] 항목은 숫자만 입력 가능합니다.
                                             * 수정 후 다시 업로드 진행해 주세요.
                                             */
                                            throw new ExcelImportException("[ " + codeMap.get("cdNm") + " ] "
                                                    + messageUtil.getMessage("common.message.inputNumber") + strModify);
                                        }

                                        if (Integer.parseInt(strFormatCellValue) < 0) {
                                            /*
                                             * [ {0} ] 항목은 0 또는 양수만 가능합니다.
                                             * 수정 후 다시 업로드 진행해 주세요.
                                             */
                                            throw new ExcelImportException("[ " + codeMap.get("cdNm") + " ] "
                                                    + messageUtil.getMessage("common.message.positiveNumber")
                                                    + strModify);
                                        }
                                    }
                                }

                                // STD_YMD 값 추출
                                if ("EXCELUP04".equals(excelType)) {
                                    STD_YMD_VAL = optionStdYmd;
                                } else {
                                    if ("".equals(STD_YMD_VAL) && STD_YMD_COL.equals(codeMap.get("coRefVal1"))) {
                                        STD_YMD_VAL = strFormatCellValue;
                                        STD_YMD_VAL = STD_YMD_VAL.replace("-", "").replace("/", "");
                                    }
                                }
                                log.info("########## STD_YMD_VAL : " + STD_YMD_VAL + " // optionStdYmd : "
                                        + optionStdYmd);

                                // DEPT_CD 값 추출
                                if ("EXCELUP08".equals(excelType)) {
                                    DEPT_CD_VAL = sheetName;

                                } else {
                                    if ("".equals(DEPT_CD_VAL) && DEPT_CD_COL.equals(codeMap.get("coRefVal1"))) {
                                        DEPT_CD_VAL = strFormatCellValue;
                                    }
                                }

                                log.info("########## DEPT_CD_VAL : " + DEPT_CD_VAL);
                                log.info("########## strFormatCellValue : " + strFormatCellValue);
                                log.info("# ");

                                if ("EXCELUP07".equals(excelType)) {

                                    if ("INVOICE_NO".equals(codeMap.get("coRefVal1"))) {
                                        rowData2.add(strFormatCellValue);
                                    }

                                    if ("TAP_CHARGE_INVOICE_PART".equals(codeMap.get("coRefVal2"))) {
                                        rowData2.add(strFormatCellValue);
                                    } else {
                                        rowData1.add(strFormatCellValue);
                                    }

                                } else {
                                    rowData1.add(strFormatCellValue);
                                }
                            }
                        }

                        listSQLData1.add(rowData1);
                        listSQLData2.add(rowData2);
                    }
                }

                if ("".equals(STD_YMD_VAL)) {
                    // 오늘날짜
                    String dateToday = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    STD_YMD_VAL = dateToday;
                }

                Map insertMap = new HashMap();

                // insertMap.put("headerListCd1", listSQLHeaderCd1);
                // insertMap.put("headerListNm1", listSQLHeaderNm1);
                // insertMap.put("headerListCd2", listSQLHeaderCd2);
                // insertMap.put("headerListNm2", listSQLHeaderNm2);
                // insertMap.put("listSQLRefVal2", listSQLRefVal2);
                // insertMap.put("listSQLRefVal3", listSQLRefVal3);
                // insertMap.put("listSQLRefVal6", listSQLRefVal6);
                // insertMap.put("companyId", companyId);
                // insertMap.put("languageCd", languageCd);
                // insertMap.put("sessionUserId", sessionVO.getUserId());
                // insertMap.put("timeDiff", timeDiff);
                // insertMap.put("areaIndex", areaIndex);

                if ("EXCELUP03".equals(excelType)) {
                    // EXCELUP03 : 부품 > 부품관리 > 부품 업로드

                    String errMsg = "";
                    // errMsg = partService.insertPartFromExcel(insertMap, codeColList,
                    // listSQLData1, sessionVO);

                    if (errMsg.equals("")) {
                        rtnMap.put("successYN", "Y");
                    } else {
                        rtnMap.put("successYN", "N");
                        rtnMap.put("errMsg", errMsg);
                    }

                } else if ("EXCELUP06".equals(excelType)) {
                    // EXCELUP06 : 애플 회계연도 관리

                    String errMsg = "";
                    // errMsg = fiscalCalendarService.insertFiscalCalendarFromExcel(insertMap,
                    // codeColList, listSQLData1, sessionVO);

                    if (errMsg.equals("")) {
                        rtnMap.put("successYN", "Y");
                    } else {
                        rtnMap.put("successYN", "N");
                    }
                    rtnMap.put("errMsg", errMsg);

                } else {

                    // server status
                    // String strServerStatus = properties.getProperty("server.status");

                    // PATH 생성
                    String strPath = "";
                    // String strPath = properties.getProperty("attach.excel.file.path");

                    // if ("prod".equalsIgnoreCase(strServerStatus)) {
                    // strPath = properties.getProperty("attach.excel.file.path.prod");
                    // } else if ("dev".equalsIgnoreCase(strServerStatus)) {
                    // strPath = properties.getProperty("attach.excel.file.path.dev");
                    // } else {
                    // strPath = properties.getProperty("attach.excel.file.path.local");
                    // }

                    // 회사코드 replace
                    // strPath = strPath.replace("{companyId}", companyId);

                    if ("fileUploadList".equals(excelPopNm)) {
                        strPath = strPath + "/" + optionPopUploadTypeCd;
                    }

                    File excelPath = new File(strPath + "/" + STD_YMD_VAL + "/");

                    if (excelPath.exists() == false) {
                        excelPath.mkdirs();
                    }

                    String strFileName = mf.getOriginalFilename();

                    File excelFile = new File(strPath + "/" + STD_YMD_VAL + "/" + strFileName);

                    String chkFileYn = "Y";

                    if ("EXCELUP05".equals(excelType)) {
                        chkFileYn = "N";

                    } else if ("EXCELUP08".equals(excelType)) {

                        if (sheetIndex > 0 || areaIndex > 0) {
                            chkFileYn = "N";
                        }
                    }

                    log.info("########## strPath      : " + strPath); // d:/attach/excel
                    log.info("########## excelPath    : " + excelPath); // d:\attach\excel\20190306
                    log.info("########## strFileName  : " + strFileName); // PEGA_TEST.xlsx
                    log.info("########## excelFile    : " + excelFile); // d:\attach\excel\20190306\PEGA_TEST.xlsx
                    log.info("########## fileSavePath (OLD) : " + STD_YMD_VAL + "/"); // 20190306/
                    log.info("########## fileSavePath : " + excelPath + "/"); // d:\attach\excel\20190306/

                    if (excelFile.exists() && chkFileYn.equals("Y")) {
                        workbook.close();
                        // throw new ExcelImportException("동일한 이름의 파일이 존재합니다.");
                        throw new ExcelImportException(messageUtil.getMessage("common.message.existFile"));

                    } else {

                        String errMsg = "";

                        insertMap.put("stdYmd", STD_YMD_VAL);
                        insertMap.put("regYmd", STD_YMD_VAL);
                        insertMap.put("shipToNoToDeptCd", DEPT_CD_VAL);
                        insertMap.put("fileNm", strFileName);
                        // insertMap.put("fileSavePath" , STD_YMD_VAL + "/");
                        insertMap.put("fileSavePath", excelPath + "/");

                        try {

                            if ("EXCELUP01".equals(excelType)) {
                                // partService.insertEpackingFromExcel(insertMap, listSQLData1);
                            } else if ("EXCELUP05".equals(excelType)) {
                                // EXCELUP05 : 파일업로드관리 : CSAT
                                // csatService.insertCsatFromExcel(insertMap, codeColList, listSQLData1,
                                // sessionVO);
                            }

                        } catch (RuntimeException e) {
                            workbook.close();
                            throw e;
                        } catch (Exception e) {
                            workbook.close();
                            throw e;
                        }

                        if ("EXCELUP01".equals(excelType) || "EXCELUP02".equals(excelType)) {
                            rtnMap.put("successYN", "Y");
                            rtnMap.put("stdYmd", STD_YMD_VAL);
                            rtnMap.put("deptCd", (String) insertMap.get("deptCd"));

                        } else {
                            if (errMsg.equals(""))
                                rtnMap.put("successYN", "Y");
                            else {
                                rtnMap.put("successYN", "N");
                                rtnMap.put("errMsg", errMsg);
                            }
                        }

                        mf.transferTo(excelFile);
                    }
                }

            } // end of - for( int areaIndex = 0 ; areaIndex < totalAreaCnt ; areaIndex++ )
        } // end of - for( int sheetIndex = 0 ; sheetIndex < totalSheetCnt ; sheetIndex++
          // )

        workbook.close();

        return rtnMap;
    }

    private void download(XSSFWorkbook workbook, HttpServletResponse res, HttpServletRequest req, String strFileName)
            throws Exception {
        strFileName += "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        res.setHeader("Content-disposition",
                "attachment;filename=" + java.net.URLEncoder.encode(strFileName, "utf-8") + ".xlsx");
        res.setHeader("Content-disposition", "attachment;filename="
                + java.net.URLEncoder.encode(strFileName, "utf-8").replace("+", "%20").replace("%28", "(")
                        .replace("%29", ")")
                + ".xlsx");
        // res.setHeader("Content-Type", "application/vnd.ms-excel; charset=utf-8");
        res.setHeader("Content-Type",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
        res.setHeader("Content-Transfer-Encoding", "binary;");
        res.setHeader("Pragma", "no-cache;");
        res.setHeader("Expires", "-1;");
        res.setHeader("Set-Cookie", "fileDownload=true; path=/");

        ServletOutputStream out = res.getOutputStream();
        workbook.write(out);
        out.close();
        workbook.close();
    }

    private XSSFCellStyle setHeaderStyle(XSSFWorkbook workbook) {
        // 데이터 헤더 폰트
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);

        XSSFCellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        byte[] b = { (byte) 0xE0, (byte) 0xE0, (byte) 0xE0 };

        XSSFColor backgroundColor = new XSSFColor(b, new DefaultIndexedColorMap());
        headerStyle.setFillForegroundColor(backgroundColor);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        headerStyle.setTopBorderColor(IndexedColors.BLACK.index);
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        headerStyle.setRightBorderColor(IndexedColors.BLACK.index);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return headerStyle;
    }

    private XSSFCellStyle setBodyStyle(XSSFWorkbook workbook) {
        XSSFCellStyle bodyStyle = workbook.createCellStyle();

        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setTopBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setRightBorderColor(IndexedColors.BLACK.index);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return bodyStyle;
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
