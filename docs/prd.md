 # PDF-to-MD-to-AudioBook

 ## Problem statement
 People want to consume long-form PDFs as audiobooks, but most PDFs are not
 designed for audio. A usable audiobook requires clean structure, extracted
 images/captions, and aligned audio chunks so listeners can resume, navigate,
 and search reliably.

## Goals
- Convert PDFs into clean, structured Markdown with image manifests.
- Convert EPUBs into clean Markdown (MD-only for EPUB in MVP).
 - Generate audiobook audio with chunk/timestamp alignment.
 - Provide a mobile playback app with a simple, single-button control flow.
 - Support agent-driven testing and automated QA workflows.
 - Provide a way to request and track agent runs.
 - Provide DevOps basics: auto-deploy to device, telemetry, and feedback capture.

## Non-goals (initial release)
- Handwriting recognition.
- Cloud-based PDF/EPUB processing (local-only for now).
- Full editorial correction of OCR errors.
 - Full desktop app or web reader.
 - Multi-user accounts and social features.

 ## Target users
 - Listeners who prefer audio for long-form documents.
 - Builders who want a repeatable PDF to audio pipeline.
 - Agents and QA systems that need machine-readable outputs for testing.

## Assumptions
- The MVP will be native Android-first (Moto Stylus G 2024 target device).
- PDFs and EPUBs are processed locally on device or local machine.
- PDFs may include images and scanned pages; handwriting is out of scope.
- LLM usage is allowed for transformation and cleanup when running locally.
- TTS must be offline/on-device for MVP using Android system TTS.
- Target content is primarily technical documents and books.

 ## Success metrics
 - PDF to audio conversion completes without manual intervention for 80% of
   tested PDFs.
 - 90% of chapter start points align with correct audio timestamps.
 - Time-to-first-audio under 2 minutes for a 30-page PDF on baseline hardware.
 - App playback resumes accurately within 2 seconds of the prior position.
- EPUB to clean Markdown conversion completes for 90% of tested EPUBs.

 ## MVP scope
 - PDF extraction to Markdown with chapter segmentation.
- EPUB extraction to clean Markdown (no audio for EPUB in MVP).
 - Image extraction with simple captions.
- Audio generation with chunk markers and timestamps using Android system TTS.
 - Android playback app with single-button control.
 - Telemetry, logging, and in-app feedback.
 - Agent-accessible test harness (API or CLI).
 - Agent job API for scheduling and monitoring runs.

 ## Future scope
 - Advanced image description via vision models.
 - RAG-ready embedding exports and search index.
 - Multi-language support and per-voice personalization.

 ## Risks
- OCR quality variance on scanned PDFs.
- Offline TTS model quality and device performance.
 - Inconsistent heading detection leading to poor chapterization.
 - Device resource constraints for on-device models.
