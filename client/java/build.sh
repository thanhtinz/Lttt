#!/usr/bin/env bash
#
# Build the Avatar 2.5.8 J2ME client into a runnable MIDlet JAR.
#
# The source uses standard java.* classes (StringBuilder, Arrays, @Override, ...)
# that are NOT in pure CLDC, so the MIDP/CLDC stubs go on the *classpath*
# (not the bootclasspath) and java.* is resolved from the host JDK. The
# resulting JAR runs on desktop emulators (KEmulator / AngelChip) and, after
# preverification, on real MIDP-2.0 devices.
#
# Usage: ./build.sh   ->   dist/Avatar258.jar
set -euo pipefail
cd "$(dirname "$0")"

OUT_JAR="dist/Avatar258.jar"
CLASSES="build/classes"
STAGE="build/pack"

echo "==> Cleaning"
rm -rf "$CLASSES" "$STAGE"
mkdir -p "$CLASSES" "$STAGE" dist

LIBS=$(ls lib/*.jar | tr '\n' ':')
echo "==> Compiling (classpath stubs: $LIBS)"
find src -name '*.java' > build/sources.list
javac -encoding UTF-8 -classpath "$LIBS" -d "$CLASSES" @build/sources.list

echo "==> Staging classes + resources"
cp -r "$CLASSES"/. "$STAGE"/
# game resources / assets that ship inside the jar
cp -r resources/a.clazz resources/agent.txt resources/icon.png \
      resources/provider.txt resources/normal "$STAGE"/

echo "==> Packaging $OUT_JAR"
jar cfm "$OUT_JAR" resources/META-INF/MANIFEST.MF -C "$STAGE" .

echo "==> Done: $OUT_JAR"
ls -la "$OUT_JAR"
