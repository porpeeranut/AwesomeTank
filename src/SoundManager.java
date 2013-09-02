
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class SoundManager {

  private int[] buffers = new int[256];
  private int[] sources;
  private IntBuffer scratchBuffer = BufferUtils.createIntBuffer(256);
  private boolean soundOutput;
  private int bufferIndex;
  private int sourceIndex;

  public SoundManager() {
  }

  public void playEffect(int buffer) {
    if(soundOutput) {
      // make sure we never choose last channel, since it is used for special sounds
    	int channel = sources[(sourceIndex++ % (sources.length-1))];

      // link buffer and source, and play it
    	AL10.alSourcei(channel, AL10.AL_BUFFER, buffers[buffer]);
    	AL10.alSourcePlay(channel);
    }
  }

  public void playSound(int buffer) {
    if(soundOutput) {
      AL10.alSourcei(sources[sources.length-1], AL10.AL_BUFFER, buffers[buffer]);
      AL10.alSourcePlay(sources[sources.length-1]);
    }
  }

  public boolean isPlayingSound() {
  	return AL10.alGetSourcei(sources[sources.length-1], AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
  }

  public void initialize(int channels) {
    try {
     AL.create();

     // allocate sources
     scratchBuffer.limit(channels);
     AL10.alGenSources(scratchBuffer);
     scratchBuffer.rewind();
     scratchBuffer.get(sources = new int[channels]);

     // could we allocate all channels?
     if(AL10.alGetError() != AL10.AL_NO_ERROR) {
     	throw new LWJGLException("Unable to allocate " + channels + " sources");
     }

     // we have sound
     soundOutput = true;
    } catch (LWJGLException le) {
    	le.printStackTrace();
      System.out.println("Sound disabled");
    }
  }
  
  public int addSound(String path) throws IOException {
    // Generate 1 buffer entry
    scratchBuffer.rewind().position(0).limit(1);
    AL10.alGenBuffers(scratchBuffer);
    buffers[bufferIndex] = scratchBuffer.get(0);

    // load wave data from buffer
    WaveData wavefile = WaveData.create(path);

    // copy to buffers
    AL10.alBufferData(buffers[bufferIndex], wavefile.format, wavefile.data, wavefile.samplerate);

    // unload file again
    wavefile.dispose();

    // return index for this sound
  	return bufferIndex++;
  }

  public void destroy() {
    if(soundOutput) {

      // stop playing sounds
      scratchBuffer.position(0).limit(sources.length);
      scratchBuffer.put(sources).flip();
      AL10.alSourceStop(scratchBuffer);

      // destroy sources
      AL10.alDeleteSources(scratchBuffer);

      // destroy buffers
      scratchBuffer.position(0).limit(bufferIndex);
      scratchBuffer.put(buffers, 0, bufferIndex).flip();
      AL10.alDeleteBuffers(scratchBuffer);

      // destory OpenAL
    	AL.destroy();
    }
  }
}