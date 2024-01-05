package com.geode.net.communications.audio;

import com.geode.net.communications.UdpDataPipe;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class MicroManager implements Runnable {

    private final MicroTransmitter transmitter;
    private final MicroReceiver receiver;

    public MicroManager(UdpDataPipe dataPipe) throws LineUnavailableException {
        transmitter = new MicroTransmitter(dataPipe);
        receiver = new MicroReceiver(dataPipe);
    }

    @Override
    public void run() {
        try {
            transmitter.capturing();
            transmitter.start();
            receiver.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listAudioSources() {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for(Mixer.Info info : infos)
        {
            Mixer m = AudioSystem.getMixer(info);
            System.out.println(m.getMixerInfo().getName());
        }
    }
}
