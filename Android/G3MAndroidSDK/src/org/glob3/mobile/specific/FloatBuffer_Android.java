

package org.glob3.mobile.specific;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.glob3.mobile.generated.IFloatBuffer;


public final class FloatBuffer_Android
         extends
            IFloatBuffer {

   private final FloatBuffer _buffer;
   private int               _timestamp;


   //   private boolean           _hasGLBuffer = false;
   //   private int               _glBuffer;


   public FloatBuffer_Android(final int size) {
      _buffer = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
   }


   public FloatBuffer_Android(final float f0,
                              final float f1,
                              final float f2,
                              final float f3,
                              final float f4,
                              final float f5,
                              final float f6,
                              final float f7,
                              final float f8,
                              final float f9,
                              final float f10,
                              final float f11,
                              final float f12,
                              final float f13,
                              final float f14,
                              final float f15) {
      _buffer = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      _buffer.put(0, f0);
      _buffer.put(1, f1);
      _buffer.put(2, f2);
      _buffer.put(3, f3);
      _buffer.put(4, f4);
      _buffer.put(5, f5);
      _buffer.put(6, f6);
      _buffer.put(7, f7);
      _buffer.put(8, f8);
      _buffer.put(9, f9);
      _buffer.put(10, f10);
      _buffer.put(11, f11);
      _buffer.put(12, f12);
      _buffer.put(13, f13);
      _buffer.put(14, f14);
      _buffer.put(15, f15);
   }


   public FloatBuffer_Android(final float[] array) {
      _buffer = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      _buffer.put(array);
      _buffer.rewind();
   }


   @Override
   public int size() {
      return _buffer.capacity();
   }


   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public float get(final int i) {
      return _buffer.get(i);
   }


   @Override
   public void put(final int i,
                   final float value) {
      if (_buffer.get(i) != value) {
         _buffer.put(i, value);
         _timestamp++;
      }
   }


   @Override
   public void rawPut(final int i,
                      final float value) {
      _buffer.put(i, value);
   }


   public FloatBuffer getBuffer() {
      return _buffer;
   }


   @Override
   public String description() {
      return "FloatBuffer_Android(timestamp=" + _timestamp + ", buffer=" + _buffer + ")";
   }


   //   @Override
   //   public void dispose() {
   //      super.dispose();
   //
   //      if (_hasGLBuffer) {
   //         final int[] buffers = new int[] { _glBuffer };
   //         GLES20.glDeleteBuffers(1, buffers, 0);
   //         _hasGLBuffer = false;
   //      }
   //   }
   //
   //
   //   public int getGLBuffer() {
   //      if (!_hasGLBuffer) {
   //         final int[] buffers = new int[1];
   //         GLES20.glGenBuffers(1, buffers, 0);
   //         _glBuffer = buffers[0];
   //         _hasGLBuffer = true;
   //      }
   //
   //      return _glBuffer;
   //   }

}
