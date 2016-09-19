/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.medusa;

import eu.hansolo.medusa.Clock.ClockSkinType;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.events.UpdateEvent;
import eu.hansolo.medusa.events.UpdateEvent.EventType;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;



/**
 * User: hansolo
 * Date: 04.01.16
 * Time: 06:31
 */
public class Test extends Application {
    private static final Random         RND = new Random();
    private static       int            noOfNodes = 0;
	private FGauge			fgauge;
    private Gauge           gauge;
    private Clock           clock;
    private long            lastTimerCall;
    private AnimationTimer  timer;
    private DoubleProperty  value;
    private BooleanProperty toggle;


    @Override public void init() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("da", "DK"));
        numberFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        numberFormat.setMinimumIntegerDigits(3);
        numberFormat.setMaximumIntegerDigits(3);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);

        value  = new SimpleDoubleProperty(0);
        toggle = new SimpleBooleanProperty(false);

        gauge = GaugeBuilder.create()
                            .skinType(SkinType.SIMPLE_SECTION)
                            .prefSize(250, 250)
                            .minValue(0)
                            .maxValue(100)
                            .animated(true)
                            //.checkThreshold(true)
                            //.onThresholdExceeded(e -> System.out.println("threshold exceeded"))
                            //.threshold(50)
                            //.lcdVisible(true)
                            //.locale(Locale.GERMANY)
                            //.numberFormat(numberFormat)
                            .title("Title")
                            .unit("°C")
                            .subTitle("SubTitle")
                            //.interactive(true)
                            //.onButtonPressed(o -> System.out.println("Button pressed"))
                            //.title("Title")
                            .sections(new Section(0, 33, Color.RED),
                                      new Section(33, 66, Color.YELLOW),
                                      new Section(66, 100, Color.LIME))
                            .sectionsVisible(true)
                            //.autoScale(false)
							//.valueColor(Color.WHITE)
                            .build();

		fgauge = FGaugeBuilder.create()
							  .gauge(gauge)
							  .gaugeDesign(GaugeDesign.TILTED_BLACK)
							  .foregroundVisible(true)
							  .build();

        gauge.valueProperty().bind(value);

        gauge.getSections().forEach(section -> section.setOnSectionUpdate(sectionEvent -> gauge.fireUpdateEvent(new UpdateEvent(Test.this, EventType.REDRAW))));

        //gauge.valueVisibleProperty().bind(toggle);

        clock = ClockBuilder.create()
                            .skinType(ClockSkinType.YOTA2)
                            //.onTimeEvent(e -> System.out.println(e.TYPE))
                            .discreteSeconds(false)
                            .secondsVisible(true)
                            //.running(true)
                            .build();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (now > lastTimerCall + 3_000_000_000l) {
                    double v = RND.nextDouble() * gauge.getRange() + gauge.getMinValue();
                    value.set(v);
                    //toggle.set(!toggle.get());

                    //System.out.println(gauge.isValueVisible());

                    //gauge.setValue(v);
                    lastTimerCall = now;
                }
            }
        };
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(fgauge);
//        StackPane pane = new StackPane(gauge);
//        StackPane pane = new StackPane(clock);
        pane.setPadding(new Insets(20));
        LinearGradient gradient = new LinearGradient(0, 0, 0, pane.getLayoutBounds().getHeight(),
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, Color.rgb(38, 38, 38)),
                                                     new Stop(1.0, Color.rgb(15, 15, 15)));
//        pane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
//        pane.setBackground(new Background(new BackgroundFill(Color.rgb(39,44,50), CornerRadii.EMPTY, Insets.EMPTY)));
//        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        pane.setBackground(new Background(new BackgroundFill(Gauge.DARK_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);


        stage.setTitle("Medusa");
        stage.setScene(scene);
        stage.show();

        //gauge.setValue(105);

        // Calculate number of nodes
        calcNoOfNodes(pane);
        System.out.println(noOfNodes + " Nodes in SceneGraph");

        timer.start();

        //gauge.getSections().get(0).setStart(10);
        //gauge.getSections().get(0).setStop(90);
    }

    @Override public void stop() {
        System.exit(0);
    }



    // ******************** Misc **********************************************
    private static void calcNoOfNodes(Node node) {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) { calcNoOfNodes(n); }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
