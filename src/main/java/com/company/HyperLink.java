package com.company;

import com.google.gson.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HyperLink {
    public static void main(String[] agrs){

    }

    String linkName;
    String oriPath;
    int oriFrame;

    int targetFrame;
    String targetPath;
    String targetJsonPath;

    int linkColor;

    ArrayList<HyperLink> links;
//    ArrayList<Shape> shapes;
//    Map<Integer,drawDemo> video_ori_map;//?
    Map<Integer,ArrayList<Shape>> shapeMap;//?

    Map<Integer,Shape> linkShape;//each link has one box in each frame !
    HyperLink(){
    }

    HyperLink(String linkName, String oriPath, String targetPath ,int targetFrame,int oriFrame,Map<Integer,Shape> linkShape, int linkColor ){
        this.linkName = linkName;
        this.oriPath = oriPath;
        this.targetPath = targetPath;
        this.targetFrame = targetFrame;
        this.linkShape = linkShape;
        this.linkColor = linkColor;
        this.oriFrame =oriFrame;
//        this.targetJsonPath = targetJsonPath;
    }

    HyperLink(String linkName, String oriPath, String targetPath, int targetFrame){
        this.linkName = linkName;
        this.oriPath = oriPath;
        this.targetPath = targetPath;
        this.targetFrame = targetFrame;
    }
//    public void addLinkToLinks(){
//        links.add(new HyperLink(linkName,oriPath,targetPath,targetFrame,linkShape,linkColor));
//    }

      public void saveJsonFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Map<Integer, Shape> linkShape = new HashMap<>();
//        int testOffset = 10;
//        linkShape.put(1,makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));
//        testOffset += 10;
//        linkShape.put(3,makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));"/Users/yze/USCOne


        // Java objects to String
        String json = gson.toJson(links);
        System.out.println(json);

        // Java objects to File
        try (
                FileWriter writer = new FileWriter("/Users/yze/Documents/GitHub/576proj/src/main/java/com/company/jsonFile.json")) {
            gson.toJson(links, writer);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public  Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

//    public JsonObject toJsonObj() {
//        JsonObject object = new JsonObject();
//
//        object.addProperty("linkName", this.linkName);
//        object.addProperty("oriPath", this.oriPath);
//        object.addProperty("targetPath", this.targetPath);
//        object.addProperty("targetFrame", this.targetFrame);
//        object.addProperty("linkColor", this.linkColor.toString());
//
//        JsonArray coordinateSet = new JsonArray();
//
//        for(int key : this.linkShape.keySet()){
//            Shape tempShape = linkShape.get(key);
//            JsonObject coordinateItem = new JsonObject();
//            coordinateItem.addProperty("frameNum", key);
//            coordinateItem.addProperty("x1", tempShape.getBounds().getX());
//            coordinateItem.addProperty("y1", tempShape.getBounds().getY());
//            coordinateItem.addProperty("width", tempShape.getBounds().getWidth());
//            coordinateItem.addProperty("height", tempShape.getBounds().getHeight());
//            coordinateSet.add(coordinateItem);
//        }
//        object.add("coordinateSet", coordinateSet);
//
//        return object;
//    }


}
