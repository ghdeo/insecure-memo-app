package com.hongik.insecurememo.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemoItem {
    public String category;
    public String memo;
    public String regDate;

    public MemoItem(String category, String memo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date date = new Date();

        this.category = category;
        this.memo = memo;
        this.regDate = format.format(date);
    }
}
