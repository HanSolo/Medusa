package eu.hansolo.skins;
/**
*
* This file is derived from  the https://github.com/BITPlan/can4eve open source project
*
* Copyright 2017 BITPlan GmbH https://github.com/BITPlan
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
*
*  You may obtain a copy of the License at
*
*  http:www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
* create a sample App
* 
* @author wf
*
*/
public class SampleApp extends WaitableApp {
 private String title;
 private Region region;
 private int screenPercent;
 private int divX;
 private int divY;

 /**
  * construct the given Sample App
  * 
  * @param title
  * @param region
  */
 public SampleApp(String title, Region region) {
   this(title,region,67,2,2);
 }

 /**
  * construct me
  * @param title
  * @param region
  * @param screenPercent
  * @param divX
  * @param divY
  */
 public SampleApp(String title, Region region, int screenPercent,int divX,int divY) {
   this.title=title;
   this.region=region;
   this.screenPercent=screenPercent;
   this.divX=divX;
   this.divY=divY;
 }
 
 /**
  * create and show the given Sample App
  * @param title
  * @param region
  * @param showTimeMSecs
  * @return the app
  * @throws Exception
  */
 public static SampleApp createAndShow(String title,Region region,int showTimeMSecs) throws Exception {
   SampleApp sampleApp = new SampleApp(title, region);
   sampleApp.show();
   sampleApp.waitOpen();
   Thread.sleep(showTimeMSecs);
   sampleApp.close();
   return sampleApp;
 }

 @Override
 public void start(Stage stage) {
   super.start(stage);
   stage.setTitle(title);
   Rectangle2D sceneBounds=super.getSceneBounds(screenPercent,divX,divY);
   Scene scene = new Scene(region, sceneBounds.getWidth(), sceneBounds.getHeight());
   stage.setScene(scene);
   stage.setX(sceneBounds.getMinX());
   stage.setY(sceneBounds.getMinY());
   stage.show();
 }

}
