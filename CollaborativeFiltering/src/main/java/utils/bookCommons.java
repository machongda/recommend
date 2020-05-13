package utils;

import com.mada.pojo.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
 * 爬取豆瓣读书需要用到的公共方法
 *
 *
 * */
public class bookCommons {


    public static author authorGet(String authorUrl, CloseableHttpClient httpClient) throws IOException {
        int aId;
        String name;
        String sex = "";
        String birth = "";
        String area = "";
        String aImagePath = "";
        String aboutAuthor = "";

        aId = Integer.parseInt(authorUrl.substring(authorUrl.lastIndexOf("/", authorUrl.length() - 2) + 1, authorUrl.length() - 1));
        CloseableHttpResponse authorResponse;
        authorResponse = commonUtils.sendGet(authorUrl, httpClient);
        //    bookResponse=commonUtils.sendGet("https://book.douban.com/subject/1084336/",httpClient);

        Document docAuthorTemp = Jsoup.parse(EntityUtils.toString(authorResponse.getEntity(), "UTF-8"));
        //写入文件
        //writeStringToDisk.writeHtml(doc.toString(), "1");
        Element info = docAuthorTemp.getElementById("headline");//作者头部信息节点
        try {
            aImagePath = info.select("div.pic").get(0).getElementsByTag("img").get(0).attr("src");
        } catch (NullPointerException e) {
            aImagePath = "";
            System.out.println("找不到头像" + authorUrl);
        }

        name = docAuthorTemp.getElementById("content").getElementsByTag("h1").get(0).text();
        Elements infoEntityList = info.getElementsByTag("li");
        for (Element infoEntity : infoEntityList) {
            String infoEntityString = infoEntity.text();
            if (infoEntityString.contains("性别")) {
                sex = infoEntityString.split(":")[1];
            } else if (infoEntityString.contains("出生日期") || infoEntityString.contains("生卒日期")) {
                birth = infoEntityString.split(":")[1];
            } else if (infoEntityString.contains("出生地") || infoEntityString.contains("国家/地区")) {
                area = infoEntityString.split(":")[1];
            }
        }
        Element intro = docAuthorTemp.getElementById("intro");//作者简介信息节点
        if (intro.select("span[class='all hidden']").size() != 0) {
            aboutAuthor = Jsoup.clean(intro.select("span[class='all hidden']").get(0).html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        } else {
            aboutAuthor = Jsoup.clean(intro.select("div[class='bd']").get(0).html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        }

        author authorEntity = new author();
        authorEntity.setAId(aId);
        authorEntity.setName(name);
        authorEntity.setSex(sex);
        authorEntity.setBirth(birth);
        authorEntity.setArea(area);
        authorEntity.setAImagePath(aImagePath);
        authorEntity.setAboutAuthor(aboutAuthor);

//        System.out.println("aId:" + aId);
////        System.out.println("name:" + name);
////        System.out.println("sex:" + sex);
////        System.out.println("birth:" + birth);
////        System.out.println("area:" + area);
////        System.out.println("aImagePath:" + aImagePath);
////        System.out.println("aboutAuthor:" + aboutAuthor);
        return authorEntity;

    }


    public static book bookGet(String bookUrl, CloseableHttpClient httpClient) throws URISyntaxException, IOException, ScriptException {
        CloseableHttpResponse bookResponse, authorResponse;
        int bId;
        String isbn = "";
        String name = "";
        String coverPath = "";
        String authorName = "";
        String publishingHouse = "";
        String publishingYear = "";
        String labels = "";
        String briefIntroduction = "这本书还没有介绍！";
        List<String> authorUrlList = new ArrayList<>();
        List<String> recommonedUrlList = new ArrayList<>();
        float score;
        int pointNumber;
        bId = Integer.parseInt(bookUrl.substring(bookUrl.lastIndexOf("/", bookUrl.length() - 2) + 1, bookUrl.length() - 1));

        //bookUrl="https://book.douban.com/subject/1084336/";
        bookResponse = commonUtils.sendGet(bookUrl, httpClient);
        //    bookResponse=commonUtils.sendGet("https://book.douban.com/subject/1084336/",httpClient);

        Document docBookTemp = Jsoup.parse(EntityUtils.toString(bookResponse.getEntity(), "UTF-8"));
        String debug = docBookTemp.toString();
//        writeStringToDisk.writeHtml(debug, "book" + bId);

        try {
            score = Float.parseFloat(docBookTemp.select("#interest_sectl div.rating_self.clearfix strong").get(0).text().trim());
            pointNumber = Integer.parseInt(docBookTemp.select("#interest_sectl > div > div.rating_self.clearfix > div > div.rating_sum > span > a > span").get(0).text().trim());
        } catch (Exception e) {
            score = 0;
            pointNumber = 0;
//            System.out.println("书籍信息不足：" + bookUrl);
//            System.out.println(e);
//            return null;
        }


        Element mainpic = docBookTemp.getElementById("mainpic");//书籍图片信息节点
        Element info = docBookTemp.getElementById("info");//书籍信息节点
        coverPath = mainpic.child(0).attr("href");//书籍封面url;
        name = docBookTemp.select("#wrapper>h1").text();
        //处理作者信息
        //可能会出现多个作者，有的是作者的直接页面，有的不是直接的作何页面，而是作者的search，
        Elements authorUrls = info.getElementsByTag("a");//id为info的节点下第一个a标签必为作者标签，不过当链接中有search时不是直接的作者页面，需再处理，只有链接中含有author时才是直接的作者页面
        boolean flag = false;//记录是否搜索到作者链接
        URI base = new URI(bookUrl);//处理链接中的相对路径
        for (Element authorE : authorUrls) {


            String authorUrl = authorE.attr("href");
            if (authorUrl.contains("author"))//直接是作者页面
            {
                flag = true;
                authorUrlList.add(base.resolve(authorUrl).toString());
            } else if (false && authorUrl.contains("search"))//得到搜索页面
            {

                authorResponse = commonUtils.sendGet(base.resolve(authorUrl).toString(), null);

                if (authorResponse.getStatusLine().getStatusCode() == 302) {

                    String redirectUrl = authorResponse.getHeaders("Location")[0].getValue().replace(" ", "");
                    authorResponse = commonUtils.sendGet(redirectUrl, null);
                }

                Document authorSearchTemp = Jsoup.parse(EntityUtils.toString(authorResponse.getEntity(), "UTF-8"));
                //解密
                Elements scripts = authorSearchTemp.getElementsByTag("script");
                String windowDATA = "";
                for (Element script : scripts) {


                    if (script.toString().contains("window.__DATA__")) {
                        windowDATA = script.toString().replaceAll("\\s+", "").replaceAll("<scripttype=\"text/javascript\">window.__DATA__=", "").replaceAll("window.__USER__=\\{\\}</script>", "");
                    }
                }


                String[] searchResultArray = {};
                try {
                    List<NameValuePair> nvps = new ArrayList<>();
                    nvps.add(new BasicNameValuePair("r", windowDATA));
                    CloseableHttpResponse closeableHttpResponse = commonUtils.sendPost("http://127.0.0.1:8081/ecryptData", nvps, httpClient);
                    String listString = EntityUtils.toString(closeableHttpResponse.getEntity());
                    searchResultArray = listString.replace("[", "").replace("]", "").replace("\"", "").split(",");


                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("搜索内容解密出错，请查看！");
                    System.out.println("URL:" + base.resolve(authorUrl).toString());
                }

                for (String s : searchResultArray) {
                    if (s.contains("author")) {
                        authorUrlList.add(s);
//                        System.out.println("搜索到作者：" + s);
                        flag = true;
                    }
                }


            }

        }
        if (flag == false) {
            System.out.println("书籍：" + bookUrl + "未搜索到作者");
        }
        //处理其他信息，书籍名，作者名，isbn等等
        String infoText = Jsoup.clean(info.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)).trim().replaceAll(" +", "").replaceAll("&nbsp;", "").replaceAll(":\n", ":");//获取保留空格的文本
        String[] infoArray = infoText.split("\\s+");
        List<String> infoList = Arrays.asList(infoArray);
        infoList = new ArrayList(infoList);
        if (infoList.get(0).split(":")[0].contains("作者")) {
            authorName = infoList.get(0).split(":")[1];
            infoList.remove(0);
        }
        if (infoList.get(infoList.size() - 1).split(":")[0].contains("ISBN")) {
            isbn = infoList.get(infoList.size() - 1).split(":")[1];
            infoList.remove(infoList.size() - 1);
        }

        for (String s : infoList) {
            if (s.contains(":")) {
                String[] split = s.split(":");
                if (split[0].equals("出版社")) {
                    publishingHouse = split[1];
                } else if (split[0].equals("出版年")) {
                    publishingYear = split[1];


                }
            }

        }


        if (docBookTemp.select("span[class='all hidden']").size() != 0) {
            briefIntroduction = Jsoup.clean(docBookTemp.select("span[class='all hidden'] div.intro").get(0).html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        } else {

            try {
                briefIntroduction = Jsoup.clean(docBookTemp.select("#link-report div[class='intro']").get(0).html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

            } catch (Exception e) {//没有找到介绍


            }
        }


        Elements dbTagsSection = docBookTemp.getElementById("db-tags-section").select("div.indent").get(0).select("span");//标签
        labels = dbTagsSection.get(0).text();
        for (int i = 1; i < dbTagsSection.size() - 1; i++) {
            labels += ",";
            labels += dbTagsSection.get(i).text();
        }
        labels += dbTagsSection.get(dbTagsSection.size() - 1).text();
        Elements dbRecSection = null;
        try {
            dbRecSection = docBookTemp.getElementById("db-rec-section").select("dl:not(.clear)");//底部图书关联推介,带有calss=clear的为清除浮动节点，内容为空
            for (Element recommenedBookEntity : dbRecSection) {
                recommenedBookEntity.getElementsByTag("a");
                //对关联书籍操作
                String recommonedUrl = recommenedBookEntity.getElementsByTag("a").get(0).attr("href");
                recommonedUrlList.add(recommonedUrl);
            }
        } catch (NullPointerException e) {
        }


        book bookEntity = new book();
        bookEntity.setBId(bId);
        bookEntity.setIsbn(isbn);
        bookEntity.setName(name);
        bookEntity.setCoverPath(coverPath);
        bookEntity.setAuthorName(authorName);
        bookEntity.setPublishingHouse(publishingHouse);
        bookEntity.setPublishingYear(publishingYear);
        bookEntity.setLabels(labels);
        bookEntity.setBriefIntroduction(briefIntroduction);
        bookEntity.setScore(score);
        bookEntity.setPointNumber(pointNumber);
        bookEntity.setAuthorUrlList(authorUrlList);
        bookEntity.setRecommonedUrlList(recommonedUrlList);
//        System.out.println("bId:" + bId);
//        System.out.println("isbn:" + isbn);
//        System.out.println("name:" + name);
//        System.out.println("coverPath:" + coverPath);
//        System.out.println("authorName:" + authorName);
//        System.out.println("publishingHouse:" + publishingHouse);
//        System.out.println("publishingYear:" + publishingYear);
//        System.out.println("labels:" + labels);
//        System.out.println("briefIntroduction:" + briefIntroduction);
//        System.out.println("score:" + score);

        return bookEntity;
        //Elements bookUrlList=docBookTemp.select("ul[class='subject-list']").get(0).select("li");


    }

    public static List<comment> shortCommentsGet(int bookId, CloseableHttpClient httpClient) throws IOException {
        List<comment> commentList = new ArrayList<>();
        int bId = bookId;
        String nickname = "";
        String content = "";
        CloseableHttpResponse commontsResponse;
        String shortCommentsUrl = "https://book.douban.com/subject/" + bookId + "/comments/";
        commontsResponse = commonUtils.sendGet(shortCommentsUrl, httpClient);
        Document commentsBookTemp = Jsoup.parse(EntityUtils.toString(commontsResponse.getEntity(), "UTF-8"));
        //写入文件
        Elements comments = commentsBookTemp.getElementById("comments").getElementsByTag("li");
        if (comments.get(0).text().equals("还没人写过短评呢"))
            return null;
        for (Element comment : comments) {
            nickname = comment.select("h3 span.comment-info a").get(0).text();
            content = comment.select("p.comment-content span.short").get(0).text();
            comment commentsEntity = new comment();
            commentsEntity.setBId(bookId);
            commentsEntity.setNickname(nickname);
            commentsEntity.setContent(content);
            commentList.add(commentsEntity);

            // System.out.println(nickname + ":" + content);
        }
        return commentList;

    }

    public static user userGet(String userUrl, CloseableHttpClient httpClient) throws IOException {
        //传入的httpcclient是已经登录的
        String image_path = "https://img3.doubanio.com/icon/ul1814372-12.jpg";
        String nickname = "匿名网友";
        String sex = "";
        Date birth = new Date();
        String password = "netUser";
        String lebels = "";
        CloseableHttpResponse userResponse;
        List<readingRecord> readingRecords = new ArrayList<>();
        int uId = Integer.parseInt(userUrl.substring(userUrl.lastIndexOf("/", userUrl.length() - 2) + 1, userUrl.length() - 1));


//        String userUrl = "https://www.douban.com/people/" + userId + "/";
        userResponse = commonUtils.sendGet(userUrl, httpClient);
        Document commentsUserTemp = Jsoup.parse(EntityUtils.toString(userResponse.getEntity(), "UTF-8"));
//       writeStringToDisk.writeHtml(commentsUserTemp.toString(), "user"+uId);
        try {
            nickname = commentsUserTemp.select("#db-usr-profile div.info h1").get(0).text();
        } catch (Exception e) {

        }
        try {
            image_path = commentsUserTemp.select("#profile img").get(0).attr("src");

        } catch (Exception e) {


        }
        for (int i = 0; i < 400; i += 15) {//获取阅读记录，最多获取400条
            userResponse = commonUtils.sendGet("https://book.douban.com/people/" + uId + "/collect?start=" + i + "&sort=time&rating=all&filter=all&mode=grid", httpClient);
            commentsUserTemp = Jsoup.parse(EntityUtils.toString(userResponse.getEntity(), "UTF-8"));
            Elements bookUrlList = commentsUserTemp.select("#content ul li.subject-item");
            if (bookUrlList != null)
                for (Element bookUrl : bookUrlList) {
                    String bookUrlString = bookUrl.select("div.pic a").attr("href");
                    Element element = bookUrl.select("div.info div.short-note div span").get(0);
                    String aClass = element.attr("class");
                    int bId = Integer.parseInt(bookUrlString.substring(bookUrlString.lastIndexOf("/", bookUrlString.length() - 2) + 1, bookUrlString.length() - 1));
                    int score=6;
                    switch(aClass){
                        case "rating5-t" :
                            score=10;
                            break;
                        case "rating4-t" :
                            score=8;
                            break;
                            case "rating3-t" :
                            score=6;
                            break;
                            case "rating2-t" :
                            score=4;
                            break;
                            case "rating1-t" :
                            score=2;
                            break;

                    }


                    readingRecord readingRcordEntity = new readingRecord();
                    readingRcordEntity.setBId(bId);
                    readingRcordEntity.setUId(String.valueOf(uId));
                    readingRcordEntity.setScore(score);
                    readingRecords.add(readingRcordEntity);
                }
            else
                break;
        }
        user userEntity = new user();
        userEntity.setUId(String.valueOf(uId));
        userEntity.setImagePath(image_path);
        userEntity.setNickname(nickname);
        userEntity.setSex(sex);
        userEntity.setBirth(birth);
        userEntity.setPassword(password);
        userEntity.setLebels(lebels);
        userEntity.setReadingRecordList(readingRecords);
//        System.out.println("nickname:" + nickname);
//        System.out.println("image_path:" + image_path);
        return userEntity;

    }


}
