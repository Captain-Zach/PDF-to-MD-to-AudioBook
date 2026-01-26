 # PDF-to-MD-to-AudioBook
 
 ## Problem statement
 People want to consume long-form PDFs as audiobooks, but most PDFs are not
 designed for audio. A usable audiobook requires clean structure, extracted
 images/captions, and aligned audio chunks so listeners can resume, navigate,
 and search reliably.
 
 ## Goals
 - Convert a PDF into clean, structured Markdown with image manifests.
 - Generate audiobook audio with chunk/timestamp alignment.
 - Provide a mobile playback app with a simple, single-button control flow.
 - Support agent-driven testing and automated QA workflows.
 - Provide a way to request and track agent runs.
 - Provide DevOps basics: auto-deploy to device, telemetry, and feedback capture.
 
 ## Non-goals (initial release)
 - Full editorial correction of OCR errors.
 - Full desktop app or web reader.
 - Multi-user accounts and social features.
 
 ## Target users
 - Listeners who prefer audio for long-form documents.
 - Builders who want a repeatable PDF to audio pipeline.
 - Agents and QA systems that need machine-readable outputs for testing.
 
 ## Assumptions
 - The MVP will be Android-first (Moto Stylus G 2024 target device).
 - PDFs may include images and scanned pages.
 - LLM usage is allowed for transformation and cleanup.
 - TTS may be cloud-based initially, with an offline path later.
 
 ## Success metrics
 - PDF to audio conversion completes without manual intervention for 80% of
   tested PDFs.
 - 90% of chapter start points align with correct audio timestamps.
 - Time-to-first-audio under 2 minutes for a 30-page PDF on baseline hardware.
 - App playback resumes accurately within 2 seconds of the prior position.
 
 ## MVP scope
 - PDF extraction to Markdown with chapter segmentation.
 - Image extraction with simple captions.
 - Audio generation with chunk markers and timestamps.
 - Android playback app with single-button control.
 - Telemetry, logging, and in-app feedback.
 - Agent-accessible test harness (API or CLI).
 - Agent job API for scheduling and monitoring runs.
 
 ## Future scope
 - Offline TTS and offline OCR for privacy-first usage.
 - Advanced image description via vision models.
 - RAG-ready embedding exports and search index.
 - Multi-language support and per-voice personalization.
 
 ## Risks
 - OCR quality variance on scanned PDFs and handwriting.
 - Cost and latency for cloud TTS at scale.
 - Inconsistent heading detection leading to poor chapterization.
 - Device resource constraints for on-device models.
