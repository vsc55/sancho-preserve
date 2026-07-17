/*
 * Reconstructed from the decompiled 0.9.4-59 binary.
 *
 * The original Prov class force-linked GCJ / GNU Classpath internals
 * (gnu.gcj.convert.*, gnu.javax.crypto.*, gnu.java.net.*, ...) so the native
 * GCJ Windows build would bundle them. Those classes do not exist on a standard
 * JVM. Sancho only touches this class under `if (VersionInfo.isGNU() && Prov.x == 2)`
 * (see Sancho.java), which is never true on a stock JRE, so this is a no-op stub
 * that keeps the reference compiling.
 */
package sancho.core;

public class Prov {
  public static int x = 1;

  public static void loadStuff() {
    // no-op on a standard JVM (GCJ-only bootstrap in the original)
  }
}
