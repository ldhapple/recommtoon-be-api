package com.recommtoon.recommtoonapi.crawler;

import com.recommtoon.recommtoonapi.webtoon.entity.Days;
import com.recommtoon.recommtoonapi.webtoon.entity.Genre;
import com.recommtoon.recommtoonapi.webtoon.entity.Webtoon;
import com.recommtoon.recommtoonapi.webtoon.repository.WebtoonRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.recommtoon.recommtoonapi.crawler.CrawlingTargets.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CrawlerService {

    public static final String NAVER_WEBTOON_URL = "https://comic.naver.com/webtoon";
    public static final String SCROLL_TO_END_SCRIPT = "window.scrollTo(0, document.body.scrollHeight);";
    public static final int MON = 1;
    public static final int SUN = 8;
    public static final String NAVER_WEBTOON_TAB_NEW = "https://comic.naver.com/webtoon?tab=new";

    private final WebtoonRepository webtoonRepository;

    public void mainCrawling() {
        /*
            기존 웹툰들 크롤링 (연재 웹툰만)
            1. 요일별 웹툰 클릭
            2. 각 웹툰 클릭
            3. 데이터 수집
            4. 데이터 저장
            5. 뒤로 가기
            6. 반복 -> 월요웹툰 수집이 끝나면 화요일로 넘어가서 반복
            7. 일요웹툰의 데이터 수집이 끝나면 종료.
        */

        WebDriver driver = setupWebDriver();

        try {
            navigateToWebtoonMainPage(driver);
            crawlingWeeklyWebtoons(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void crawlingWeeklyWebtoons(WebDriver driver) {
        List<WebElement> elementsWithDays = driver.findElements(By.className(DAYS_TAB.getClassName()));

        for (int dayIdx = MON; dayIdx < SUN; dayIdx++) {
            elementsWithDays.get(dayIdx).click(); //요일 클릭
            scrollToEndAndWaitBody(driver);
            Days days = Days.values()[dayIdx - 1];
            crawlingWebtoons(driver, days);
            elementsWithDays = driver.findElements(By.className(DAYS_TAB.getClassName()));
        }
    }

    private void crawlingWebtoons(WebDriver driver, Days days) {

        for (int j = 0; j < driver.findElements(By.className(ITEM.getClassName())).size(); j++) {
            List<WebElement> webtoonElements = driver.findElements(By.className(ITEM.getClassName()));
            webtoonElements.get(j).click();
            waitBody(driver);

            String titleId = getTitleId(driver);

            if (titleId.isEmpty()) {
                //성인 웹툰인 경우 로그인창으로 넘어감. -> titleId를 구할 수 없음.
                driver.navigate().back();
                continue;
            }

            String imgUrl = getImgUrl(driver);
            String title = getTitle(driver);
            String author = getAuthor(driver);
            Set<Genre> genres = getGenres(driver);
            String story = getStory(driver);

            Webtoon webtoonData = Webtoon.createWebtoon(titleId, title, author, genres, days, story, imgUrl);
            webtoonRepository.save(webtoonData);

            driver.navigate().back();
        }
    }

    private static String getStory(WebDriver driver) {
        return driver.findElement(By.className(STORY.getClassName())).getText();
    }

    private static Set<Genre> getGenres(WebDriver driver) {
        Set<Genre> genres = new HashSet<>();
        List<WebElement> genreElements = driver.findElements(By.className(GENRES.getClassName()));

        for (WebElement genreElement : genreElements) {
            String genreKoreanName = genreElement.getText().replace("#", "");
            Genre existGenre = Genre.isExistGenre(genreKoreanName);

            if (existGenre != null) {
                genres.add(existGenre);
            }
        }
        return genres;
    }

    private static String getAuthor(WebDriver driver) {
        List<WebElement> authors = driver.findElements(By.className(AUTHOR.getClassName()));
        return authors.stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(", "));
    }

    private static String getTitle(WebDriver driver) {
        return driver.findElement(By.className(TITLE.getClassName())).getText();
    }

    private static String getImgUrl(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement imgElement = wait.until(ExpectedConditions.elementToBeClickable(By.className(THUMBNAIL.getClassName())));

        return imgElement.getAttribute(SRC.getClassName());
    }

    private static String getTitleId(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl();

        Pattern pattern = Pattern.compile("titleId=(\\d+)");
        Matcher matcher = pattern.matcher(currentUrl);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private static void navigateToWebtoonMainPage(WebDriver driver) {
        driver.get(NAVER_WEBTOON_URL);
        scrollToEndAndWaitBody(driver);
    }

    private static void scrollToEndAndWaitBody(WebDriver driver) {
        JavascriptExecutor je = (JavascriptExecutor) driver;
        je.executeScript(SCROLL_TO_END_SCRIPT);

        waitBody(driver);
    }

    private static void waitBody(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.tagName(BODY.getClassName())));
    }

    private static WebDriver setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }
}
