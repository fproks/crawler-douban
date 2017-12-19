import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by fprok on 2017/12/19.
 */

public class xpathTest {

    private static final String url = "https://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91?start=40&type=S";
    private static final String xpathTitle = ".//*[@id='subject_list']/ul/li//div[2]/h2/a";


    @Test
    public void cookie() throws IOException {

        WebClient webClient = startWebClient();
        HtmlPage page = client.getPage("https://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91?start=40&type=S");
        Set<Cookie> cookies = client.getCookieManager().getCookies();
        if (cookies.size() <= 0) System.out.println("cookie 错误");
        List list = page.getByXPath(".//*[@id='subject_list']/ul/li/div[2]/div[1]");
        HtmlDivision link = (HtmlDivision) list.get(0);

        System.out.println(link.asText());
    }


    private static WebClient client = null;


    public WebClient startWebClient() {
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
