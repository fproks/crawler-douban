package cn.linhos.Crawler;


import cn.linhos.Util.Utils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fprok on 2017/12/19.
 */

/**
 * 使用HtmlUnit 对指定网页进行爬取
 * 使用xpath 获取指定数据
 * 三个线程同时爬取
 * 同时写入到一个Map中
 *
 */
public class Crawling {


    private static WebClient client = null;

    private static final String BOOK_INFO_XPATH = ".//*[@id='subject_list']/ul/li/div[2]/div[1]";  //图书信息的xpath 路径
    private static final String BOOK_NAME_XPATH = ".//*[@id='subject_list']/ul/li/div[2]/h2/a";   //图像名称xpath 路径
    private static final String SOURCE_XPATH = ".//*[@id='subject_list']/ul/li/div[2]/div[2]/span[2]";  //评分xpath路径
    private static final String EVALUATION_NUMBER = ".//*[@id='subject_list']/ul/li/div[2]/div[2]/span[3]";  // 评论数路径
   // private static final Pattern PublisherPattern = Pattern.compile("(?<=/ )((.*出版社.*)|(.*公司))(?=/)");
    private static final Pattern EVALUATION_NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Object MAP_LOCK = new Object();

    private static Map<String, Data> dataMap = new HashMap<>(10000);


    /**
     * 单例的方式启动WebClient，防止多次启动
     * @return
     */
    private WebClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new WebClient(BrowserVersion.CHROME);
                    client = new WebClient(BrowserVersion.CHROME);
                    client.getCookieManager().setCookiesEnabled(true);
                    client.getOptions().setThrowExceptionOnFailingStatusCode(false);
                    client.getOptions().setThrowExceptionOnScriptError(false);
                    client.getOptions().setCssEnabled(false);
                    client.getOptions().setUseInsecureSSL(true);
                    client.getOptions().setJavaScriptEnabled(false);
                    client.getOptions().setTimeout(600000);
                }
            }
        }
        return client;
    }

    /**
     * 清空存储数据，关闭WebClient
     */
    public  void close(){
        if(client!=null){
            synchronized (this){
                if(client!=null) {
                    client.close();
                    client=null;
                }
            }
        }
        synchronized (MAP_LOCK){
            dataMap.clear();
        }
    }


    /**
     * 根据制定的url获取页面
     * @param url
     * @return
     */
    public HtmlPage getPageWithDataUrl(String url) {
        if (!Utils.isUrl(url)) return null;
        WebClient client = getClient();
        HtmlPage page;
        synchronized (this) {
            try {
                page = client.getPage(url);
            } catch (IOException e) {
                return null;

            }
        }
        return page;
    }


    /**
     * xpath获取页面中的数据并填入hashMap中，
     * 防止一本书多次出现
     * @param page 获取到的页面内容
     */
    public void getDataToList(HtmlPage page) {
        if (page == null) return;
        List<?> bookNameList = page.getByXPath(BOOK_NAME_XPATH);
        List<?> bookInfoList = page.getByXPath(BOOK_INFO_XPATH);
        List<?> bookSourceList = page.getByXPath(SOURCE_XPATH);
        List<?> EvaluationNumberList = page.getByXPath(EVALUATION_NUMBER);
        for (int i = 0; i < bookNameList.size(); i++) {
            Data data = new Data();
            data.setBookName(((HtmlAnchor) bookNameList.get(i)).asText()); //书名
            HtmlDivision divTmp = (HtmlDivision) bookInfoList.get(i);
            getInfoWithSplit(data, divTmp.asText()); //作者，出版社
            String source = ((HtmlSpan) bookSourceList.get(i)).asText().trim();
            data.setSource(Float.parseFloat(source));
            HtmlSpan evaluateTmp = (HtmlSpan) EvaluationNumberList.get(i);
            getEvaluationNumber(data, evaluateTmp);
            if (data.getEvaluationNumber() > 1000) {
                synchronized (MAP_LOCK) {
                    if (!dataMap.containsKey(data.getBookName()))
                        dataMap.put(data.getBookName(), data);

                }
            }
        }
    }


    /**
     * 获取作者和出版社信息
     *
     * @param data
     * @param info
     */
    private void getInfoWithSplit(Data data, String info) {
        // getPublisher(data, info);
        String[] infoList = info.split("/");
        data.setAuthor(infoList[0].trim());
        data.setPublishers(infoList[infoList.length - 3]);
    }


    /**
     * 获评价人数
     * @param data  待填充的数据
     * @param span  存储评价人数的span块
     */
    private void getEvaluationNumber(Data data, HtmlSpan span) {
        String tmp = span.asText();
        Matcher matcher = EVALUATION_NUMBER_PATTERN.matcher(tmp);
        if (matcher.find())
            data.setEvaluationNumber(Integer.parseInt(matcher.group()));
        else
            data.setEvaluationNumber(0);
    }

    private void readDataWithBaseUrl(String baseUrl) {
        // String internet ="https://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91?start=";
        //对url 进行拼接修改，查找每个链接的排名最靠前的120本书
        for (int i = 0; i < 6; i++) {
            String tmpUrl = baseUrl + Integer.toString(i * 20) + "&type=S";
            HtmlPage page = getPageWithDataUrl(tmpUrl);
            getDataToList(page);
            try {
                //防止爬取过快被屏蔽
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
    }


    /**
     * 起始函数
     * 对互联网，编程，算法三个方面的书进行爬取
     *
     * @throws Exception
     */
    public void startToCrawling() throws Exception {
        String internetBaseUrl = "https://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91?start=";
        String programingBaseUrl = "https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?start=";
        String algorithmBaseUrl = "https://book.douban.com/tag/%E7%AE%97%E6%B3%95?start=";
        //三个字符串对应三个爬取主链接
        //线程池，三个线程同时爬取
        ExecutorService ThreadPool = Executors.newFixedThreadPool(3);
        ThreadPool.execute(() -> readDataWithBaseUrl(internetBaseUrl));
        ThreadPool.execute(() -> readDataWithBaseUrl(programingBaseUrl));
        ThreadPool.execute(() -> readDataWithBaseUrl(algorithmBaseUrl));
        ThreadPool.shutdown();
        boolean finish;
        do {
            finish = ThreadPool.awaitTermination(2, TimeUnit.SECONDS);
        } while (!finish);

        //对爬取数据进行提取，排序
        ArrayList<Data> dataList = new ArrayList<>(dataMap.values());
        dataList.sort((Data data1, Data data2) -> Integer.compare(data2.getEvaluationNumber(), data1.getEvaluationNumber()));
        //存储为xls 文件
        ExcelWrite excelWrite = new ExcelWrite("result.xls", "crawling", new String[]{"序号", "书名", "评分", "评价人数", "作者", "出版社"});
        excelWrite.createExcel();
        excelWrite.writeToExcel(dataList);
        System.out.println("FINISH");
    }


}


