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

@Service
@Transactional
@RequiredArgsConstructor
public class CrawlerService {

    private final WebtoonRepository webtoonRepository;

    public void mainCrawling() {
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://comic.naver.com/webtoon");

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

            getBodyTag(driver);
            List<WebElement> elementsWithDays = driver.findElements(By.className("SubNavigationBar__link--PXX5B"));

            for (int i = 1; i < 8; i++) {
                elementsWithDays.get(i).click(); //요일 클릭
                getBodyTag(driver);
                Days days = Days.values()[i-1];
                Thread.sleep(2000);

                for (int j = 0; j < driver.findElements(By.className("item")).size(); j++) {
                    List<WebElement> dayWebtoons = driver.findElements(By.className("item"));
                    dayWebtoons.get(j).click();
                    getBodyTagWithoutScroll(driver);

                    String titleId = getTitleId(driver);

                    if (titleId.isEmpty()) {
                        //성인 웹툰인 경우 로그인창으로 넘어감. -> titleId를 구할 수 없음.
                        driver.navigate().back();
                        elementsWithDays = driver.findElements(By.className("SubNavigationBar__link--PXX5B"));
                        continue;
                    }

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement imgElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("Poster__image--d9XTI")));

                    String imgUrl = imgElement.getAttribute("src");

                    String title = driver.findElement(By.className("EpisodeListInfo__title--mYLjC")).getText();

                    List<WebElement> authors = driver.findElements(By.className("ContentMetaInfo__link--xTtO6"));
                    String author = authors.stream()
                            .map(WebElement::getText)
                            .collect(Collectors.joining(", "));

                    List<WebElement> genreElements = driver.findElements(By.className("TagGroup__tag--xu0OH"));
                    Set<Genre> genres = new HashSet<>();
                    for (WebElement genreElement : genreElements) {
                        String genreKoreanName = genreElement.getText().replace("#", "");
                        Genre existGenre = Genre.isExistGenre(genreKoreanName);

                        if (existGenre != null) {
                            genres.add(existGenre);
                        }
                    }

                    String content = driver.findElement(By.className("EpisodeListInfo__summary--Jd1WG")).getText();

                    Webtoon webtoonData = Webtoon.createWebtoon(titleId, title, author, genres, days, content, imgUrl);
                    webtoonRepository.save(webtoonData);
                    driver.navigate().back();
                }

                elementsWithDays = driver.findElements(By.className("SubNavigationBar__link--PXX5B"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static String getTitleId(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl();

        Pattern pattern = Pattern.compile("titleId=(\\d+)");
        Matcher matcher = pattern.matcher(currentUrl);

        String titleId = "";

        if (matcher.find()) {
            titleId = matcher.group(1);
        }

        return titleId;
    }

    private static void getBodyTag(WebDriver driver) {
        JavascriptExecutor je = (JavascriptExecutor) driver;
        je.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
    }

    private static void getBodyTagWithoutScroll(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
    }

    public void newCrawling() {
        /*
            신작 웹툰 크롤링
            1. 각 웹툰 클릭
            2. 데이터 수집
            3. 데이터 업데이트
            4. 뒤로 가기
            5. 반복 -> 신규웹툰 데이터 수집이 끝나면 종료.
        */
    }
}
