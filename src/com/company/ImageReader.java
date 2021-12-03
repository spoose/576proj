package com.company;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ImageReader {

    private ArrayList<File> list_img;

    public static void main(String[] args) {
        ImageReader reader = new ImageReader();
        reader.list_img = reader.FolderConfig("/Users/yze/Downloads/USC/USCOne");
        BufferedImage testImage = new BufferedImage(352,288,BufferedImage.TYPE_INT_RGB);
        testImage = reader.BImgFromFile(reader.list_img.get(0));

    }


    private int imageWidth = 352;
    private int imageHeight = 288;
    private int pixel_num = 352*288;

    ImageReader(){

    }

    ArrayList<File> FolderConfig(String image_folder_path){
        ArrayList<File> list = new ArrayList<File>();

        File selectedFolder = new File(image_folder_path);
        File[] files = selectedFolder.listFiles();// Get all the names of the files present
        Collections.sort(Arrays.asList(files));
        System.out.println("Files are:");
        // Display the names of the files
//        for (int i = 0; i < files.length; i++) {
//            System.out.println(files[i].getName());
//        }

        for (int i = 3; i < files.length;i++){
            File imageFile = files[i];
            list.add(imageFile);
//            System.out.println(list.get(i-3));
        }
//        System.out.println(list.get(1).getName());
        System.out.println(list.get(1).length());
        System.out.println("Total Images Added: " + list.size());

        return list;
    }

    BufferedImage BImgFromFile(File imageFile){

        if (!imageFile.getAbsolutePath().endsWith(".rgb")){
            return null;
        }
        try {
                InputStream fileStream = new FileInputStream(imageFile);
                long len = imageFile.length();
                byte[] bytes = new byte[(int)len];
                int offset = 0;
                int numberRead;
                int index = 0;
                while (offset < bytes.length && (numberRead=fileStream.read(bytes,offset,bytes.length-offset)) >= 0){
                    offset = offset + numberRead;
                }
                //Create new buffered image object
                BufferedImage newImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB);
                //Read the image pixels
                for (int y = 0; y < imageHeight; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        byte r = bytes[index];
                        byte g = bytes[index+(pixel_num)];
                        byte b = bytes[index+(2*(pixel_num))];
                        int pixel = 0XFF000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF) << 0;
                        newImage.setRGB(x,y,pixel);
                        index = index  +1;
                    }
                }
                fileStream.close();
                return newImage;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

    }
}
