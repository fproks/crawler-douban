package cn.linhos.Crawler;

import org.junit.Test;

/**
 * Created by fprok on 2017/12/19.
 */
public class CrawlingTest {
    /**
     * 爬虫测试方法
     *
     * @throws Exception
     */
    @Test
    public void startToCrawling() throws Exception {
        Crawling crawling = new Crawling();
        crawling.startToCrawling();
        crawling.close();
    }
}
