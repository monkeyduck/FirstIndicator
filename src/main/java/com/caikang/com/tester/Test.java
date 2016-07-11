package com.caikang.com.tester;

import com.caikang.com.pitch.MainLabel;

import java.io.File;
import java.io.IOException;

/**
 * Created by llc on 16/7/7.
 */
public class Test {
    public static void main(String args[]) throws IOException, InterruptedException{
        MainLabel mainLabel = new MainLabel();
        File file = new File("py/labeled.txt");
        mainLabel.downloadAllAudioWav(file);
        mainLabel.createAudioLabel();
        mainLabel.runPython();
    }
}
