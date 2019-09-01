package ru.mosExchange.testProject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueTests {

    private static final String TOKEN = "93f56cd3a131bc66ecee5e232d4a02ee091fbc7e";
    private static final String URL = "https://api.github.com/repos/TestUserForTests/APITests/issues";
    private static Gson gson = new Gson();

    private String issueTitle;
    private Issue testIssue;
    private Collection<Issue> setOfIssues;

    private TypeToken<List<Issue>> itemsListType = new TypeToken<>(){};

    @BeforeEach
    public void init() throws IOException {
        issueTitle = "Test Issue "+ UUID.randomUUID().toString();
        testIssue = new Issue().withTitle(issueTitle).withBody("Test Issue Body");
        setOfIssues = getIssues(URL, itemsListType);
    }

    @Test
    public void testIssueCreated() throws IOException {
        System.out.println("Old set of issues: ");
        setOfIssues.forEach(System.out::println);

        createIssue(testIssue);

        System.out.println("\n" +"New set of issues: ");
        setOfIssues.forEach(System.out::println);

        assertEquals(1L, issueCountByExample(setOfIssues, testIssue), "One created issue expected");
        System.out.println();
    }

    // getting set of user's issues
    private <T> T getIssues(String url,TypeToken<T> returnType) throws IOException {
        String json = Request.Get(url)
                .execute().returnContent().asString();
        return gson.fromJson(json,returnType.getType());
    }

    //creation of an issue
    private void createIssue(Issue newIssue) throws IOException {
        String json = gson.toJson(newIssue);
        post(URL, TOKEN,json);
    }

    //
    private Response post(String url, String auth, String postData) throws IOException {
        Request putRequest = Request.Post(url)
                .addHeader("Authorization", "token "+ auth)
                .addHeader("Accept","application/vnd.github.symmetra-preview+json")
                .bodyString(postData, ContentType.APPLICATION_JSON);
        return getExecutor().execute(putRequest);
    }

    private Executor getExecutor() {
        return Executor.newInstance();
    }

    //getting the number of created issues
    private long issueCountByExample(Collection<Issue> issues, Issue example){
        return issues.stream().filter(issue -> example.getTitle().equals(issue.getTitle())).count();
    }
}