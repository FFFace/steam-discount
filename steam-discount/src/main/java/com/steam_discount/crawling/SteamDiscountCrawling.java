package com.steam_discount.crawling;


import com.steam_discount.discountList.entity.Discount;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class SteamDiscountCrawling {

    private WebDriver driver;
    private final String url = "https://store.steampowered.com/search/?supportedlang=koreana&category1=998&specials=1&hidef2p=1&ndl=1";

    private final String WEB_DRIVER_ID = "webdriver.chrome.driver";

//    @Value("${CHROME.DRIVER.PATH}")
    private String WEB_DRIVER_PATH="./chromedriver-win64/chromedriver.exe";

    public List<Discount> getDiscountList(){
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);

        List<Discount> discountList = new ArrayList<>();
        System.out.println("a");

        try{
            driver.get(url);
            Thread.sleep(2000);

            new Actions(driver).sendKeys(Keys.END).perform();
            Thread.sleep(2000);

            List<WebElement> list = driver.findElements(By.className("search_result_row"));

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
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            driver.close();
        }

        return discountList;
    }
}
