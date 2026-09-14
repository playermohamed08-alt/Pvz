# Garden Defense 32

Original PvZ-style tower-defense prototype for Android, targeted to `armeabi-v7a` only.

## Current prototype
- 5 lanes
- 50 selectable original plant slots
- sun/cost system
- zombies with HP bars
- projectiles and basic waves
- touch controls
- 32-bit ARM ABI split

This project does **not** contain Plants vs. Zombies Fusion assets or code. It is an original prototype inspired by the tower-defense genre.

## Build
The included GitHub Actions workflow installs Android SDK/NDK and builds an `armeabi-v7a` release APK. GitHub Actions can run workflows on GitHub-hosted virtual machines and upload the APK as an artifact.
