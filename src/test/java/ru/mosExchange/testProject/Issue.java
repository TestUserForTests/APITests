package ru.mosExchange.testProject;

import java.util.Objects;

public class Issue {
    private int id;
    private String title;
    private String body;

    public int getId() {
        return id;
    }

    public Issue withId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Issue withTitle(String title) {
        this.title = title;
        return this;
    }

    public Issue withBody(String body) {
        this.body = body;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (id != issue.id) return false;
        if (title != null ? !title.equals(issue.title) : issue.title != null) return false;
        return body != null ? body.equals(issue.body) : issue.body == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
