package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class ExtentMerger {
    private static final Pattern EXTENT_JSON_PATTERN =
            Pattern.compile("window.__extent__\\s*=\\s*(\\{.*?\\});", Pattern.DOTALL);

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: ExtentMerger <input-folder> <output-file>");
            return;
        }

        Path inputDir = Paths.get(args[0]);
        Path outputFile = Paths.get(args[1]);

        List<JSONObject> allTests = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*.html")) {
            for (Path htmlFile : stream) {
                System.out.println("Processing: " + htmlFile);

                String html = Files.readString(htmlFile);
                JSONObject json = extractExtentJson(html);

                if (json == null) {
                    System.err.println("WARNING: No JSON found in " + htmlFile);
                    continue;
                }

                // Extract tests array
                if (json.has("test")) {
                    for (Object t : json.getJSONArray("test")) {
                        allTests.add((JSONObject) t);
                    }
                }
            }
        }

        if (allTests.isEmpty()) {
            System.err.println("No tests found. Combined report will be empty.");
        }

        // Build combined JSON
        JSONObject combined = new JSONObject();
        combined.put("test", allTests);

        // Write combined JSON to temp file
        Path tempJson = Paths.get("combined.json");
        Files.writeString(tempJson, combined.toString(2));

        // Generate final HTML report
        ExtentSparkReporter spark = new ExtentSparkReporter(outputFile.toString());
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);

        // Add tests to ExtentReports
        for (JSONObject t : allTests) {
            String name = t.optString("name", "Unnamed Test");
            String status = t.optString("status", "unknown");

            extent.createTest(name).log(
                    com.aventstack.extentreports.Status.valueOf(status.toUpperCase()),
                    "Merged from suite"
            );
        }

        extent.flush();
        System.out.println("Merged report generated: " + outputFile);
    }

    private static JSONObject extractExtentJson(String html) {
        Matcher matcher = EXTENT_JSON_PATTERN.matcher(html);
        if (matcher.find()) {
            String jsonText = matcher.group(1);
            return new JSONObject(jsonText);
        }
        return null;
    }
}
