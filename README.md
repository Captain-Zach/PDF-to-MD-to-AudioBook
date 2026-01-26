# PDF-to-MD-to-AudioBook
 
Convert PDFs and EPUBs into clean Markdown and audiobook audio with aligned
chunk timestamps. This repo is in early setup and focuses on:
 
- PDF and EPUB to Markdown extraction and cleanup
- Image extraction and manifests
- Markdown to audiobook generation with chunk alignment (offline TTS)
- A native Android playback app with single-button control
- DevOps foundations (auto-deploy, logging, feedback)
- Agent-accessible testing and orchestration

Constraints:
- PDFs and EPUBs are processed locally only (no upload).
- Handwriting recognition is out of scope.
 
## Docs
- [Product requirements](docs/prd.md)
- [Initial plan (milestone-based)](docs/initial-plan.md)
- [Agent orchestration](docs/agent-orchestration.md)
- [DevOps foundations](docs/devops.md)
- [Pipeline output schema](contracts/pipeline-output.schema.json)
 
## Status
Scaffolding docs and contracts are in place. Implementation starts next.
