/**
 * Copyright © 2024 unibeam Created. All rights reserved.
 * FileName: getBingPicture
 * Author:   unibeam
 * Date:     2024/6/18
 * Description: 用于下载每日bing壁纸
 */

package org.example;

import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <用于下载每日bing壁纸>
 *
 * @Author unibeam
 * @createTime 2024/6/18 下午5:09
 * @since 0.1.0
 */

public class GetBingPicture {
    public static String bingJSON;
    public static String pictureURL;


    public GetBingPicture() throws MalformedURLException {
        URL request = new URL("https://global.bing.com/HPImageArchive" +
                ".aspx?format=js&idx=0&n=1&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160");
        bingJSON = getJSON(request);
    }


    public static void downloadPicture(URL url, String path) {
        try {
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getJSON(URL request) {
        String json;
        try {
            StringBuffer jsonBuffer = new StringBuffer();
            URLConnection connection = request.openConnection();
            //读取返回的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonBuffer.append(inputLine);
            }
            json = jsonBuffer.toString();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    private static String transformTime(String time) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(time, inputFormatter);
        return date.format(outputFormatter);
    }

    public String getPictureURL() {
        if (!bingJSON.isEmpty()) {
            //对json进行处理返回图片URL
            JSONObject jsonObject = JSONObject.parseObject(bingJSON);
            JSONObject images = jsonObject.getJSONArray("images").getJSONObject(0);
            pictureURL = (String) images.get("urlbase");
        }
        return "https://global.bing.com" + pictureURL + "_UHD.jpg";
    }

    public String getPictureName() {
        String time = null;
        if (!bingJSON.isEmpty()) {
            //对json进行处理返回图片URL
            JSONObject jsonObject = JSONObject.parseObject(bingJSON);
            JSONObject images = jsonObject.getJSONArray("images").getJSONObject(0);
            pictureURL = (String) images.get("urlbase");
            time = (String) images.get("enddate");
        }
        return pictureURL.replace("/th?id=OHR.", "") + "-" + transformTime(time) + ".jpg";
    }
}
