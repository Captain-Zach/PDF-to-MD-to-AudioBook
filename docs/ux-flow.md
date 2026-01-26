 # UX Flow (Single-Button Playback)
 
 ## Core interaction
 - Single primary button controls play/pause.
 - Long press marks a chunk boundary or adds a bookmark.
 - Double tap skips to next chunk.
 
 ## Playback states
 - idle -> playing -> paused
 - resume from last chunk marker
 - handle missing audio by skipping to next chunk
 
 ## Feedback capture
 - "Report issue" from player menu
 - attaches current chunk ID and timestamp
 - optional short text note
