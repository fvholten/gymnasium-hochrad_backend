package de.hochrad.backend.domain;

import java.util.List;
import java.util.Objects;

public class NewsOfDay {

    private NewsOfDay() {

    }

    private List<String> newsOfDay;

    public NewsOfDay(List<String> newsOfDay) {
        this.newsOfDay = newsOfDay;
    }

    public List<String> getNewsOfDay() {
        return newsOfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsOfDay)) return false;
        NewsOfDay newsOfDay1 = (NewsOfDay) o;
        return Objects.equals(newsOfDay, newsOfDay1.newsOfDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsOfDay);
    }
}
