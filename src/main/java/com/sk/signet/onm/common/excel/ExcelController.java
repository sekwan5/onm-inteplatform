package com.sk.signet.onm.common.excel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sk.signet.onm.common.exception.ExcelImportException;
import com.sk.signet.onm.common.grid.GridResultVo;
import com.sk.signet.onm.common.utils.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

        log.debug("########## downloadExcel ########## paramMap : " + paramMap);

        // Cookie setFileDown = new Cookie("fileDownYn", "N");
        // res.addCookie(setFileDown);

        String paramExcelHeader = (String) paramMap.get("excelHeader");
        String paramExcelKey = (String) paramMap.get("excelKey");
        String paramExcelWidth = (String) paramMap.get("excelWidth");
        String pageId = (String) paramMap.get("pageId");
        log.debug("########## downloadExcel pageId : " + pageId);
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
        GridResultVo gridList = null;

        if ("serviceCompany".equals(pageId)) { // 서비스사업자관리
            // gridList = serviceService.selectServiceCompanyList(paramMap);
            excelList = gridList.getRows();

        }

        // log.debug("############## excelList.size() : " + excelList.size() );

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
            XSSFRow bodyRow = null;
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
                    XSSFRow bodyRow1 = sheet.createRow(row + i);
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
                        XSSFRow bodyRow1 = sheet.createRow(rowCnt + xx);
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

            XSSFRow nameRow = sheet.createRow(1);
            Cell nameCell = nameRow.createCell(0);
            nameCell.setCellValue(excelNm);
            nameCell.setCellStyle(nameStyle);

            // 시간 표기
            XSSFRow dateRow = sheet.createRow(2);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            // 다운로드
            download(workbook, res, req, excelNm);

        } else {
            res.setContentType("text/html;charset:UTF-8");
            res.setCharacterEncoding("UTF-8");
            res.setHeader("Cache-Control", "no-cache");

            String vMessage = messageUtil.getMessage("common.message.failToDownloadExcel"); // 엑셀 다운로드할 데이터가 없습니다.
            log.debug("############## vMessage : " + vMessage);// document.getElementById('asxLoading').hide()

            throw new ExcelImportException(
                    "successMsg " + messageUtil.getMessage("excelUtil.message.incorrectColumnCnt"));
            // res.getWriter().print("<script>alert('데이터가 조회되지 않아 다운로드 할 수
            // 없습니다.');</script>");asxLoading
        }
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
