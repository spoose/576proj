package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class LinkList {
    ArrayList<HyperLink_read> links;

    String linkName;
    String oriPath;

    int oriFrame;
    int targetFrame;
    String targetPath;
    String targetJsonPath;

    int linkColor;

    Map<Integer, Rectangle> linkShape;

}
