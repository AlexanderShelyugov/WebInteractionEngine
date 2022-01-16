package ru.gobetter.webinteraction.selenium;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.testcontainers.containers.Network.newNetwork;

@Slf4j
public class SeleniumDriverFactory {
    private static final String HOST_ROOT = "/home/alexander/programming/frameworks/webdrivers";
    private static final String HOST_ADDONS = HOST_ROOT + "/addons/";
    private static final String CONTAINER_ADDONS = "/etc/newsreader/addons";

    public static WebDriver createNewContainer() {
        val profile = new FirefoxProfile();
        profile.setPreference("permissions.default.image", 2);
        profile.setPreference("network.dns.disablePrefetchFromHTTPS", false);
        profile.setPreference("network.predictor.enable-prefetch", false);
        profile.addExtension(new File(CONTAINER_ADDONS + File.separator + "decentraleyes.xpi"));
        profile.addExtension(new File(CONTAINER_ADDONS + File.separator + "uBlock.xpi"));
//        profile.addExtension(new File(CONTAINER_ADDONS));
        val options = new FirefoxOptions()
            .setProfile(profile);
        val chromeOptions = new ChromeOptions()
//            .addExtensions(new File(HOST_ADDONS + File.separator + "uBlock.zip"))
            ;

        val container = new BrowserWebDriverContainer<>()
            .withCapabilities(chromeOptions)
            .withNetwork(newNetwork())
            .withFileSystemBind(HOST_ADDONS, CONTAINER_ADDONS, BindMode.READ_WRITE)
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
//            .withLogConsumer(new Slf4jLogConsumer(log))
            ;
        container.start();
        return container.getWebDriver();
    }

    public static WebDriver createNewFirefoxDriver(String executablePath, boolean loadImages) {
        val profile = new FirefoxProfile();
        if (!loadImages) {
            profile.setPreference("permissions.default.image", 2);
        }
        profile.setPreference("network.dns.disablePrefetchFromHTTPS", false);
        profile.setPreference("network.predictor.enable-prefetch", false);
        val extensions = Stream.of(
                "decentraleyes",
                "uBlock",
                "cookies"
            )
            .map(addonName -> HOST_ADDONS + File.separator + addonName + ".xpi")
            .map(File::new)
            .collect(toList());

        extensions.forEach(profile::addExtension);
        val options = new FirefoxOptions().setProfile(profile);
        System.setProperty("webdriver.gecko.driver", executablePath);
        val driver = new FirefoxDriver(options);
        extensions.stream()
            .map(File::toPath)
            .forEach(driver::installExtension);
        driver.manage().window().maximize();
        return driver;
    }
}
