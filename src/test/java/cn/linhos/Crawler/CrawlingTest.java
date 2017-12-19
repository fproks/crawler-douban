package cn.linhos.Crawler;

import org.junit.Test;

public class CrawlingTest {
    /**
     * 爬虫测试方法
     * @throws Exception
     */
    @Test
    public void StartToCrawling() throws Exception {
        Crawling crawling =new Crawling();
        crawling.startToCrawling();
        crawling.Close();
    }
}
