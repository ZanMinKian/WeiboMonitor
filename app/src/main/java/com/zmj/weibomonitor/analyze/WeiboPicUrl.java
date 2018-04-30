package com.zmj.weibomonitor.analyze;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by ZMJ on 2018/4/25.
 */

/*顾名思义*/
public class WeiboPicUrl {
    private String url;
    public WeiboPicUrl(String url){
        this.url=url;
    }
    
    public byte[] toByteArray() throws IOException {
        Connection.Response resp= Jsoup
                .connect(url)
                .ignoreContentType(true)
                .execute();
        return resp.bodyAsBytes();
    }
}
