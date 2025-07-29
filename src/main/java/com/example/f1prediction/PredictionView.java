package com.example.f1prediction;
import com.example.f1prediction.schedule.PredictService;
import com.example.f1prediction.schedule.RaceSchedule;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.Route;
import java.util.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Route("prediction")

public class PredictionView extends VerticalLayout {

    private final HorizontalLayout cardsLayout = new HorizontalLayout();
    private final Paragraph shuffleStatus = new Paragraph("Shuffling predictions...");

    private final RaceSchedule raceSchedule;
    private final PredictService predictService;

    @Autowired
    public PredictionView(RaceSchedule raceSchedule, PredictService predictService) {
        this.raceSchedule = raceSchedule;
        this.predictService = predictService;

        setSizeFull();
        setPadding(true);
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Background image
        getElement().getStyle().set("background-image", "url('images/f1_background.jpg')");
        getElement().getStyle().set("background-size", "cover");
        getElement().getStyle().set("background-position", "center");
        getElement().getStyle().set("background-repeat", "no-repeat");



        // Next race title
        String nextRace = raceSchedule.getNextRace();
        H1 nextRaceTitle = new H1("Next Race: " + nextRace + " 🏁");
        nextRaceTitle.getStyle()
                .set("color", "#0a3d62") // dark blue
                .set("font-size", "2.5em")
                .set("font-weight", "bold")
                .set("animation", "pulse 2s infinite ease-in-out");
        getElement().executeJs("""
    const style = document.createElement('style');
    style.textContent = `
      @keyframes pulse {
        0% { transform: scale(1); }
        50% { transform: scale(1.05); }
        100% { transform: scale(1); }
      }
    `;
    document.head.appendChild(style);
""");
        add(nextRaceTitle);

        // Main title
        H2 title = new H2("\uD83C\uDFC1 F1 Race Win Predictions");
        title.getStyle().setColor("white");
        title.getStyle().set("font-style", "italic");
        title.getStyle().set("text-shadow", "2px 2px 4px #000");
        title.getStyle().set("margin-bottom", "40px");
        add(title);

        // Shuffle status message
        shuffleStatus.getStyle().set("color", "yellow");
        shuffleStatus.getStyle().set("font-weight", "bold");
        shuffleStatus.getStyle().set("margin-bottom", "10px");
        add(shuffleStatus);

        // Card layout
        cardsLayout.setWidthFull();
        cardsLayout.setSpacing(true);
        cardsLayout.setAlignItems(Alignment.CENTER);
        cardsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        cardsLayout.getStyle().set("flex-wrap", "wrap");
        add(cardsLayout);

        showSortedDrivers(getPredictionForDrivers());
        //startShuffleAnimation();
    }

    private List<DriverPrediction> getPredictionForDrivers() {
        Map<String,Double> predictionMap = predictService.predictionResults(raceSchedule.getNextRace());
        return List.of(
                new DriverPrediction("Max Verstappen", "/images/max.png", predictionMap.get("VER")),
                new DriverPrediction("Oscar Piastri", "/images/oscar.png", predictionMap.get("PIA")),
                new DriverPrediction("Charles Leclerc", "/images/leclerc.png", predictionMap.get("LEC")),
                new DriverPrediction("Lando Norris", "/images/lando.png", predictionMap.get("NOR")),
                new DriverPrediction("Lewis Hamilton", "/images/hamilton.png", predictionMap.get("HAM")),
                new DriverPrediction("George Russel", "/images/russel.png", predictionMap.get("RUS"))
        );
    }

    private void showSortedDrivers(List<DriverPrediction> list) {
        cardsLayout.removeAll();
        list.stream()
                .sorted(Comparator.comparingDouble(DriverPrediction::getProbability).reversed())
                .forEach(dp -> cardsLayout.add(buildCard(dp)));
        shuffleStatus.setText(""); // Hide shuffle message after done
    }

    private void showShuffledDrivers(List<DriverPrediction> list) {
        cardsLayout.removeAll();
        List<DriverPrediction> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        shuffled.forEach(dp -> cardsLayout.add(buildCard(dp)));
    }
    /*
    private void startShuffleAnimation() {
        Thread shuffleThread = new Thread(() -> {
            try {
                int shuffleCount = 10;
                int shuffleDelay = 300;
                for (int i = 0; i < shuffleCount; i++) {
                    UI ui = UI.getCurrent();
                    if (ui != null) {
                        ui.access(() -> showShuffledDrivers(drivers));
                    }
                    Thread.sleep(shuffleDelay);
                }
                UI ui = UI.getCurrent();
                if (ui != null) {
                    ui.access(() -> showSortedDrivers(drivers));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        shuffleThread.start();
    }*/

    private Component buildCard(DriverPrediction dp) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("180px");
        card.setPadding(true);
        card.setSpacing(false);
        card.getStyle().set("border", "1px solid #ccc");
        card.getStyle().set("border-radius", "8px");
        card.setAlignItems(Alignment.CENTER);
        card.getStyle().set("background", "white");

        Image img = new Image(dp.getImagePath(), dp.getName());
        img.setWidth("150px");
        img.setHeight("150px");
        img.getStyle().set("border-radius", "50%");

        Paragraph name = new Paragraph(dp.getName());
        name.getStyle().set("font-weight", "bold");
        name.getStyle().set("font-size", "1.1em");
        name.getStyle().set("margin", "8px 0 4px 0");

        Paragraph prediction = new Paragraph(String.format("Win Prediction: %.5f", dp.getProbability()));
        prediction.getStyle().set("margin", "0 0 8px 0");

        card.add(img, name, prediction);

        return card;
    }

    public static class DriverPrediction {
        private final String name;
        private final String imagePath;
        private final double probability;

        public DriverPrediction(String name, String imagePath, double probability) {
            this.name = name;
            this.imagePath = imagePath;
            this.probability = probability;
        }

        public String getName() {
            return name;
        }

        public String getImagePath() {
            return imagePath;
        }

        public double getProbability() {
            return probability;
        }
    }
}
