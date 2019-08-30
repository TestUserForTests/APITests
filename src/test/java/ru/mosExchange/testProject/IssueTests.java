package ru.mosExchange.testProject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
        String json = getExecutor().execute(Request.Get("https://api.github.com/TestUserForTests/issues"))
                .returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issues = parsed.getAsJsonObject().get("issues");
        return new Gson().fromJson(issues, new TypeToken<Set<Issue>>(){}.getType());
    }

    private Executor getExecutor() {
        return Executor.newInstance()
                .auth("51bd85e2f2a96d0d083c363ca6dfed33ec5455b9", "");
    }

    private int createIssue(Issue newIssue) throws IOException {
        String json = getExecutor().execute(Request.Post("https://api.github.com/repos/TestUserForTests/APITests/issues")
                .bodyForm(new BasicNameValuePair("title", newIssue.getTitle()),
                        new BasicNameValuePair("body", newIssue.getBody())))
                .returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        return parsed.getAsJsonObject().get("id").getAsInt();
    }
}

//    public void main() {
//    }