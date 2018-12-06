package de.hochrad.backend;

import de.hochrad.backend.domain.Classes;
import de.hochrad.backend.domain.NewsOfDay;
import de.hochrad.backend.domain.Vertretungen;
import de.hochrad.backend.utils.FirebaseUtils;
import de.hochrad.backend.utils.ParseUtils;
import de.hochrad.backend.utils.UrlUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;

class Main {

    public static void main(String[] args) {
        runEvery10Minutes();
    }

    private static FirebaseUtils firebaseUtils;

    static {
        try {
            firebaseUtils = new FirebaseUtils();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.hochrad.de/");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    private static void runEvery10Minutes() {
        final Runnable beeper = () -> {
            if (netIsAvailable()) {
                try {
                    List<String> classes = ParseUtils.parseClasses();
                    uploadVertretungen(classes);
                    uploadClasses(new Classes(classes));

                    List<String> newsOfDay = ParseUtils.parseNewsOfDay(UrlUtils.getClassURL(UrlUtils.getWeekOfYear(0), 1));

                    uploadNewsOfDay(new NewsOfDay(newsOfDay));

                } catch (IOException ignored) {
                }
            }

        };

        scheduler.scheduleAtFixedRate(beeper, 0, 15, MINUTES);
    }

    private static void uploadVertretungen(List<String> classes) throws IOException {
        for (int i = 0; i < classes.size(); i++) {

            //This-Week
            firebaseUtils.writeVertretungen(new Vertretungen.Builder()
                    .forKlasse(classes.get(i))
                    .forWeekOfYear(UrlUtils.getWeekOfYear(0))
                    .forUrl(UrlUtils.getClassURL(UrlUtils.getWeekOfYear(0), i + 1)).build());

            //Next-Week
            firebaseUtils.writeVertretungen(new Vertretungen.Builder()
                    .forKlasse(classes.get(i))
                    .forWeekOfYear(UrlUtils.getWeekOfYear(1))
                    .forUrl(UrlUtils.getClassURL(UrlUtils.getWeekOfYear(1), i + 1)).build());
        }
    }

    private static void uploadClasses(Classes classes) {
        firebaseUtils.writeKlassen(classes);
    }

    private static void uploadNewsOfDay(NewsOfDay newsOfDay) {
        firebaseUtils.writeNewsOfDay(newsOfDay);
    }

}
