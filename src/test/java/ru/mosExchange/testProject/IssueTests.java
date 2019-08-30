package ru.mosExchange.testProject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueTests {

    @Test
    public void testCreateIssue() throws IOException {
        Set<Issue> oldIssues = getIssues();
        Issue newIssue = new Issue().withTitle("Test Issue").withBody("Test Issue Body");
        int IssueId = createIssue(newIssue);
        Set<Issue> newIssues = getIssues();
        oldIssues.add(newIssue.withId(IssueId));
        assertEquals(newIssues, oldIssues);
    }

    private Set<Issue> getIssues() throws IOException {
        String json = Request.Get("https://api.github.com/repos/TestUserForTests/APITests/issues")
                .execute().returnContent().asString();
        return new HashSet<>(Arrays.asList((new Gson()).fromJson(json,Issue[].class)));
    }

    private int createIssue(Issue newIssue) throws IOException {
        String json = Request.Post("https://api.github.com/repos/TestUserForTests/APITests/issues")
                .addHeader("Authorization","51bd85e2f2a96d0d083c363ca6dfed33ec5455b9")
                .bodyForm(new BasicNameValuePair("title", newIssue.getTitle()),
                        new BasicNameValuePair("body", newIssue.getBody()))
                .execute().returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        return parsed.getAsJsonObject().get("id").getAsInt();
    }

//    private Set<Issue> getIssues() throws IOException {
//        String json = getExecutor().execute(Request.Get("https://api.github.com/repos/TestUserForTests/APITests/issues"))
//                .returnContent().asString();
//        JsonElement parsed = new JsonParser().parse(json);
//        JsonElement issues = parsed.getAsJsonObject().get("issues");
//        return new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
//        return new HashSet<>(Arrays.asList((new Gson()).fromJson(json,Issue[].class)));
//    }

//    private Executor getExecutor() {
//        return Executor.newInstance()
//                .auth("51bd85e2f2a96d0d083c363ca6dfed33ec5455b9", "");
//    }
}