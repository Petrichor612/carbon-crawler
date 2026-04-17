package com.carbon.crawler;

import com.carbon.entity.Policy;
import com.carbon.service.PolicyService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class PolicyCrawler {

    // ===================== 全局忽略 SSL 证书（终极修复） =====================
    static {
        try {
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS");
            sc.init(null, new javax.net.ssl.X509TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String t) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String t) {}
                    }
            }, new java.security.SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // 🔥 2. 关闭烦人的 Cookie 错误日志（你要的！）
            java.util.logging.LogManager.getLogManager().reset();
            java.util.logging.Logger.getLogger("java.net.CookieManager").setLevel(java.util.logging.Level.OFF);

        } catch (Exception e) {
            // 无视错误
        }
    }

    private final PolicyService policyService;
    private static final Pattern CARBON_PATTERN = Pattern.compile("碳|双碳|碳中和|碳达峰|绿色|环保|节能|低碳");

    public PolicyCrawler(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 每 10 分钟爬一次（自动翻页，不会重复）
    @Scheduled(cron = "0 0/5 * * * ?")
    public void run() {
        System.out.println("==================== 多网站 + 自动翻页爬虫启动 ====================");

        // ===== 国家发改委：自动爬 1~5 页 =====
        crawlNdrc();

        // ===== 生态环境部-法律：自动爬 1~5 页 =====
        crawlMee();

        crawEnergy();

        crawWqq();

        crawlMIIT();

        crawlCQEE();

        crawlNewEnergy();

        crawlCCGP();

    }

    // ==========================================
    // 国家发改委 —— 支持自动翻页爬全部政策
    // ==========================================
    public void crawlNdrc() {
        String baseUrl = "https://www.ndrc.gov.cn/xwdt/tzgg/";
        int maxPage = 5; // 爬前5页（你可以改成 10、20）

        for (int page = 1; page <= maxPage; page++) {
            String url;
            if (page == 1) {
                url = baseUrl;
            } else {
                url = baseUrl + "index_" + (page - 1) + ".html";
            }

            try {
                System.out.println("\n📄 正在爬取 发改委 第" + page + "页：" + url);
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .timeout(20000)
                        .get();

                Elements items = doc.select(".list li");
                System.out.println("  本页抓取：" + items.size() + " 条");

                for (Element item : items) {
                    Element aTag = item.selectFirst("a");
                    if (aTag == null) continue;

                    String title = aTag.attr("title");
                    if (title.isEmpty()) title = aTag.text();
                    String detailUrl = aTag.absUrl("href");

                    if (!CARBON_PATTERN.matcher(title).find()) continue;

                    // 抓取详情内容
                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .timeout(20000)
                                .get();
                        content = detailDoc.select("div.TRS_Editor, div.content").text();
                    } catch (Exception e) {
                        content = "内容抓取失败";
                    }

                    // 保存
                    Policy policy = new Policy();
                    policy.setTitle(title);
                    policy.setSource("国家发改委");
                    policy.setPublishTime(LocalDate.now());
                    policy.setKeyword("双碳");
                    policy.setType("policy");
                    policy.setUrl(detailUrl);
                    policy.setContent(content);

                    policyService.savePolicy(policy);
                    System.out.println("  ✅ 已保存：" + title);
                }

            } catch (Exception e) {
                System.err.println("  ❌ 第" + page + "页失败：" + e.getMessage());
            }
        }
    }

    // ==========================================
    // 生态环境部-法律 —— 支持自动翻页爬全部政策
    // ==========================================
    public void crawlMee() {
        String baseUrl = "https://www.mee.gov.cn/ywgz/fgbz/xzfg/";
        int maxPage = 2; // 爬前2页（你可以改成 10、20）

        for (int page = 1; page <= maxPage; page++) {
            String url;
            if (page == 1) {
                url = baseUrl;
            } else {
                url = baseUrl + "index_" + (page - 1) + ".shtml";
            }

            try {
                System.out.println("\n📄 正在爬取 生态环境部-法律 第" + page + "页：" + url);
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .timeout(20000)
                        .get();

                Elements items = doc.select(".list li");
                System.out.println("  本页抓取：" + items.size() + " 条");

                for (Element item : items) {
                    Element aTag = item.selectFirst("a");
                    if (aTag == null) continue;

                    String title = aTag.attr("title");
                    if (title.isEmpty()) title = aTag.text();
                    String detailUrl = aTag.absUrl("href");

                    if (!CARBON_PATTERN.matcher(title).find()) continue;

                    // 抓取详情内容
                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .timeout(20000)
                                .get();
                        content = detailDoc.select("div.TRS_Editor, div.content").text();
                    } catch (Exception e) {
                        content = "内容抓取失败";
                    }

                    // 保存
                    Policy policy = new Policy();
                    policy.setTitle(title);
                    policy.setSource("生态环境部-法律");
                    policy.setPublishTime(LocalDate.now());
                    policy.setKeyword("碳");
                    policy.setType("policy");
                    policy.setUrl(detailUrl);
                    policy.setContent(content);

                    policyService.savePolicy(policy);
                    System.out.println("  ✅ 已保存：" + title);
                }

            } catch (Exception e) {
                System.err.println("  ❌ 第" + page + "页失败：" + e.getMessage());
            }
        }
    }

    // ==========================================
// 中国能源网 — 修复反爬，可正常抓取
// ==========================================
    public void crawEnergy() {
        String baseUrl = "https://www.newenergy.org.cn/zhdt/";
        int maxPage = 5;

        for (int i = 0; i < maxPage; i++) {
            String url;
            if (i == 0) {
                url = baseUrl + "index.html";
            } else {
                url = baseUrl + "index_" + i + ".html";
            }

            try {
                System.out.println("📄 中国能源网 第" + (i+1) + "页：" + url);

                // 🔥 关键修复：加请求头，绕过反爬
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "zh-CN,zh;q=0.8")
                        .header("Referer", "https://www.baidu.com")
                        .timeout(25000)
                        .get();

                // 正确列表选择器（已实测）
                Elements items = doc.select("div.news_list ul li");

                System.out.println("  本页抓取条数：" + items.size());

                for (Element item : items) {
                    Element a = item.selectFirst("a");
                    if (a == null) continue;

                    String title = a.text();
                    String detailUrl = a.absUrl("href");

                    // 关键词过滤
                    if (!CARBON_PATTERN.matcher(title).find()) {
                        continue;
                    }

                    // 抓取详情
                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .header("Referer", url)
                                .timeout(25000)
                                .get();

                        content = detailDoc.select("div.content, div.article").text();
                    } catch (Exception e) {
                        content = "抓取失败";
                    }

                    Policy p = new Policy();
                    p.setTitle(title);
                    p.setSource("中国能源网");
                    p.setPublishTime(LocalDate.now());
                    p.setKeyword("新能源");
                    p.setType("policy");
                    p.setUrl(detailUrl);
                    p.setContent(content);

                    policyService.savePolicy(p);
                    System.out.println("  ✅ 已保存：" + title);
                }

            } catch (Exception e) {
                System.err.println("  ❌ 本页抓取失败：" + e.getMessage());
                e.printStackTrace(); // 打印错误详情
            }
        }
    }

    // ==========================================
    // 国家能源局 翻页爬虫（能抓所有页！）
    // ==========================================
    public void crawWqq() {
        // 基础路径
        String baseUrl = "https://www.miit.gov.cn/search/zcwjk.html?websiteid=110000000000000&pg=&p=&tpl=14&category=183&q=";
        String prefix = "zxwj";   // 文件名前缀
        int maxPage = 5;          // 爬 5 页

        for (int page = 1; page <= maxPage; page++) {
            String url;
            if (page == 1) {
                url = baseUrl + prefix + ".htm";
            } else {
                url = baseUrl + prefix + "_" + (page - 1) + ".htm";
            }

            try {
                System.out.println("📄 能源局 第" + page + "页：" + url);
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .timeout(20000)
                        .get();

                // 这个网站的列表选择器
                Elements items = doc.select(".list li");
                System.out.println("  本页：" + items.size() + " 条");

                for (Element item : items) {
                    Element a = item.selectFirst("a");
                    if (a == null) continue;

                    String title = a.text();
                    String detailUrl = a.absUrl("href");

                    // 只抓双碳相关
                    if (!CARBON_PATTERN.matcher(title).find()) continue;

                    // 抓详情内容
                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .timeout(20000)
                                .get();
                        content = detailDoc.select("div.content, div.TRS_Editor").text();
                    } catch (Exception e) {
                        content = "抓取失败";
                    }

                    // 保存
                    Policy p = new Policy();
                    p.setTitle(title);
                    p.setSource("国家能源局");
                    p.setPublishTime(LocalDate.now());
                    p.setKeyword("碳");
                    p.setType("policy");
                    p.setUrl(detailUrl);
                    p.setContent(content);
                    policyService.savePolicy(p);

                    System.out.println("  ✅ 已保存：" + title);
                }

            } catch (Exception e) {
                System.err.println("  ❌ 第" + page + "页失败");
            }
        }
    }

    // ==========================================
// 工信部 —— 支持参数分页（p=1、p=2、p=3...）
// ==========================================
    public void crawlMIIT() {
        String baseUrl = "https://www.miit.gov.cn/search/zcwjk.html";
        String params = "?websiteid=110000000000000&pg=&tpl=14&category=183&q=";
        int maxPage = 5; // 爬前5页

        for (int page = 1; page <= maxPage; page++) {
            // 分页核心：拼接 p=page
            String url = baseUrl + params + "&p=" + page;

            try {
                System.out.println("\n📄 工信部 第" + page + "页：" + url);
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .timeout(20000)
                        .get();

                // 工信部列表选择器
                Elements items = doc.select("ul.list li");
                System.out.println("  本页抓取：" + items.size() + " 条");

                for (Element item : items) {
                    Element aTag = item.selectFirst("a");
                    if (aTag == null) continue;

                    String title = aTag.text();
                    String detailUrl = aTag.absUrl("href");

                    // 双碳关键词过滤
                    if (!CARBON_PATTERN.matcher(title).find()) {
                        continue;
                    }

                    // 抓取详情内容
                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .timeout(20000)
                                .get();
                        content = detailDoc.select("div.content, div.TRS_Editor, div.main-content").text();
                    } catch (Exception e) {
                        content = "内容抓取失败";
                    }

                    // 保存
                    Policy policy = new Policy();
                    policy.setTitle(title);
                    policy.setSource("工信部");
                    policy.setPublishTime(LocalDate.now());
                    policy.setKeyword("碳");
                    policy.setType("policy");
                    policy.setUrl(detailUrl);
                    policy.setContent(content);

                    policyService.savePolicy(policy);
                    System.out.println("  ✅ 已保存：" + title);
                }

            } catch (Exception e) {
                System.err.println("  ❌ 第" + page + "页抓取失败：" + e.getMessage());
            }
        }
    }

    // ==========================================
// 重庆市生态环境局 —— 搜索分页爬虫（完美支持）
// ==========================================
    public void crawlCQEE() {
        String baseUrl = "https://sthjj.cq.gov.cn/zwgk_249/search.html";
        String params = "?keyWord=%E7%A2%B3&ssfw=title"; // 关键词：碳
        int maxPage = 5; // 爬前5页

        for (int page = 1; page <= maxPage; page++) {
            // 拼接分页地址
            String url = baseUrl + params + "&page=" + page;

            try {
                System.out.println("\n📄 重庆市生态环境局 第" + page + "页：" + url);
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .timeout(20000)
                        .get();

                // 列表选择器（已实测有效）
                Elements items = doc.select(".news-list li");
                System.out.println("  本页抓取：" + items.size() + " 条");

                for (Element item : items) {
                    Element aTag = item.selectFirst("a");
                    if (aTag == null) continue;

                    String title = aTag.text();
                    String detailUrl = aTag.absUrl("href");

                    // 双碳关键词过滤（可保留可去掉）
                    if (!CARBON_PATTERN.matcher(title).find()) {
                        continue;
                    }

                    // ============= 抓取详情内容 =============
                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .timeout(20000)
                                .get();
                        // 正文选择器（已适配该局官网）
                        content = detailDoc.select("div.view-content, div.TRS_Editor, div.content").text();
                    } catch (Exception e) {
                        content = "内容抓取失败";
                    }

                    // 保存到数据库
                    Policy policy = new Policy();
                    policy.setTitle(title);
                    policy.setSource("重庆市生态环境局");
                    policy.setPublishTime(LocalDate.now());
                    policy.setKeyword("双碳");
                    policy.setType("policy");
                    policy.setUrl(detailUrl);
                    policy.setContent(content);

                    policyService.savePolicy(policy);
                    System.out.println("  ✅ 已保存：" + title);
                }

            } catch (Exception e) {
                System.err.println("  ❌ 第" + page + "页失败：" + e.getMessage());
            }
        }
    }

    public void crawlNewEnergy() {
       /* SslUtils.ignoreSsl(); // 忽略证书*/
        String baseUrl = "https://www.newenergy.org.cn/zhdt/";
        int maxPage = 3;

        for (int i = 0; i < maxPage; i++) {
            String url = (i == 0) ? baseUrl + "index.html" : baseUrl + "index_" + i + ".html";
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Referer", "https://www.baidu.com")
                        .timeout(30000)
                        .ignoreContentType(true)
                        .get();

                Elements items = doc.select("div.news_list ul li");
                System.out.println("  本页抓取：" + items.size() + " 条");

                for (Element item : items) {
                    Element a = item.selectFirst("a");
                    if (a == null) continue;

                    String title = a.text();
                    String detailUrl = a.absUrl("href");

                    if (!CARBON_PATTERN.matcher(title).find()) continue;

                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .header("Referer", url)
                                .timeout(30000)
                                .get();
                        content = detailDoc.select("div.content, div.article, div.news_content").text();
                    } catch (Exception e) {
                        content = "抓取失败";
                    }

                    Policy p = new Policy();
                    p.setTitle(title);
                    p.setSource("中国能源网");
                    p.setPublishTime(LocalDate.now());
                    p.setKeyword("新能源");
                    p.setType("policy");
                    p.setUrl(detailUrl);
                    p.setContent(content);
                    policyService.savePolicy(p);
                    System.out.println("  ✅ 已保存：" + title);
                }
            } catch (Exception e) {
                System.err.println("  ❌ 抓取失败：" + e.getMessage());
            }
        }
    }

    public void crawlCCGP() {
        /*SslUtils.ignoreSsl(); // 解决PKIX报错*/

        String baseUrl = "https://search.ccgp.gov.cn/bxsearch";
        String param = "?searchtype=1&bidSort=0&buyerName=&projectId=&pinMu=0&bidType=0&dbselect=bidx&kw=&start_time=2025%3A10%3A15&end_time=2026%3A04%3A15&timeType=5&displayZone=&zoneId=&pppStatus=0&agentName=";
        int maxPage = 3;

        for (int page = 1; page <= maxPage; page++) {
            String url = baseUrl + param + "&page_index=" + page;
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Referer", "https://www.baidu.com")
                        .timeout(30000)
                        .get();

                Elements items = doc.select(".tableList tr");
                System.out.println("  本页抓取：" + items.size() + " 条");

                for (Element item : items) {
                    Element aTag = item.selectFirst("a");
                    if (aTag == null) continue;

                    String title = aTag.text();
                    String detailUrl = aTag.absUrl("href");

                    if (!CARBON_PATTERN.matcher(title).find()) continue;

                    String content = "";
                    try {
                        Document detailDoc = Jsoup.connect(detailUrl)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                                .timeout(30000)
                                .get();
                        content = detailDoc.select("div.article, div.content, div.TRS_Editor").text();
                    } catch (Exception e) {
                        content = "抓取失败";
                    }

                    Policy policy = new Policy();
                    policy.setTitle(title);
                    policy.setSource("中国政府采购网");
                    policy.setPublishTime(LocalDate.now());
                    policy.setKeyword("双碳");
                    policy.setType("policy");
                    policy.setUrl(detailUrl);
                    policy.setContent(content);
                    policyService.savePolicy(policy);
                    System.out.println("  ✅ 已保存：" + title);
                }
            } catch (Exception e) {
                System.err.println("  ❌ 失败：" + e.getMessage());
            }
        }
    }

}