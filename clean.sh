#!/bin/bash

# Delete IntelliJ project files
find . -name "*.iml" -type f -delete
rm -rf .idea

# Delete Gradle build folders
rm -rf .gradle
find . -type d -name "build" -exec rm -rf {} +

# Delete logs and local env files
find . -name "*.log" -type f -delete
find . -name ".DS_Store" -type f -delete

# Delete Kotlin output folders
find . -type d -name "out" -exec rm -rf {} +

# Delete Windows and macOS metadata files
find . -name "Thumbs.db" -type f -delete
find . -name "ehthumbs.db" -type f -delete
find . -name ".AppleDouble" -type f -delete
find . -name ".LSOverride" -type f -delete

# Delete compiled files and archives
find . -name "*.class" -type f -delete
find . -name "*.jar" -type f -delete
find . -name "*.war" -type f -delete
find . -name "*.ear" -type f -delete
