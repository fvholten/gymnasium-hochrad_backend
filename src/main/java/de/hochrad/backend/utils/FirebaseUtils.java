package de.hochrad.backend.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import de.hochrad.backend.domain.Classes;
import de.hochrad.backend.domain.NewsOfDay;
import de.hochrad.backend.domain.Vertretungen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class FirebaseUtils {

    private Firestore db;

    public FirebaseUtils() throws IOException {

        InputStream inputStream = new FileInputStream(new File("C:\\Users\\Finn\\Documents\\service-key.json"));
        FirebaseApp.initializeApp(
                new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(inputStream)))
                        .build());

        FirebaseApp.getInstance();

        db = FirestoreClient.getFirestore(FirebaseApp.getInstance());
    }

    @SuppressWarnings("ConstantConditions")
    public void writeVertretungen(Vertretungen vertretungen) {
        Optional lastVertretungen =
                FileUtils.getObjectFromFile(vertretungen.getKlasse() + "." + vertretungen.getWeekOfYear(), Vertretungen.class);

        if (lastVertretungen.isPresent() && lastVertretungen.get().equals(vertretungen)) {
            System.out.println(vertretungen.getKlasse() + ":" + vertretungen.getWeekOfYear() + "::no changes in file");
        } else {
            db.collection("vertretungsplan")
                    .document(vertretungen.getKlasse() + ":" + vertretungen.getWeekOfYear())
                    .addSnapshotListener(((value, error) -> {
                        if (value != null && value.getLong("hashCode") == vertretungen.hashCode()) {
                            System.out.println(vertretungen.getKlasse() + ":" + vertretungen.getWeekOfYear() + "::no changes in db");
                        } else {
                            db.collection("vertretungsplan")
                                    .document(vertretungen.getKlasse() + ":" + vertretungen.getWeekOfYear())
                                    .set(vertretungen);
                            System.out.println(vertretungen.getKlasse() + ":" + vertretungen.getWeekOfYear() + "::updated!");
                        }
                    }));
            FileUtils.writeObjectToFile(vertretungen, vertretungen.getKlasse() + "." + vertretungen.getWeekOfYear());
        }
    }

    public void writeKlassen(Classes classes) {
        Optional lastClasses = FileUtils.getObjectFromFile("classes", Classes.class);

        if (lastClasses.isPresent() && lastClasses.get().equals(classes)) {
            System.out.println("classes" + "::no changes in file");
        } else {
            db.collection("vertretungsplan").document("classes").addSnapshotListener((value, error) -> {

                if (value != null && Objects.equals(value.get("classes"), classes.getClasses())) {
                    System.out.println("classes::no changes in db");
                } else {
                    db.collection("vertretungsplan").document("classes").set(classes);
                    System.out.println("classes::upload!");
                }
            });
            FileUtils.writeObjectToFile(classes, "classes");
        }
    }

    public void writeNewsOfDay(NewsOfDay newsOfDay) {
        Optional lastNewsOfDay = FileUtils.getObjectFromFile("newsOfDay", NewsOfDay.class);

        if (lastNewsOfDay.isPresent() && lastNewsOfDay.get().equals(newsOfDay)) {
            System.out.println("newsOfDay" + "::no changes in file");
        } else {
            db.collection("vertretungsplan").document("newsOfDay").addSnapshotListener((value, error) -> {
                if (value != null && Objects.equals(value.get("newsOfDay"), newsOfDay.getNewsOfDay())) {
                    System.out.println("newsOfDay::no changes in db");
                } else {
                    db.collection("vertretungsplan").document("newsOfDay").set(newsOfDay);
                    System.out.println("newsOfDay::upload!");
                }
            });
            FileUtils.writeObjectToFile(newsOfDay, "newsOfDay");
        }
    }
}
