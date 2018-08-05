package com.company.babysteps.utils;

import android.util.Log;

import com.company.babysteps.entities.Post;
import com.company.babysteps.entities.Week;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

//Class mapper the Jsoup elements to a concrete object.
public final class HTMLParserUtils {

    private static final String ID_BODY = "stageBody";
    private static final String TAG_WEEK_HEADER = "h1";
    private static final String IMAGE_DIV_CLASS = "stageMainImg";
    private static final String IMAGE_TAG = "img";
    private static final String URL_IMAGE_ATTR = "src";
    private static final String HEADER_TAG = "h2";
    private static final String PARAGRAPH_TAG = "p";

    private HTMLParserUtils() { }

    public static Week getWeekInfo(String path) {
        Week week = new Week();
        try {
            Connection connection = Jsoup.connect(path);
            connection.timeout(5000);
            Document doc = connection.get();
            Element body = doc.getElementById(ID_BODY);
            Element weekHeader = doc.getElementsByTag(TAG_WEEK_HEADER).first();
            week.setTitle(weekHeader.text());
            Element imageDiv = doc.getElementsByClass(IMAGE_DIV_CLASS).first();
            if(imageDiv != null) {
                Element element = imageDiv.getElementsByTag(IMAGE_TAG).first();
                String urlImage = element.absUrl(URL_IMAGE_ATTR);
                week.setImageUrl(urlImage.equals("") ? null : urlImage);
            }
            Element header = body.select(HEADER_TAG).first();
            Elements siblings = header.siblingElements();
            Post post = new Post();
            int index = 0;
            post.setId(index++);
            post.setTitle(header.text());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < siblings.size(); i++) {
                Element sibling = siblings.get(i);
                if(PARAGRAPH_TAG.equals(sibling.tagName()) && !sibling.text().isEmpty()) {
                    sb.append(sibling.text()).append("\n\n");
                    if(i == siblings.size() - 1) {
                        post.setBody(sb.toString());
                        week.addPost(post);
                    }
                } else if(HEADER_TAG.equals(sibling.tagName())) {
                    post.setBody(sb.toString());
                    week.addPost(post);
                    post = new Post();
                    sb = new StringBuilder();
                    post.setId(index++);
                    post.setTitle(sibling.text());
                }
            }
            return week;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(HTMLParserUtils.class.getSimpleName(), e.getMessage());
            return null;
        }
    }
}