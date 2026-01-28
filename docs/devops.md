 # DevOps Foundations

 ## Goals
 - Auto-deploy builds to a target Android device.
 - Capture in-app feedback and status logs.
 - Provide backend service hosting for pipeline tasks.
 - Make the system reachable by agents for testing.
- Keep PDF and EPUB content local-only.

 ## Auto-deploy to phone
 - Use internal testing (Firebase App Distribution or Play Internal Testing).
 - CI builds on merge to main or a release tag.
 - Notify via email/Slack with version + changelog.
- Validate Android system TTS offline voice availability during QA.

 ## Logging and telemetry
 - Structured logs with correlation IDs per conversion job.
 - Upload logs on app close or error.
 - Tag logs with device model and app version.
- Avoid uploading source documents or extracted content.

 ## In-app feedback
 - Simple feedback form with optional screenshot.
 - Attach recent logs and app state summary.
 - Store feedback in a backend service for triage.

 ## Backend service hosting
 - Provide a small API for:
   - conversion requests
   - asset upload/download
   - agent job scheduling (see agent-orchestration.md)
 - Deploy as a container on a small VM or managed service.
- Do not store PDFs/EPUBs; accept only metadata and logs.

 ## Agent access
 - Provide a test endpoint for end-to-end runs.
- Allow agents to run conversions against local files.
 - Rate limit and require API keys for safety.
