package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExtentMerger {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: ExtentMerger <inputDir> <outputFile>");
            return;
        }

        merge(args[0], args[1]);
    }

    public static void merge(String inputDir, String outputFile) {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(outputFile);
        extent.attachReporter(spark);

        File folder = new File(inputDir);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".html"));

        if (files == null || files.length == 0) {
            System.out.println("No HTML reports found in: " + inputDir);
            return;
        }

        for (File file : files) {
            String suiteName = file.getName().replace(".html", "");
            ExtentTest suiteSection = extent.createTest(suiteName);

            try {
                String html = Files.readString(file.toPath(), StandardCharsets.UTF_8);

                // Extract JSON inside <script id="extent-json">
                String jsonStart = "<script id=\"extent-json\" type=\"application/json\">";
                String jsonEnd = "</script>";

                int start = html.indexOf(jsonStart);
                int end = html.indexOf(jsonEnd, start);

                if (start == -1 || end == -1) {
                    suiteSection.warning("Could not find JSON in: " + file.getName());
                    continue;
                }

                String json = html.substring(start + jsonStart.length(), end).trim();

                JSONObject root = new JSONObject(json);
                JSONArray tests = root.getJSONObject("report").getJSONArray("tests");

                for (int i = 0; i < tests.length(); i++) {
                    JSONObject t = tests.getJSONObject(i);

                    String testName = t.getString("name");
                    String statusText = t.getString("status").toLowerCase();

                    Status status = Status.PASS;
                    if (statusText.contains("fail")) status = Status.FAIL;
                    if (statusText.contains("skip")) status = Status.SKIP;

                    ExtentTest mergedTest = suiteSection.createNode(testName).log(status, "Merged from " + file.getName());

                    if (t.has("exceptions")) {
                        JSONArray exceptions = t.getJSONArray("exceptions");
                        if (!exceptions.isEmpty()) {
                            JSONObject ex = exceptions.getJSONObject(0);
                            mergedTest.log(Status.FAIL, ex.getString("stacktrace"));
                        }
                    }
                }

            } catch (Exception e) {
                suiteSection.warning("Could not parse report: " + file.getName());
            }
        }

        extent.flush();
    }
}
