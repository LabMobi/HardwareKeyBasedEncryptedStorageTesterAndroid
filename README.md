Mobi Lab

# Hardware Key Based Encrypted Storage Tester README

## Description

As simple push-a-button tester to see if the EncryptedSharedPreferences based storage is operational on a given device. If not then allows to easily post logs about the failure.

## Running it

There are the following options

### A. Take a build from GitHub releases

1. Take the latest debug build from GitHub releases.

   1. Releases:

      https://github.com/LabMobi/HardwareKeyBasedEncryptedStorageTesterAndroid/releases

      1. You can direct-download or scan a download QR there.

2. Install it

3. Run it via `Test storage` button

### B1. Clone and compile yourself via Android Studio

1. Clone the repository
2. Switch to master branch
3. Compile and run the debug build variant
4. Run it via `Test storage` button

### B2. Clone and compile from command line

1. Clone the repository

2. Switch to master branch

3. Install via Gradle:

   ```
   .\gradlew installDebug
   ```

4. Run it via `Test storage` button



## Posting logs

Use the button from the Toolbar and chose a destination to share to log file to.



## Building code

Full build can be done via:

```
.\gradlew buildAllRelease
```

This builds all variants and runs all linters.

Code linters (Detekt, ktlint) can be also ran separately via:

```
.\gradlew checkCode
```

NOTE: This skips the Android Lint as that takes a long time to run and rarely has something to say.

## Contact

### Mobi Lab

Email: [hello@lab.mobi](mailto:hello@lab.mobi)

Twitter: https://mobile.twitter.com/LabMobi

Web: https://lab.mobi/
