package Client;

import javax.sound.sampled.*;

public class AudioPlayer {
    private SourceDataLine sourceDataLine;
    private AudioFormat audioFormat;
    AudioPlayer() {
        audioFormat = setAudioFormat();
        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, this.audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(this.audioFormat);
            sourceDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private AudioFormat setAudioFormat() {
        float sampleRate = 44100.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
    }

    public void playAudio(byte[] data) {
        sourceDataLine.write(data, 0, data.length);
    }
}
