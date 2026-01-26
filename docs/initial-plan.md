 # Initial Plan (Milestone-Based)
 
 This is a milestone plan, not a sprint plan. It is optimized for asynchronous
 agent collaboration with clear contracts and handoffs.
 
 ## Milestone 0: Alignment and Contracts
 **Goal:** define scope and interfaces so multiple agents can work in parallel.
 - PRD and success metrics
 - Data contracts (MD, images, audio manifests)
 - UX flow for single-button playback
 - Risk register and tech decisions log
 
 **Primary outputs**
 - docs/prd.md
 - contracts/pipeline-output.schema.json
 - docs/ux-flow.md
 
 ## Milestone 1: DevOps and Repo Scaffold
 **Goal:** enable safe parallel work and continuous delivery.
 - Repo structure, CLI/API shell
 - CI, lint, and test harness
 - Auto-deploy pipeline to Android device
 - Logging, telemetry, and feedback pipeline
 
 **Primary outputs**
 - docs/devops.md
 - CI pipeline config
 - Mobile build pipeline
 
 ## Milestone 2: PDF to Markdown + Images
 **Goal:** reliable extraction and normalization.
 - PDF text extraction
 - OCR path for scanned pages
 - Markdown normalization and chapter segmentation
 - Image extraction and manifest generation
 
 **Primary outputs**
 - content.md
 - images.json
 - tests/fixtures for sample PDFs
 
 ## Milestone 3: Markdown to Audio + Alignment
 **Goal:** audio generation with chunk timestamps.
 - TTS adapter layer
 - Chunking and timestamp correlation
 - Resume/recovery markers
 
 **Primary outputs**
 - audio_manifest.json
 - Chapter audio files
 
 ## Milestone 4: Mobile Playback App
 **Goal:** listener-first experience with one-button control.
 - Playback UI and single-button flow
 - Chunk markers and feedback capture
 - Offline cache and resume
 
 **Primary outputs**
 - Android app MVP
 - Logs + in-app feedback
 
 ## Milestone 5: Agent Test Harness
 **Goal:** allow automated agents to test end-to-end.
 - Agent-friendly API or CLI for conversions
 - Synthetic test packs and regression checks
 - Device performance benchmarks
 
 **Primary outputs**
 - QA scripts
 - Benchmark report
 - Agent integration guide
 
 ## Milestone 6: RAG Export and Polish
 **Goal:** deliver RAG-ready outputs and docs.
 - Export cleaned MD + metadata for RAG
 - Embedding hooks (optional)
 - Docs, examples, and release checklist
