package com.sk.signet.onm.common.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
@SuppressWarnings({ "rawtypes" })
public class GridResultVo {

    private int total = 1; // 총 페이지 갯수
    private int page = 1; // 현재 페이지
    private int records = 0; // 총row 갯수
    private int rowPerPage = 10; // 페이지 당 보여줄 row
    private List<Map<String, String>> rows = new ArrayList<Map<String, String>>(); // 그리드에 뿌려질 data

    public void setPage(int page) {
        this.page = page;
        setTotal();
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
        setTotal();
    }

    public void setRecords(int records) {
        this.records = records;
        setTotal();
    }

    public void setTotal() {
        this.total = (this.records / this.rowPerPage) + 1 - (this.records % this.rowPerPage == 0 ? 1 : 0);
        if (this.total == 0) {
            this.total = 1;
        }
    }
}
