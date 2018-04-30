package com.zmj.weibomonitor.analyze;

import com.zmj.po.Weibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMJ on 2018/4/25.
 */

public class WeiboFetcher {
    private static final String weiboUrlBase="https://m.weibo.cn/api/container/getIndex?type=uid&value=%s&containerid=107603%s";

    /*获取微博*/
    public static List<Weibo> fetch(String weiboId) throws IOException,JSONException{
        List<Weibo> weibos=new ArrayList<Weibo>();

        Connection.Response resp = Jsoup
                .connect(String.format(weiboUrlBase, weiboId, weiboId))
                .ignoreContentType(true)
                .execute();
        JSONObject json = new JSONObject(resp.body());
        int ok = json.getInt("ok");

        if (ok == 1) {
            JSONArray cards = json.getJSONObject("data").getJSONArray("cards");
            for (int i = 0; i < cards.length(); i++) {
                Weibo weibo=new Weibo();
                weibo.setAuthorId(Long.valueOf(weiboId));

                JSONObject mblog = cards.getJSONObject(i).getJSONObject("mblog");
                String text = mblog.get("text").toString();//微博文字
                weibo.setText(text);

                try {//若发生JSONException，说明这条微博是转发过来的，或是一条无图微博
                    JSONArray pics = mblog.getJSONArray("pics");
                    List<String> picsUrl=new ArrayList<String>();
                    for (int j = 0; j < pics.length(); j++) {
                        String url = pics.getJSONObject(j).get("url").toString();//微博图片url
                        picsUrl.add(url);
                    }
                    weibo.setType(Weibo.TYPE_WEIBO_WITH_PIC);
                    weibo.setPicsUrl(picsUrl);
                } catch (JSONException e) {
                    try {//若发生异常，说明是无图微博
                        JSONObject retweeted_status = mblog.getJSONObject("retweeted_status");
                        String originName = retweeted_status.getJSONObject("user").getString("screen_name");//被转发微博的博主名
                        String originText = retweeted_status.getString("text");//被转发微博的内容

                        weibo.setType(Weibo.TYPE_FORWORDING_WEIBO);
                        weibo.setOriginAuthorName(originName);
                        weibo.setOriginWeiBoText(originText);
                    } catch (JSONException e1) {
                        weibo.setType(Weibo.TYPE_WEIBO_WITHOUT_PIC);
                    }
                }

                weibos.add(weibo);
            }
        }

        return weibos;
    }
}
