package com.sk.signet.onm.common.excel;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class ExcelService {

    public List<Map<String, String>> downloadExcelList(Map param) throws Exception {

        // LOGGER.info("###################################### downloadExcelList start
        // ");

        // 예약상태
        if (param.get("searchStatusCd") != null && !"".equals(param.get("searchStatusCd"))) {
            String statusCd = (String) param.get("searchStatusCd");
            if (statusCd.indexOf(",") > -1) {
                param.put("searchStatusCd", statusCd.split(","));
            }
        }

        // 부서리스트
        if (param.get("searchDeptCdList") != null && !"".equals(param.get("searchDeptCdList"))) {
            // 다중 부서 권한을 갖는 경우
            String deptCd = (String) param.get("searchDeptCdList");
            param.put("searchDeptCdList", deptCd.split(","));

        } else if (param.get("searchDeptCd") != null && !"".equals(param.get("searchDeptCd"))) {
            // 일반사용자
            String deptCd = (String) param.get("searchDeptCd");
            param.put("searchDeptCdList", deptCd.split(","));
        }

        return null;
        // excelMapper.downloadExcelList(param);
    }
}
