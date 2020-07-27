package ba.genijalno.appium.clicktest;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class TestClickCounter {

    private static final Logger LOG = LoggerFactory.getLogger(TestClickCounter.class.getSimpleName());

    private AndroidDriver<MobileElement> driver;
    private final String apkPackageName = "ba.genijalno.android.appiumclicktest";
    private final By BTN_CLICK_ME = By.id(apkPackageName + ":id/btn_click_me");
    private final By BTN_CLOSE_ME = By.id(apkPackageName + ":id/btn_close_me");

    @BeforeSuite
    public void setup() throws IOException {
        setupAppium();
    }

    @AfterSuite
    public void tearDown() {
        uninstallApp();
    }

    @Test
    public void startClicking() {
        int maxCount = 1000;
        LOG.info("CLICKING STARTED {} TIMES", maxCount);
        for (int i = 0; i < maxCount; i++) {
            int currentCount = i + 1;
            click(BTN_CLICK_ME);
            LOG.info("CLICKED {} of {} TIMES", currentCount, maxCount);
            delay(1000);
            click(BTN_CLOSE_ME);
            LOG.info("CLOSED {} of {} TIMES", currentCount, maxCount);
//            assertText(currentCount);
        }
        delay(8000);
        assertText(maxCount);
    }

    private void delay(long millis) {
        try {
            //wait til app updates UI
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void assertText(int clickCount) {
        MobileElement btnClickMe = fluentWait(BTN_CLICK_ME);
        String expected = "CLICK ME! (" + clickCount + ")";
        String actual = btnClickMe.getText();
        LOG.info("Expected: {}", expected);
        LOG.info("Actual: {}", actual);
        assert expected.equalsIgnoreCase(actual);
    }

    public void setupAppium() throws IOException {
        String deviceUdid = "VS986dcbac072";
        String APK_FILE = new File("src/test/resources", "app-debug-appium-click-counter.apk").getAbsolutePath();
        URL appiumServerUrl = new URL("http://127.0.0.1:4723/wd/hub");
        LOG.info("SET CAPABILITIES, deviceUdid: {}, APK_FILE: {}", deviceUdid, APK_FILE);
        //SET CAPABILITIES FOR ANDROID EMULATOR
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.APP, APK_FILE);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability("appWaitActivity", "*");
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(AndroidMobileCapabilityType.ANDROID_INSTALL_TIMEOUT, 120000);
        capabilities.setCapability("adbExecTimeout", 200000);
        capabilities.setCapability(MobileCapabilityType.UDID, deviceUdid);
        capabilities.setCapability("clearDeviceLogsOnStart", true);
        driver = new AndroidDriver<>(appiumServerUrl, capabilities);
        LOG.info("RESET APP");
        driver.resetApp();
    }

    public void uninstallApp() {
        LOG.info("REMOVE APP: {}", apkPackageName);
        driver.removeApp(apkPackageName);
        LOG.info("DRIVER QUIT");
        driver.quit();
    }

    public MobileElement fluentWait(final By locator) {
        LOG.info("FLUENT WAIT: {}", locator);
        try {
            Wait<AndroidDriver<MobileElement>> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(60))
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(org.openqa.selenium.NoSuchElementException.class);
            return wait.until(
                    driver1 -> driver1.findElement(locator)
            );
        } catch (Exception e) {
            LOG.error("fluentWait failed: " + locator.toString(), e);
            throw e;
        }
    }

    protected MobileElement click(final By locator) {
        LOG.info("CLICK ELEMENT: {}", locator);
        MobileElement element = fluentWait(locator);
        try {
            element.click();
        } catch (Exception e) {
            if (e instanceof StaleElementReferenceException) {
                LOG.warn("CLICK ELEMENT (StaleElementReferenceException): {}", locator);
                // retry as suggested here: https://www.selenium.dev/exceptions/#stale_element_reference
                element = fluentWait(locator);
                try {
                    element.click();
                } catch (Exception ex) {
                    LOG.error("StaleElementReferenceException recovery failed: " + locator.toString(), ex);
                    throw ex;
                }
            } else {
                LOG.error("CLICK ELEMENT (Unexpected Exception): " + locator, e);
                throw e;
            }
        }
        return element;
    }
}
