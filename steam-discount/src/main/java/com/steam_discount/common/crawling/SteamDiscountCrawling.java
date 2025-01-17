package com.steam_discount.common.crawling;


import com.steam_discount.discountList.entity.Discount;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SteamDiscountCrawling {

    private WebDriver driver;
    private final String url = "https://store.steampowered.com/search/?supportedlang=koreana&category1=998&specials=1&hidef2p=1&ndl=1";

    @Scheduled(cron = "0 5 10 * * *", zone = "America/Los_Angeles")
    public List<Discount> getDiscountList() {
        log.info("--- Crawling Steam Discount Start ---");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-images");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--lang=ko_KR");

        options.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        options.addArguments("--accept-language=ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        List<Discount> discountList = new ArrayList<>();

        try{
            driver.get(url);
            new Actions(driver).sendKeys(Keys.END).perform();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Thread.sleep(2000);
            List<WebElement> list = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("search_result_row")));

            long count = 1;

            for(WebElement element : list){
                Discount discount = new Discount();

                discount.setId(count);
                discount.setLink(element.getAttribute("href"));
                discount.setName(element.findElement(By.className("title")).getText());
                discount.setImage(element.findElement(By.tagName("img")).getAttribute("src"));

                try{
                    WebElement originPrice = element.findElement(By.className("discount_original_price"));
                    discount.setOriginPrice(originPrice.getText());
                } catch (Exception e){
                    discount.setOriginPrice("");
                }

                try{
                    WebElement discountPrice = element.findElement(By.className("discount_final_price"));
                    discount.setDiscountPrice(discountPrice.getText());
                } catch (Exception e){
                    discount.setDiscountPrice("");
                }

                try{
                    WebElement discountPct = element.findElement(By.className("discount_pct"));
                    discount.setDiscountPercent(discountPct.getText());
                } catch (Exception e){
                    discount.setDiscountPercent("");
                }

                log.info("--- Crawling Steam Discount Update {} ---", discount.getName());
                discountList.add(discount);
                count++;

                if(count > 100)
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        driver.quit();

        log.info("--- Crawling Steam Discount End ---");
        return discountList;
    }
}