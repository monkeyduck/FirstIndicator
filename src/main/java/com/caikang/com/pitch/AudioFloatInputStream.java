
package com.caikang.com.pitch;

import java.io.IOException;
import java.io.InputStream;


public abstract class AudioFloatInputStream {
	
	private static class DirectAudioFloatInputStream extends AudioFloatInputStream {
		@Override
		public long getFrameLength() throws IOException {
			return stream.available() / 2;
    }

    @Override
		public int read(float b[], int off, int len) throws IOException {
    	int b_len = len * framesize_pc;
      if (buffer == null || buffer.length < b_len)
      	buffer = new byte[b_len];
      int ret = stream.read(buffer, 0, b_len);
      if (ret == -1) {
      	return -1;
      } 
      else {
      	converter.toFloatArray(buffer, b, off, ret / framesize_pc);
        return ret / framesize_pc;
      }
    }

    @Override
		public long skip(long len) throws IOException {
    	long b_len = len * framesize_pc;
      long ret = stream.skip(b_len);
      if (ret == -1L)
      	return -1L;
      else
      	return ret / framesize_pc;
    }
    
    @Override
		public int available() throws IOException {
    	return stream.available() / framesize_pc;
    }

    @Override
		public void close() throws IOException {
    	stream.close();
    }

    @Override
		public void mark(int readlimit) {
    	stream.mark(readlimit * framesize_pc);
    }

    @Override
		public boolean markSupported() {
    	return stream.markSupported();
    }

    @Override
		public void reset() throws IOException {
    	stream.reset();
    }

    private final InputStream stream;
    private AudioFloatConverter converter;
    private final int framesize_pc;
    private byte buffer[];

    public DirectAudioFloatInputStream(InputStream stream) {
    	converter = AudioFloatConverter.getConverter();
      if (converter == null) {
        converter = AudioFloatConverter.getConverter();
      }
      framesize_pc = 2;
      this.stream = stream;
    }
  }

  public AudioFloatInputStream() {
    normalized = true;
  }

  public static AudioFloatInputStream getInputStream(InputStream stream) throws IOException {
    return new DirectAudioFloatInputStream(stream);
  }
    
  public float getSampleScale() {
    return 1.0F;
  }

  public void setNormalized(boolean normalized) {
    this.normalized = normalized;
  }

  public boolean isNormalized() {
  	return normalized;
  }

  public abstract long getFrameLength() throws IOException;

  public abstract int read(float af[], int i, int j) throws IOException;

  public int read(float b[]) throws IOException {
  	return read(b, 0, b.length);
  }

  public float read() throws IOException {
    float b[] = new float[1];
    int ret = read(b, 0, 1);
    if(ret == -1 || ret == 0)
    	return 0.0F;
    else
      return b[0];
  }

  public abstract long skip(long l) throws IOException;

  public abstract int available() throws IOException;

  public abstract void close() throws IOException;

  public abstract void mark(int i);

  public abstract boolean markSupported();

  public abstract void reset() throws IOException;

  protected boolean normalized;
}
