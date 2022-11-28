package com.sk.signet.onm.common.grid;

import java.util.List;
import java.util.Map;

public class GridUtil {
    /**
     * 리스트 코드를 그리드 셀렉트 박스용 텍스트로 변경 (ex -> CODE1:CODE1VALUE;CODE2:CODE2VALUE
     *
     * @param listCode
     * @return
     */
    public static String getGridSelectbox(List<Map<String, String>> listCode) {
        int cnt = 0;
        String gridSelValue = "";
        for (Map<String, String> code : listCode) {
            if (cnt > 0)
                gridSelValue += ";";
            gridSelValue += code.get("cd") + ":" + code.get("cdNm");
            cnt++;
        }
        return gridSelValue;
    }

    public static String getGridSelectbox(List<Map<String, String>> listCode, String val, String valNm) {
        int cnt = 0;
        String gridSelValue = "";
        for (Map<String, String> code : listCode) {
            if (cnt > 0)
                gridSelValue += ";";
            gridSelValue += code.get(val) + ":" + code.get(valNm);
            cnt++;
        }
        return gridSelValue;
    }
}
