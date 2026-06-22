package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }

        return extent;
    }

    private static void createInstance() {
        // 1) Clean Reports folder
        Path reportsDir = Path.of("Reports");

        try {
            if (Files.exists(reportsDir)) {
                try (Stream<Path> walk = Files.walk(reportsDir)) {
                    walk.sorted(Comparator.reverseOrder())
                            .forEach(path -> {
                                File file = path.toFile();
                                if (!file.delete()) {
                                    System.err.println("Could not delete: " + file.getAbsolutePath());
                                }
                            });
                }
            }

            Files.createDirectories(reportsDir);
            Files.createDirectories(Path.of("Reports/screenshots"));

        } catch (IOException e) {
            System.err.println("Failed to clean Reports folder: " + e.getMessage());
        }

        // 2) Create Spark reporter
        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/TestReport.html");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Platform", "Android");
        extent.setSystemInfo("Framework", "JUnit 5");
        extent.setSystemInfo("Automation", "Appium");

        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("Appium Test Execution");
    }
}
