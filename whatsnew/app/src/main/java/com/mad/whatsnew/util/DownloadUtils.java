package com.mad.whatsnew.util;

import android.os.AsyncTask;
import android.util.Log;

import com.mad.whatsnew.model.News;
import com.mad.whatsnew.newsActivity.NewsPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Download Utils class
 */
public class DownloadUtils {

    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String DES_TAG = "description";
    private static final String CATE_TAG = "category";
    private static final String CREATOR_DEFAULT = "unknown";
    private static final String CREATOR_TAG = "dc:creator";
    private static final String INPUT_DF = "EEE, d MMM yyyy H:m:s z";
    private static final String OUTPUT_DF = "d MMM yyyy";
    private static final String PUBD_TAG = "pubDate";
    private static final String LINK_TAG = "link";
    private static final String P_TAG = "<p>";
    private static final String P_END_TAG = "</p>";
    private static final String DEFAULT_VAL = "default";

    /**
     * Download xml
     * @param webLink the link
     * @param keyword the keyword
     * @param numbers the number to download
     * @return ArrayList of news object
     */
    public static ArrayList<News> downLoadXml(String webLink, String keyword, int numbers) {
        ArrayList<News> downloadList = new ArrayList<>();
        try {
            URL url = new URL(webLink);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.parse(new InputSource(url.openStream()));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(ITEM_TAG);
            if (nodeList.getLength() < numbers) {
                numbers = nodeList.getLength();
            }

            for (int i = 0; i < numbers; i++) {
                Boolean relate = false;
                Element item = (Element) nodeList.item(i);
                String title = item.getElementsByTagName(TITLE_TAG).item(0).getTextContent();
                if (title.toLowerCase().contains(keyword.toLowerCase()) && !keyword.equals("")) {
                    relate = true;
                }

                String des = item.getElementsByTagName(DES_TAG)
                        .item(0).getTextContent();
                if (des.toLowerCase()
                        .contains(keyword.toLowerCase()) && !keyword.endsWith("")) {
                    relate = true;
                }

                NodeList cateList = item.getElementsByTagName(CATE_TAG);
                ArrayList<String> cateArray = new ArrayList<>();
                for (int a = 0; a < cateList.getLength(); a++) {
                    String cate = cateList.item(a).getTextContent();
                    if (cate.toLowerCase().equals(keyword)) {
                        relate = true;
                    }
                    cateArray.add(cate);
                }

                String creator = CREATOR_DEFAULT;
                NodeList childNl = nodeList.item(i).getChildNodes();
                for (int b = 0; b < childNl.getLength(); b++) {
                    if (childNl.item(b).getNodeName().equals(CREATOR_TAG)) {
                        creator = childNl.item(b).getTextContent();
                        break;
                    }
                }
                if (creator.toLowerCase().contains(keyword)) {
                    relate = true;
                }

                if (keyword.equals("")) {
                    relate = true;
                }

                if (relate) {
                    //date
                    DateFormat inputDF  = new SimpleDateFormat(INPUT_DF);
                    DateFormat outputDF = new SimpleDateFormat(OUTPUT_DF);
                    String fullDate = item.getElementsByTagName(PUBD_TAG).item(0).getTextContent();
                    String date = outputDF.format(inputDF.parse(fullDate));
                    //date

                    String link = item.getElementsByTagName(LINK_TAG).item(0).getTextContent();
                    String description = item.getElementsByTagName(DES_TAG).item(0).getTextContent();
                    description = description.replace(P_TAG, "");
                    description = description.replace(P_END_TAG, "");
                    description = description.trim();

                    News news = new News(title, description, date, creator
                            , link, DEFAULT_VAL, DEFAULT_VAL, DEFAULT_VAL, cateArray);
                    downloadList.add(news);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return downloadList;
    }

    /**
     * Download one news object
     * @param webLink the link
     * @return new object
     */
    public static News downloadOneNews(String webLink) {
        News news = null;
        try {
            URL url = new URL(webLink);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.parse(new InputSource(url.openStream()));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(ITEM_TAG);

            Element item = (Element) nodeList.item((int)(Math.random()*nodeList.getLength()));
            String title = item.getElementsByTagName(TITLE_TAG).item(0).getTextContent();

            String creator = CREATOR_DEFAULT;
            NodeList childNl = nodeList.item((int)(Math.random()*nodeList.getLength())).getChildNodes();
            for (int b = 0; b < childNl.getLength(); b++) {
                if (childNl.item(b).getNodeName().equals(CREATOR_TAG)) {
                    creator = childNl.item(b).getTextContent();
                    break;
                }
            }

            //date
            DateFormat inputDF  = new SimpleDateFormat(INPUT_DF);
            DateFormat outputDF = new SimpleDateFormat(OUTPUT_DF);
            String fullDate = item.getElementsByTagName(PUBD_TAG).item(0).getTextContent();
            String date = outputDF.format(inputDF.parse(fullDate));
            //date

            String link = item.getElementsByTagName(LINK_TAG).item(0).getTextContent();
            String description = item.getElementsByTagName(DES_TAG).item(0).getTextContent();
            description = description.replace(P_TAG, "");
            description = description.replace(P_END_TAG, "");
            description = description.trim();

            news = new News(title, description, date, creator
                    , link, DEFAULT_VAL, DEFAULT_VAL, DEFAULT_VAL, null);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return news;
    }

    /**
     * Download text from html
     * @param webLink the link
     * @return the text of html
     */
    public static String downloadHtml(String webLink) {
        String line = null;
        String newsContent = "";
        try {
            URL url = new URL(webLink);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // read each line and write to System.out
            while ((line = br.readLine()) != null) {
                if (line.contains(P_TAG) && line.contains(P_END_TAG)) {
                    Document doc = Jsoup.parse(line);
                    Elements contents = doc.select("p");
                    for (org.jsoup.nodes.Element element : contents) {
                        String content = element.text();
                        newsContent += content;
                        newsContent += "\n\n";
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        newsContent = newsContent.trim();
        newsContent += "\n\n\n\n\n\n\n\n";
        return newsContent;
    }
}
