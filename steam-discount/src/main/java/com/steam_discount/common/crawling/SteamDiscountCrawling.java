package com.steam_discount.common.crawling;


import com.steam_discount.discountList.entity.Discount;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SteamDiscountCrawling {

    private WebDriver driver;
    private final String url = "https://store.steampowered.com/search/?supportedlang=koreana&category1=998&specials=1&hidef2p=1&ndl=1";

    public List<Discount> getDiscountList() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-images");
        options.addArguments("--blink-settings=imagesEnabled=false");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);


        List<Discount> discountList = new ArrayList<>();

        try{
            driver.get(url);
            new Actions(driver).sendKeys(Keys.END).perform();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));
            Thread.sleep(2000);
            List<WebElement> list = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("search_result_row")));

            long count = 1;

            for(WebElement element : list){
                Discount discount = new Discount();

                discount.setId(count);
                discount.setLink(element.getAttribute("href"));
                discount.setName(element.findElement(By.className("title")).getText());
                discount.setImage(element.findElement(By.tagName("img")).getAttribute("src"));
                discount.setOriginPrice(element.findElement(By.className("discount_original_price")).getText());
                discount.setDiscountPrice(element.findElement(By.className("discount_final_price")).getText());
                discount.setDiscountPercent(element.findElement(By.className("discount_pct")).getText());

                discountList.add(discount);
                count++;

                if(count > 100)
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        driver.quit();

        return discountList;
    }
}
