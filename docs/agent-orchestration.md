 # Agent Orchestration and "Requesting Agents"
 
 ## Problem
 We want a way to request and coordinate asynchronous agents (LLM workers) to
 run tasks like PDF conversion, OCR evaluation, TTS quality checks, or regression
 validation. The repo currently has no mechanism to schedule or track these
 tasks.
 
 ## Proposed solution
 Build an "agent control plane" that exposes a small API for creating and
 monitoring agent jobs. Agents consume jobs from a queue, run the task, and
 write results back to a shared artifact store.
 
 ### Minimal API (v1)
 - `POST /agent/jobs`
   - Request a new agent run.
 - `GET /agent/jobs/{job_id}`
   - Get status, logs, and outputs.
 - `POST /agent/jobs/{job_id}/cancel`
   - Stop an agent job.
 
 ### Job payload
 ```json
 {
   "job_type": "pdf_to_md | ocr_eval | tts_eval | e2e_test",
   "input_uri": "s3://bucket/input.pdf",
   "output_uri": "s3://bucket/job-outputs/",
   "constraints": {
     "max_cost_usd": 5.0,
     "max_duration_sec": 900
   },
   "metadata": {
     "requested_by": "ci | user | agent",
     "priority": "low | normal | high"
   }
 }
 ```
 
 ### Agent job lifecycle
 - queued -> running -> succeeded | failed | cancelled
 - store partial results on failure for debugging
 
 ## Storage and queue options
 - Queue: Redis, RabbitMQ, or a Postgres-backed queue.
 - Storage: S3-compatible object store for artifacts and logs.
 - Status: persisted in a database for audit and retry.
 
 ## Integration points
 - CI can request an agent run for nightly regression tests.
 - The mobile app can submit a PDF conversion request.
 - Agents can chain runs, e.g. OCR eval -> re-run with different settings.
 
 ## MVP scope
 - Single worker process that pulls jobs from a queue.
 - Job definitions stored in a database table.
 - Output artifacts stored with job-scoped prefixes.
 - Basic admin UI or CLI to list jobs.
