package com.sk.signet.onm.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("reportUtil")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportUtil {

    @Autowired
    @Qualifier("propertyService")
    private Properties properties;

    @Value("#{propertyService['report.file.path']}")
    public String CONTRACT_PATH;
    public String IMAGE_PATH = "/image/";

    /**
     * 리포트 번호 조합 리턴
     *
     * @param map
     * @return
     */
    public String getReportNo(Map map) {
        String strReturn = "";

        if (!"".equals(map.get("acceptNo")) && !"".equals(map.get("createYmd")) && !"".equals(map.get("reportTypeCd"))
                && !"".equals(map.get("reportSeq"))) {
            String acceptNo = (String) map.get("acceptNo");
            String createYmd = ((String) map.get("createYmd")).replaceAll("\\-", "").replaceAll(" ", "");
            String reportTypeCd = (String) map.get("reportTypeCd");
            String reportSeq = (String) map.get("reportSeq");
            reportSeq = StringUtils.leftPad(reportSeq, 2, '0');
            strReturn = acceptNo + "_" + createYmd + "_" + reportTypeCd + "_" + reportSeq;
        }

        return strReturn;
    }

    /**
     * 리포트 번호 이미지 파일명 리턴
     *
     * @param map
     * @return
     */
    public String getReportImageFileName(Map map) {
        String strReturn = "";

        if (!"".equals(map.get("acceptNo")) && !"".equals(map.get("createYmd")) && !"".equals(map.get("reportTypeCd"))
                && !"".equals(map.get("reportSeq"))) {
            String acceptNo = (String) map.get("acceptNo");
            String createYmd = ((String) map.get("createYmd")).replaceAll("\\-", "").replaceAll(" ", "");
            String reportTypeCd = (String) map.get("reportTypeCd");
            String reportSeq = (String) map.get("reportSeq");
            // String modDt = (String)map.get("modDt");
            reportSeq = StringUtils.leftPad(reportSeq, 2, '0');
            strReturn = acceptNo + "_" + createYmd + "_" + reportTypeCd + "_" + reportSeq;
        }

        return strReturn;
    }

    /**
     * Capture 이미지 저장
     *
     * @param paramMap - FileName, FilePath 전달
     * @throws Exception
     */
    public void saveImage(Map paramMap) throws Exception {
        String fileName = this.getReportImageFileName(paramMap) + ".png";
        String fileSavePath = this.CONTRACT_PATH + "/" + paramMap.get("createYmd");
        byte[] b = Base64.decodeBase64(((String) paramMap.get("reportIMG")).split(",")[1]);

        File dir = new File(fileSavePath);
        if (!dir.exists())
            dir.mkdirs();

        File imageFile = new File(fileSavePath + "/" + fileName);
        if (!imageFile.exists()) {
            OutputStream os = new FileOutputStream(imageFile);
            IOUtils.write(b, os);
            os.close();
        }

        paramMap.put("fileNm", fileName);
        paramMap.put("fileSavePath", fileSavePath);
    }
}
