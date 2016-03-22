package xiaofan.insdownloader.insparser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by zhaoyu on 2015/2/27.
 */
public class Parser {
    public static String parseUrl(String url){
        //<meta property="og:video" content="http://scontent-sin1-1.cdninstagram.com/t50.2886-16/11669168_841650245913624_887345504_n.mp4">
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36").timeout(30 * 1000).get();
            Elements elements = document.getElementsByAttributeValue("property","og:image");
            if(elements == null || elements.size() == 0){
                return null;
            }
            Element element = elements.get(0);
            String photoUrl = element.attr("content");
            Log.w("Parser","parse url is:" + photoUrl);
            return photoUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
     }


    public static MediaData parseMedia(String url){
        //<meta property="og:video" content="http://scontent-sin1-1.cdninstagram.com/t50.2886-16/11669168_841650245913624_887345504_n.mp4">
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36").timeout(30 * 1000).get();
            Elements imageElements = document.getElementsByAttributeValue("property","og:image");
            Elements videoElements =  document.getElementsByAttributeValue("property","og:video");
            if(imageElements == null || imageElements.size() == 0){
                return null;
            }
            MediaData mediaData = new MediaData();
            if(videoElements == null || videoElements.size() == 0){
                mediaData.isImage = true;
                Element element = imageElements.get(0);
                mediaData.url = element.attr("content");
            }else{
                mediaData.isImage = false;
                Element element = videoElements.get(0);
                mediaData.url = element.attr("content");
            }
            return mediaData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
