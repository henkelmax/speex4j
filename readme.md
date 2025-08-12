# Speex4J

A Java wrapper for [Speex](https://www.speex.org/) written in C using JNI.

Java 8+ is required to use this library.

## Supported Platforms

- `Windows x86_64`
- `Windows aarch64`
- `macOS x86_64`
- `macOS aarch64`
- `Linux x86_64`
- `Linux aarch64`

## Usage

**Maven**

``` xml
<dependency>
  <groupId>de.maxhenkel.speex4j</groupId>
  <artifactId>speex4j</artifactId>
  <version>1.0.0</version>
</dependency>

<repositories>
  <repository>
    <id>henkelmax.public</id>
    <url>https://maven.maxhenkel.de/repository/public</url>
  </repository>
</repositories>
```

**Gradle**

``` groovy
dependencies {
  implementation 'de.maxhenkel.speex4j:speex4j:1.0.0'
}

repositories {
  maven {
    name = "henkelmax.public"
    url = 'https://maven.maxhenkel.de/repository/public'
  }
}
```

## Example Code

``` java
try(AutomaticGainControl agc = new AutomaticGainControl(960, 48000)) {
  short[] audio = ...;
  agc.agc(audio);
}
```

## Building from Source

### Prerequisites

- [Java](https://www.java.com/en/) 21
- [Zig](https://ziglang.org/) 0.14.1
- [Ninja](https://ninja-build.org/)

### Building

``` bash
./gradlew build
```

## Credits

- [Speex](https://github.com/xiph/speexdsp)
