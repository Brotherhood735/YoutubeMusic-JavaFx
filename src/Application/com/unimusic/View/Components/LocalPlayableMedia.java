package com.unimusic.View.Components;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import java.nio.ByteBuffer;

public class LocalPlayableMedia implements PlayableMedia {

    protected MediaPlayerFactory mediaPlayerFactory;

    protected EmbeddedMediaPlayer  embeddedMediaPlayer;

    protected WritableImage videoImage;

    protected PixelBuffer<ByteBuffer> videoPixelBuffer;

    protected ImageView videoImageView;

    protected String mediaUrl;

    public LocalPlayableMedia(String mediaName, ImageView videoImageView) {
        mediaUrl = mediaName ;
        this.videoImageView = videoImageView;
    }

    public void play(String path){
        embeddedMediaPlayer.media().play(path);
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
        embeddedMediaPlayer.controls().play();
    }

    @Override
    public void pause() {
        embeddedMediaPlayer.controls().pause();
    }

    @Override
    public void restart() {
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.controls().play();
    }

    @Override
    public void stop() {
        dispose();
    }

    @Override
    public void dispose(){
        if (embeddedMediaPlayer == null) return;
        if (mediaPlayerFactory == null) return;
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        mediaPlayerFactory.release();
    }

    @Override
    public void seek(Duration duration) {
        embeddedMediaPlayer.controls().setTime((long) duration.toMillis());
    }

    @Override
    public void setVolume(double volume) {
        embeddedMediaPlayer.audio().setVolume((int) volume);
    }

    @Override
    public void setOnReady(Runnable runnable) {
        embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                runnable.run();
            }
        });
    }

    @Override
    public void addCurrentTimeListener(TimeChangedListener listener) {
        embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter()
        {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                listener.timeChanged(Duration.millis(newTime));
            }
        });
    }

    @Override
    public Duration getMaxDuration() {
        return Duration.millis(embeddedMediaPlayer.status().length());
    }

    class FXCallbackVideoSurface extends CallbackVideoSurface {
        FXCallbackVideoSurface() {
            super(new FXBufferFormatCallback(), new FXRenderCallback(), true, VideoSurfaceAdapters.getVideoSurfaceAdapter());
        }
    }

    private class FXBufferFormatCallback implements BufferFormatCallback {
        private int sourceWidth;
        private int sourceHeight;

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            this.sourceWidth = sourceWidth;
            this.sourceHeight = sourceHeight;
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void newFormatSize(int i, int i1, int i2, int i3) {

        }

        @Override
        public void allocatedBuffers(ByteBuffer[] buffers) {
            assert buffers[0].capacity() == sourceWidth * sourceHeight * 4;
            PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
            videoPixelBuffer = new PixelBuffer<>(sourceWidth, sourceHeight, buffers[0], pixelFormat);
            videoImage = new WritableImage(videoPixelBuffer);
            videoImageView.setImage(videoImage);
        }
    }

    private class FXRenderCallback implements RenderCallback {
        @Override
        public void lock(MediaPlayer mediaPlayer) {

        }

        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] byteBuffers, BufferFormat bufferFormat, int i, int i1) {
            Platform.runLater(() -> {
                videoPixelBuffer.updateBuffer(pb -> null);
            });
        }

        @Override
        public void unlock(MediaPlayer mediaPlayer) {

        }
    }
}

