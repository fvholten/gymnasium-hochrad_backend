package de.hochrad.backend.domain;

import java.util.Objects;

public class Vertretung {

    private Vertretung() {

    }

    private String wochentag;
    private String klasse;
    private String stunde;
    private String art;
    private String fach;
    private String raum;
    private String info;

    private Vertretung(String wochentag, String klasse, String stunde, String art, String fach, String raum, String info) {
        this.wochentag = wochentag;
        this.klasse = klasse;
        this.stunde = stunde;
        this.art = art;
        this.fach = fach;
        this.raum = raum;
        this.info = info;
    }

    public String getWochentag() {
        return wochentag;
    }

    public String getKlasse() {
        return klasse;
    }

    public String getStunde() {
        return stunde;
    }

    public String getArt() {
        return art;
    }

    public String getFach() {
        return fach;
    }

    public String getRaum() {
        return raum;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertretung)) return false;
        Vertretung that = (Vertretung) o;
        return Objects.equals(wochentag, that.wochentag) &&
                Objects.equals(klasse, that.klasse) &&
                Objects.equals(stunde, that.stunde) &&
                Objects.equals(art, that.art) &&
                Objects.equals(fach, that.fach) &&
                Objects.equals(raum, that.raum) &&
                Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wochentag, klasse, stunde, art, fach, raum, info);
    }

    public static class Builder {

        private String wochentag;
        private String klasse;
        private String stunde;
        private String art;
        private String fach;
        private String raum;
        private String info;

        public Builder forWochentag(String wochentag) {
            this.wochentag = wochentag;
            return this;
        }

        public Builder forKlasse(String klasse) {
            this.klasse = klasse;
            return this;
        }

        public Builder forStunde(String stunde) {
            this.stunde = stunde;
            return this;
        }

        public Builder forArt(String art) {
            this.art = art;
            return this;
        }

        public Builder forFach(String fach) {
            this.fach = fach;
            return this;
        }

        public Builder forRaum(String raum) {
            this.raum = raum;
            return this;
        }

        public Builder forInfo(String info) {
            this.info = info;
            return this;
        }

        public Vertretung build() {
            return new Vertretung(wochentag, klasse, stunde, art, fach, raum, info);
        }
    }
}
