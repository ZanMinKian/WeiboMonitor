package com.zmj.po;
import java.io.Serializable;
import java.util.List;

public class Weibo implements Serializable{
    public static final int TYPE_WEIBO_WITH_PIC=0;
    public static final int TYPE_WEIBO_WITHOUT_PIC=1;
    public static final int TYPE_FORWORDING_WEIBO=2;


    private int type;
    private Long authorId;//weiboId
    private String text;
    private List<String> picsUrl;

    private String originAuthorName;
    private String originWeiboText;



    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public List<String> getPicsUrl() {
        return picsUrl;
    }
    public void setPicsUrl(List<String> picsUrl) {
        this.picsUrl = picsUrl;
    }
    public String getOriginAuthorName() {
        return originAuthorName;
    }
    public void setOriginAuthorName(String originAuthorName) {
        this.originAuthorName = originAuthorName;
    }
    public String getOriginWeiBoText() {
        return originWeiboText;
    }
    public void setOriginWeiBoText(String originWeiBoText) {
        this.originWeiboText = originWeiBoText;
    }
}
