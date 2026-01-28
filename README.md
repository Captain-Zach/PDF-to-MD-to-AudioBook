# PDF-to-MD-to-AudioBook

Convert PDFs and EPUBs into clean Markdown and audiobook audio with aligned chunk timestamps. The application is a native Android app that processes documents locally on your device.

## Features

- **Local Conversion**: Convert PDF and EPUB files to Markdown entirely on-device. No data is uploaded to the cloud.
- **Audio Playback**: Listen to the converted text using Android's system Text-to-Speech (TTS).
- **Chunk Alignment**: Audio is generated with chunk alignment for reliable navigation.
- **Privacy Focused**: Strictly local processing ensures your documents remain private.

## Constraints

- PDFs and EPUBs are processed locally only.
- Handwriting recognition is out of scope.
- Target device: Android 8.0 (API 26) and above.

## Installation

### Prerequisites

- **JDK 17**: Ensure you have Java Development Kit 17 installed.
- **Android Studio**: Recommended for development.
- **Android SDK**: Target SDK 34, Min SDK 26.

### Build from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/pdf2audio.git
   cd pdf2audio
   ```

2. Build the project using the Gradle wrapper:
   ```bash
   ./gradlew assembleDebug
   ```

3. Install on a connected device or emulator:
   ```bash
   ./gradlew installDebug
   ```

## Usage

1. Open the **PDF2Audio** app on your Android device.
2. Tap the **Pick File** button to select a PDF or EPUB file from your device storage.
3. The app will begin converting the file in the background. Status updates will appear on the screen.
4. Once conversion is complete, the **Play** button will become active.
5. Tap **Play** to start listening. The app highlights the current chunk being read.
6. Use **Pause** to stop playback.

## Docs

- [Product requirements](docs/prd.md)
- [Initial plan (milestone-based)](docs/initial-plan.md)
- [Agent orchestration](docs/agent-orchestration.md)
- [DevOps foundations](docs/devops.md)
- [Pipeline output schema](contracts/pipeline-output.schema.json)

## Status

**MVP**: Basic conversion (PDF/EPUB to Markdown) and TTS playback are implemented. The app supports selecting files, background conversion, and simple playback controls.

## Contributing

We welcome contributions! Please read our [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to submit pull requests, report bugs, and suggest features.
