import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.htmlcleaner.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class xpathTest {

    private static final String url = "https://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91?start=40&type=S";
    private static final String xpathTitle = ".//*[@id='subject_list']/ul/li//div[2]/h2/a";




    @Test
    public void cookie() throws IOException {

        WebClient webClient = StartWebClient();
        HtmlPage page = client.getPage("https://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91?start=40&type=S");
        Set<Cookie> cookies = client.getCookieManager().getCookies();
        if (cookies.size() <= 0) System.out.println("cookie 错误");
        List list = page.getByXPath(".//*[@id='subject_list']/ul/li/div[2]/div[1]");
        HtmlDivision link = (HtmlDivision) list.get(0);

        System.out.println(link.asText());
    }


    private static WebClient client = null;


    public WebClient StartWebClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
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


    @Test
    public void split() {
        String str = "劳伦斯・莱斯格、李旭 / 李旭 / 中信出版社 / 2004-10-1 / 30.00元 ";

        String[] list = str.split("/");
        System.out.println(list[list.length - 3].trim());

    }
}
