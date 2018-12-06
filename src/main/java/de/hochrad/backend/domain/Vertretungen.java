package de.hochrad.backend.domain;

import de.hochrad.backend.utils.ParseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Vertretungen {

    private Vertretungen() {

    }

    private int hashCode;
    private String klasse;
    private int weekOfYear;
    private List<Vertretung> vertretungsList;

    private Vertretungen(String klasse, int weekOfYear, List<Vertretung> vertretungsList, int hashCode) {
        this.klasse = klasse;
        this.weekOfYear = weekOfYear;
        this.vertretungsList = vertretungsList;
        this.hashCode = hashCode;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public List<Vertretung> getVertretungsList() {
        return vertretungsList;
    }

    public void setVertretungsList(List<Vertretung> vertretungsList) {
        this.vertretungsList = vertretungsList;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertretungen)) return false;
        Vertretungen that = (Vertretungen) o;
        return weekOfYear == that.weekOfYear &&
                Objects.equals(klasse, that.klasse) &&
                Objects.equals(vertretungsList, that.vertretungsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(klasse, weekOfYear, vertretungsList);
    }

    public static class Builder {

        private String url;
        private String klasse;
        private int weekOfYear;

        public Builder forUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder forKlasse(String klasse) {
            this.klasse = klasse;
            return this;
        }

        public Builder forWeekOfYear(int weekOfYear) {
            this.weekOfYear = weekOfYear;
            return this;
        }

        public Vertretungen build() throws IOException {
            List<Vertretung> vertretungList = ParseUtils.parseVertretungen(url, klasse);

            return new Vertretungen(klasse, weekOfYear, vertretungList, Objects.hash(klasse, weekOfYear, vertretungList));
        }
    }
}
