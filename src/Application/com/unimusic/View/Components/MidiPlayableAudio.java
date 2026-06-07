package com.unimusic.View.Components;

import javafx.util.Duration;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MidiPlayableAudio implements PlayableMedia {

    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private Sequence sequence;
    private Runnable onReady;
    private List<TimeChangedListener> timeListeners = new ArrayList<>();
    private boolean running = false;

    public MidiPlayableAudio() {
        try {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();

            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            Transmitter seqTransmitter = sequencer.getTransmitter();
            Receiver synthReceiver = synthesizer.getReceiver();
            seqTransmitter.setReceiver(synthReceiver);

            sequencer.addMetaEventListener(meta -> {
                if (meta.getType() == 47) {
                    running = false;
                }
                Duration current = getMaxDuration().multiply(sequencer.getMicrosecondPosition() / (double) sequence.getMicrosecondLength());
                for (TimeChangedListener listener : timeListeners) {
                    listener.timeChanged(current);
                }
            });

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void load(String filePath) {
        try {
            sequence = MidiSystem.getSequence(new File(filePath));
            sequencer.setSequence(sequence);
            if (onReady != null) onReady.run();
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seek(Duration duration) {
        if (sequencer != null && sequence != null) {
            long micro = (long)(sequence.getMicrosecondLength() * duration.toMillis() / getMaxDuration().toMillis());
            sequencer.setMicrosecondPosition(micro);
        }
    }

    @Override
    public void setVolume(double volume) {
        if (synthesizer != null) {
            int vol = (int)(volume * 127);
            MidiChannel[] channels = synthesizer.getChannels();
            for (MidiChannel ch : channels) {
                ch.controlChange(7, vol);
            }
        }
    }

    @Override
    public void setMute(boolean isMute) {

    }

    @Override
    public void setOnReady(Runnable runnable) {
        this.onReady = runnable;
    }

    @Override
    public void addCurrentTimeListener(TimeChangedListener listener) {
        timeListeners.add(listener);
    }

    @Override
    public Duration getMaxDuration() {
        if (sequence != null) {
            return Duration.millis(sequence.getMicrosecondLength() / 1000.0);
        }
        return Duration.ZERO;
    }

    @Override
    public void init() {

    }

    @Override
    public MediaPlayer getLocalMediaPlayer() {
        return null;
    }

    @Override
    public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
        return null;
    }

    @Override
    public void play() {
        if (sequencer != null && !sequencer.isRunning()) {
            sequencer.start();
            running = true;
        }
    }

    @Override
    public void pause() {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
            running = false;
        }
    }

    @Override
    public void restart() {
        if (sequencer != null) {
            sequencer.stop();
            sequencer.setTickPosition(0);
            running = false;
        }
    }

    @Override
    public void stop() {
        dispose();
    }


    public void setInstrument(int channel, int instrument) {
        if (synthesizer != null && channel >= 0 && channel < 16) {
            MidiChannel[] channels = synthesizer.getChannels();
            channels[channel].programChange(instrument);
        }
    }

    public void dispose() {
        if (sequencer != null && sequencer.isOpen()) sequencer.close();
        if (synthesizer != null && synthesizer.isOpen()) synthesizer.close();
    }
}
