package avt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Message {
   public byte command;
   private ByteArrayOutputStream b = null;
   private DataOutputStream dos = null;
   private ByteArrayInputStream d = null;
   private DataInputStream dis = null;

   public Message() {
   }

   public Message(byte var1) {
      this.command = var1;
      this.b = new ByteArrayOutputStream();
      this.dos = new DataOutputStream(this.b);
   }

   public Message(byte var1, byte[] var2) {
      this.command = var1;
      this.d = new ByteArrayInputStream(var2);
      this.dis = new DataInputStream(this.d);
   }

   public final byte[] getData() {
      return this.b.toByteArray();
   }

   public final DataInputStream reader() {
      return this.dis;
   }

   public final DataOutputStream writer() {
      return this.dos;
   }

   public final void cleanup() {
      try {
         if (this.dis != null) {
            this.dis.close();
         }

         if (this.dos != null) {
            this.dos.close();
            return;
         }
      } catch (IOException var2) {
      }

   }
}
