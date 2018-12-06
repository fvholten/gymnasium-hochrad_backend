package de.hochrad.backend.utils;

import de.hochrad.backend.domain.Vertretung;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParseUtils {

    static final String BASE_URL;
    private static final String hochradURI = "https://www.gymnasium-hochrad.de/";

    static {
        String tempURL = "https://hochrad.de/idesk/plan/public.php/Vertretungsplan%20Sch%C3%BCler/55b3979bef1fa6b3/";
        try {
            tempURL = ParseUtils.parseVertretungsplanBaseURI();
        } catch (IOException ignored) {
        }
        BASE_URL = tempURL;
    }

    public static List<String> parseClasses() throws IOException {

        String suffix = "frames/navbar.htm";

        String text = Jsoup.connect(BASE_URL + suffix).get().toString();

        String[] splitted = text.split("var classes = \\[\"");
        if (splitted.length == 2) {
            splitted = splitted[1].split("\"];");
            if (splitted.length > 0) {
                return cleanupClasses(Arrays.asList(splitted[0].split("\",\"")));
            }
        }
        return Collections.emptyList();
    }

    private static List<String> cleanupClasses(@Nonnull List<String> classes) {
        return classes.stream().map(s -> s.replace("/", "-")).collect(Collectors.toList());
    }

    private static String parseVertretungsplanBaseURI() throws IOException {
        Document doc = Jsoup.connect(hochradURI).get();
        if (doc != null) {
            return doc.body()
                    .getElementById("mighty_allwrap")
                    .getElementById("header")
                    .getElementById("menu-quickmenu")
                    .getElementById("menu-item-75")
                    .getAllElements()
                    .attr("href");
        }
        return "";
    }

    private static String wochentag = "";

    public static List<Vertretung> parseVertretungen(@Nonnull String url, @Nonnull String klasse) throws IOException {
        // Aufbau der Seite (Beispielhaft)
        // <div id="vertretung">
        //  ... <b>18.5. Montag</b> ...
        //  <table class="subst" > ***Details für Montag*** </table>
        //  ... <b>20.5. Dienstag</b> ...
        //  <table class="subst" > ***Details für Dienstag*** </table>
        //  ...
        // </div>

        List<Vertretung> vertretungList = new ArrayList<>();

        Jsoup.connect(url).get()
                .getElementById("vertretung").children()
                .forEach(part -> {
                    if (part.nodeName().equals("b") && !part.text().startsWith("[")) {
                        wochentag = part.text();
                    } else if (part.nodeName().equals("p")) {
                        part.children()
                                .stream()
                                .filter(part2 -> part2.nodeName().equals("b") && !part2.text().startsWith("["))
                                .forEach(part2 -> wochentag = part2.text());
                    } else if (part.nodeName().equals("table")) {
                        if (part.attributes().hasKey("class") && part.attributes().get("class").equals("subst")) {
                            String finalWochentag = wochentag;
                            part.select("tr").forEach(element -> {
                                Element[] tds = element.select("td").toArray(new Element[0]);
                                if (tds.length >= 7) {
                                    vertretungList.add(new Vertretung.Builder()
                                            .forWochentag(finalWochentag)
                                            .forStunde(tds[0].text())
                                            .forFach(tds[2].text())
                                            .forRaum(tds[3].text())
                                            .forArt(tds[4].text())
                                            .forInfo(tds[7].text())
                                            .forKlasse(klasse)
                                            .build());
                                }
                            });
                        }
                    }
                });
        return vertretungList;
    }

    /*
    Die Nachrichten des Tages sind wichtige Informationen, die alle Schüler mitbekommen sollen.
    Sie sind zentral verfügbar und für alle Classes gleich. Sie sind ein Element des
    Vertretungsplanes.
     */

    public static List<String> parseNewsOfDay(@Nonnull String url) throws IOException {
        return Jsoup.connect(url).get().body()
                .select("table")
                .select("tbody")
                .select("tr")
                .stream()
                .flatMap(c -> c.childNodes().stream())
                .filter(n -> n.hasAttr("colspan"))
                .filter(n -> n.nodeName().equals("td"))
                .filter(node -> node.attr("colspan").equals("2"))
                .map(n -> ((Element) n).text())
                .collect(Collectors.toList());

    }
}
