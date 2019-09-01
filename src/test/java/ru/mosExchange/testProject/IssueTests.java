package ru.mosExchange.testProject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IssueTests {

    private static final String TOKEN = "ac3d9326185f38862e20ca1187d2a3540f99cecf";
    private static final String URL = "https://api.github.com/repos/TestUserForTests/APITests/issues";
    private static Gson gson = new Gson();
    private String issueTitle = "Test Issue"+ UUID.randomUUID().toString();
    private Issue testIssue;
    private Collection<Issue> oldIssues;
    private TypeToken<List<Issue>> itemsListType = new TypeToken<>(){};

    @BeforeEach
    void init() throws IOException {
        testIssue = new Issue().withTitle(issueTitle).withBody("Test Issue Body");
        oldIssues = getIssues(itemsListType);
    }

    @Test
    void testIssueCreated() throws IOException {
        System.out.println("Old set of user's issues: "+oldIssues.size()+" issues");
        oldIssues.forEach(System.out::println);

        createIssue(testIssue);

        Collection<Issue> newIssues = getIssues(itemsListType);
        System.out.println("\n"+"New set of user's issues: "+ newIssues.size()+" issues");
        newIssues.forEach(System.out::println);

        assertEquals(1L, issueCountByExample(newIssues, testIssue),"only one issue expected");
        System.out.println("\n"+"Verification OK");
    }


    private <T> T getIssues(TypeToken<T> returnType) throws IOException {
        String json = Request.Get(IssueTests.URL)
                .execute().returnContent().asString();
        return gson.fromJson(json,returnType.getType());
    }

    private void createIssue(Issue newIssue) throws IOException {
        String json = gson.toJson(newIssue);
        post(json);
    }

    private void post(String postData) throws IOException {
        Request putRequest = Request.Post(IssueTests.URL)
                .addHeader("Authorization", "token "+ IssueTests.TOKEN)
                .addHeader("Accept","application/vnd.github.symmetra-preview+json")
                .bodyString(postData, ContentType.APPLICATION_JSON);
        getExecutor().execute(putRequest).returnContent().asString();
    }

    private Executor getExecutor() {
        return Executor.newInstance();
    }

    private long issueCountByExample(Collection<Issue> issues, Issue example){
        return issues.stream().filter(issue -> example.getTitle().equals(issue.getTitle())).count();
    }
}