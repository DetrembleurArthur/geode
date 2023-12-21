package com.geode.net.communications.audio;

import com.geode.net.communications.UdpDataPipe;

import javax.sound.sampled.*;
import java.io.*;

public class MicroTransmitter extends Thread {
    private TargetDataLine line;
    private AudioFormat format;
    private AudioInputStream audioInputStream;
    private UdpDataPipe dataPipe;

    public MicroTransmitter(UdpDataPipe dataPipe) throws LineUnavailableException {
        format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
        line = (TargetDataLine) AudioSystem.getLine(info);
        this.dataPipe = dataPipe;
    }

    public AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
    }

    /**
     * Captures the sound and record into a WAV file
     */

    public void capturing() throws LineUnavailableException {
        line.open(format);
        line.start();   // start capturing
        audioInputStream = new AudioInputStream(line);
    }

    public void pause()
    {
        line.stop();
    }

    public void unpause()
    {
        line.start();
    }

    public void terminate()
    {
        pause();
        line.close();
    }

    @Override
    public void run()
    {
        byte[] buffer;
        try
        {
            while ((buffer = audioInputStream.readNBytes((int) format.getSampleRate())) != null) {
                dataPipe.send(buffer);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}