/*
 * Copyright (c) 2017 by Gerrit Grunwald
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
package eu.hansolo.skins;

import org.junit.Test;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.LcdDesign;
import eu.hansolo.medusa.LcdFont;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.GridPane;

/**
 * Unit Test for LcdSkin
 * @author wf
 *
 */
public class LcdSkinTest extends SkinTest {
  
  public static LcdFont lcdFont=LcdFont.DIGITAL;
  public static LcdDesign lcdDesign=LcdDesign.GRAY;

  /**
   * create the given LCD Gauge
   * @param title
   * @param unit
   * @return  the gauge
   */
  public static Gauge createGauge(String title, String unit, int decimals) {
    Gauge gauge = GaugeBuilder.create().skinType(SkinType.LCD).animated(true)
        .oldValueVisible(false).maxMeasuredValueVisible(false)
        .minMeasuredValueVisible(false).decimals(decimals).tickLabelDecimals(0)
        .title(title).unit(unit)
        .lcdDesign(lcdDesign).lcdFont(lcdFont).build();
    return gauge;
  }
  
  @Test
  public void testIssue128() throws Exception {
    WaitableApp.toolkitInit();
    GridPane gridPane=new GridPane();
    Gauge speedGauge = createGauge("speed","km/h",0);
    gridPane.add(speedGauge, 0, 0);
    //int showTimeMSecs=10000;
    //SampleApp.createAndShow("Issue128", gridPane, showTimeMSecs);
    SampleApp sampleApp = new SampleApp("Issue128",gridPane);
    sampleApp.show();
    sampleApp.waitOpen();
    Property<Number> speed=new SimpleDoubleProperty();
    speedGauge.valueProperty().bind(speed);
    for (int i=0;i<=100;i++) {
      speed.setValue(50.0+Math.random()*10);
      Thread.sleep(10);
    }
    sampleApp.close();
  }

}
