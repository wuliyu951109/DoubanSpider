package com.douban.spider;

import com.douban.spider.dao.MovieMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
public class SpiderApplication implements CommandLineRunner {

    @Autowired
    private MovieMapper movieMapper;

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
    }

    public String getJsonText() throws IOException {
        final String url = "https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=time&page_limit=20&page_start=0";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String result = "";
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        response.close();
        return result;
    }

    @Override
    public void run(String... args) throws Exception {

        final String jsonText = getJsonText();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, ArrayList<HashMap<String, Object>>> jsonmap = objectMapper.readValue(jsonText, HashMap.class);
        ArrayList<HashMap<String, Object>> subjects = jsonmap.get("subjects");
        for (int i = 0; i < subjects.size(); i++) {
            final HashMap<String, Object> movie = subjects.get(i);
            String name = (String) movie.get("title");
            String rate = (String) movie.get("rate");
            movieMapper.add(name, rate);
        }
        System.out.println("OK");
    }
}
