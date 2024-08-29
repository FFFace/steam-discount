package com.steam_discount.crawling;


import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;

public class SteamDiscountCrawling {

    private WebDriver driver;
    private final String url = "https://store.steampowered.com/search/?supportedlang=koreana&category1=998&specials=1&hidef2p=1&ndl=1";

    private final String WEB_DRIVER_ID = "webdriver.chrome.driver";

//    @Value("${CHROME.DRIVER.PATH}")
    private String WEB_DRIVER_PATH="./chromedriver-win64/chromedriver.exe";

    public SteamDiscountCrawling(){
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
    }

    public void crawling(){
        try{
            driver.get(url);
            Thread.sleep(2000);

            new Actions(driver).sendKeys(Keys.END).perform();
            Thread.sleep(2000);

            List<WebElement> list = driver.findElements(By.className("search_result_row"));

            for(WebElement element : list){
                System.out.println(element.getAttribute("href"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
