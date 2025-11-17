package com.unimusic.View.Components;

import javafx.util.Duration;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

            // Listen to ticks and notify listeners
            sequencer.addMetaEventListener(meta -> {
                if (meta.getType() == 47) { // End of track
                    running = false;
                }
                Duration current = getMediaDuration().multiply(sequencer.getMicrosecondPosition() / (double) sequence.getMicrosecondLength());
                for (TimeChangedListener listener : timeListeners) {
                    listener.timeChanged(current);
                }
            });

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Load a MIDI file */
    public void load(String filePath) {
        try {
            sequence = MidiSystem.getSequence(new File(filePath));
            sequencer.setSequence(sequence);
            if (onReady != null) onReady.run();
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    /** Play */
    @Override
    public void playMedia() {
        if (sequencer != null && !sequencer.isRunning()) {
            sequencer.start();
            running = true;
        }
    }

    /** Pause */
    @Override
    public void pauseMedia() {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
            running = false;
        }
    }

    /** Reset */
    @Override
    public void resetMedia() {
        if (sequencer != null) {
            sequencer.stop();
            sequencer.setTickPosition(0);
            running = false;
        }
    }

    /** Seek to position */
    @Override
    public void seek(Duration duration) {
        if (sequencer != null && sequence != null) {
            long micro = (long)(sequence.getMicrosecondLength() * duration.toMillis() / getMediaDuration().toMillis());
            sequencer.setMicrosecondPosition(micro);
        }
    }

    /** Volume control (0.0 - 1.0) for all channels */
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

    /** Render/stopRender placeholders */
    @Override
    public void render() {}
    @Override
    public void stopRender() {}

    /** OnReady callback */
    @Override
    public void setOnReady(Runnable runnable) {
        this.onReady = runnable;
    }

    /** Add listener for time changes */
    @Override
    public void addCurrentTimeListener(TimeChangedListener listener) {
        timeListeners.add(listener);
    }

    /** Duration of MIDI */
    @Override
    public Duration getMediaDuration() {
        if (sequence != null) {
            return Duration.millis(sequence.getMicrosecondLength() / 1000.0);
        }
        return Duration.ZERO;
    }

    /** Not applicable for MIDI */
    @Override
    public MediaPlayer getLocalMediaPlayer() {
        return null;
    }
    @Override
    public EmbeddedMediaPlayer getOnlineMediaPlayer() {
        return null;
    }
    @Override
    public Media getLocalMedia() {
        return null;
    }

    /** Change instrument for a channel */
    public void setInstrument(int channel, int instrument) {
        if (synthesizer != null && channel >= 0 && channel < 16) {
            MidiChannel[] channels = synthesizer.getChannels();
            channels[channel].programChange(instrument);
        }
    }

    /** Dispose resources */
    public void dispose() {
        if (sequencer != null && sequencer.isOpen()) sequencer.close();
        if (synthesizer != null && synthesizer.isOpen()) synthesizer.close();
    }
}
