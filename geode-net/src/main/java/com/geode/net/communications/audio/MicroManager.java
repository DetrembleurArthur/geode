package com.geode.net.communications.audio;

import com.geode.net.communications.UdpDataPipe;

import javax.sound.sampled.LineUnavailableException;

public class MicroManager implements Runnable {

    private MicroTransmitter transmitter;
    private MicroReceiver receiver;

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
}
