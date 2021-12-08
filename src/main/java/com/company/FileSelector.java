package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class FileSelector implements ActionListener {

    private final String title;
    private final Component parent;
    private int mode = JFileChooser.FILES_AND_DIRECTORIES;

    FileSelector(String title, Component parent, int mode) {
        this.mode = mode;
        this.title = title;
        this.parent = parent;
    }

    FileSelector(String title, Component parent) {
        this.title = title;
        this.parent = parent;
    }

    abstract void onFileSelected(File selectedFile);

    @Override
    public final void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setDialogTitle(title);
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)) {
            onFileSelected(fileChooser.getSelectedFile());
        }
    }

}