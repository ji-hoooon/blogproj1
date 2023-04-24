package com.mino.blogproj.model.util;

import com.jayway.jsonpath.JsonPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

//컨텐트에서 src를 찾아서 이미지  파싱 -> 썸네일 가져온다
//사용자에게 선택을 받아서 해도 되지만 여기선 0번지로
public class JsoupTest {
//    String html="<!DOCTYPE html>\n" +
//            "<html lang=\"en\">\n" +
//            "<head>\n" +
//            "  <title>Document</title>\n" +
//            "</head>\n" +
//            "<body>\n" +
//            "  <div id=\"weather\">10도</div>\n" +
//            "  <div class=\"loc\">서울</div>\n" +
//            "</body>\n" +
//            "</html>";

    String html=" <div id=\"weather\">10도</div>\n" +
            "<div class=\"loc\">서울</div>\n";
    @Test
    public void jsoup_test(){
        System.out.println(html);
        Document doc = Jsoup.parse(html);
        System.out.println(doc.toString());
        //한 건인지 여러건인지 알 수 없으므로
        Elements elements = doc.select("#weather");
        System.out.println(elements.get(0).text());

        Elements elements2 = doc.select(".loc");
        System.out.println(elements2.get(0).html());    //서울 안에 객체라면 그대로
        System.out.println(elements2.get(0).text());    //서울 안에 텍스트만
    }

}
