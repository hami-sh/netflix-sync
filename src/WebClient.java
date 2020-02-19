import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;

public class WebClient {
    public static void main(String[] args) throws Exception {
        File file = new File("user/details.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String username = reader.readLine();
        String password = reader.readLine();

        System.out.println(username + " " + password);

        WebDriver driver = new ChromeDriver();
        driver.get("http://netflix.com");

        Thread.sleep(1000);

        WebElement login = driver.findElement(By.xpath("//*[@id=\"appMountPoint\"]/div/div/div" +
                "/div/div/div[1]/div/a"));
        login.click();

        Thread.sleep(1000);

        WebElement user = driver.findElement(By.xpath("//*[@id=\"id_userLoginId\"]"));
        user.sendKeys(username);
        WebElement pass = driver.findElement(By.xpath("//*[@id=\"id_password\"]"));
        pass.sendKeys(password);

        WebElement signin = driver.findElement(By.xpath("//*[@id=\"appMountPoint\"]/div/div[3" +
                "]/div/div/div[1]/form/button"));
        signin.click();

        // begin communication
        String[] input = {""};
        Peer.main(input);
    }
}
