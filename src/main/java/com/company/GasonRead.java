package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GasonRead {

    String pathName = "/Users/yze/Documents/GitHub/576proj/src/main/java/com/company/jsonFile3.json";
    Color color;
    LinkList[] linkLists;

    GasonRead(String pathName){
        this.pathName = pathName;
    }

    public LinkList[] getLinkList(){
        Gson gson = new Gson();
        LinkList[] links;
        try (Reader reader = new FileReader(pathName)) {
            // Convert JSON File to Java Objec
            links = gson.fromJson(reader, LinkList[].class);
            return links;
        }
        catch (IOException e) {
                System.out.println("last video, this video doesn't contain hyper link");
                return null;
//            e.printStackTrace();
        }
//        return  null;
    }

    public Map<Integer, Rectangle> getLinkShape(int index){
        LinkList aLink = getLinkList()[index];
        Map<Integer, Rectangle> linkShape = new HashMap<>();
        linkShape = aLink.linkShape;
        return  linkShape;
    }

    // pretty print
    public static void main(String[] args) {
//        saveJsonFile();
        Gson gson = new Gson();
                try (Reader reader = new FileReader("/Users/yze/Documents/GitHub/576proj/src/main/java/com/company/jsonFile3.json")) {
            // Convert JSON File to Java Objec
            LinkList[] links = gson.fromJson(reader, LinkList[].class);
            // print staff
            System.out.println(gson.toJson(links));
            System.out.println(links[1].linkShape.keySet());

            Map<Integer,ArrayList<Rectangle>> shapeListMap = new HashMap<>();
            for (int j = 0; j < 8999; j++){
                shapeListMap.put(j,new ArrayList<>());
            }
            for (int i = 0; i < links.length; i++){
                for(int j : links[i].linkShape.keySet()){
                    Rectangle rec = links[i].linkShape.get(j);
                    shapeListMap.get(j).add(rec);
                    System.out.println(rec);
                }
            }

            System.out.println(shapeListMap);
            System.out.println(links[0].linkShape.get(1).getX());
//            System.out.println(link.targetFrame);
//            System.out.println(link.linkShape.get(1).getBounds2D());//shape
//            ArrayList<Shape> shapes = new ArrayList<>();
//            shapes.add(links[0].linkShape.get(8));
//            System.out.println("Shapes:"+shapes.get(0));



        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (Reader reader = new FileReader("/Users/yze/Documents/GitHub/576proj/src/main/java/com/company/jsonFile.json")) {
//            // Convert JSON File to Java Object
//            HyperLink_read link = gson.fromJson(reader, HyperLink_read.class);
//            // print staff
//            System.out.println(link.linkColor);
//            System.out.println(link.targetFrame);
//            System.out.println(link.linkShape.get(1).getBounds2D());//shape
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void saveJsonFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<Integer, Shape> linkShape = new HashMap<>();
        int testOffset = 10;
        linkShape.put(1,makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));
        testOffset += 10;
        linkShape.put(3,makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));

        HyperLink link = new HyperLink("testL","/Users/yze/USCOne","/Users/yze/USCTwo",100,50,linkShape,Color.GREEN.getRGB());

        ArrayList<HyperLink> links = new ArrayList<>();
        links.add(link);
        links.add(link);
        // Java objects to String
        String json = gson.toJson(links);

        System.out.println(json);

        // Java objects to File
        try (
                FileWriter writer = new FileWriter("/Users/yze/Documents/GitHub/576proj/src/main/java/com/company/jsonFile2.json")) {
            gson.toJson(links, writer);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public static Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

//    private static HyperLink createLinkObject() {

//        HyperLink staff = new HyperLink();
//
//        staff.setName("mkyong");
//        staff.setAge(35);
//        staff.setPosition(new String[]{"Founder", "CTO", "Writer"});
//        Map<String, BigDecimal> salary = new HashMap() {{
//            put("2010", new BigDecimal(10000));
//            put("2012", new BigDecimal(12000));
//            put("2018", new BigDecimal(14000));
//        }};
//        staff.setSalary(salary);
//        staff.setSkills(Arrays.asList("java", "python", "node", "kotlin"));
//
//        return staff;
//    }

}
