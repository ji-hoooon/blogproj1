package com.mino.blogproj.core.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//썸네일을 만들기 위해 파싱하는 유틸 클래스
public class MyParseUtil {
    public static String getThumbnail(String html){
        String thumbnail;
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("img");

        if(els.size() == 0){
//            thumbnail= "/upload/person.png";
            thumbnail= "/images/dora.png";
            //사진이 없으면 기본 이미지로
        }else{
            Element el = els.get(0);
            thumbnail = el.attr("src");
        }
        return thumbnail;
    }
}
