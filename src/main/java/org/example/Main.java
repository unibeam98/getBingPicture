package org.example;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        //从bing获取图片
        if (args.length == 0) {
            GetBingPicture bingPicture = new GetBingPicture();
            URL url = new URL(bingPicture.getPictureURL());
            GetBingPicture.downloadPicture(url, bingPicture.getPictureName());
        } else if (args.length == 1) {
            GetBingPicture bingPicture = new GetBingPicture();
            URL url = new URL(bingPicture.getPictureURL());
            GetBingPicture.downloadPicture(url, args[0] + bingPicture.getPictureName());
        }
    }
}