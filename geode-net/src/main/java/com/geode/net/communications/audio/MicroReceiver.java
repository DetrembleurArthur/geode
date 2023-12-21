package com.geode.net.communications.audio;

import com.geode.net.communications.UdpDataPipe;

import javax.sound.sampled.*;
import java.io.IOException;

public class MicroReceiver extends Thread {
    private AudioFormat format;
    private UdpDataPipe dataPipe;

    public MicroReceiver(UdpDataPipe dataPipe) throws LineUnavailableException {
        format = getAudioFormat();
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

    @Override
    public void run()
    {
        byte[] buffer;
        try
        {
            while ((buffer = (byte[]) dataPipe.recv()) != null) {
                Clip clip = AudioSystem.getClip();
                clip.open(format, buffer, 0, buffer.length);
                clip.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}