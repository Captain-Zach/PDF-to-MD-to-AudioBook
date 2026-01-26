 # DevOps Foundations
 
 ## Goals
 - Auto-deploy builds to a target Android device.
 - Capture in-app feedback and status logs.
 - Provide backend service hosting for pipeline tasks.
 - Make the system reachable by agents for testing.
 
 ## Auto-deploy to phone
 - Use internal testing (Firebase App Distribution or Play Internal Testing).
 - CI builds on merge to main or a release tag.
 - Notify via email/Slack with version + changelog.
 
 ## Logging and telemetry
 - Structured logs with correlation IDs per conversion job.
 - Upload logs on app close or error.
 - Tag logs with device model and app version.
 
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
 
 ## Agent access
 - Provide a test endpoint for end-to-end runs.
 - Allow agents to upload PDFs and retrieve outputs.
 - Rate limit and require API keys for safety.
