# AI Hub

All your AI assistants in one fast, clean Android app.

## Features
- 12+ AI tools: ChatGPT, Claude, Gemini, Grok, DeepSeek, Mistral, Perplexity, Copilot, HuggingChat, Meta AI, Poe, You.com
- **Multiple accounts** of the same AI — fully isolated sessions
- Lazy WebView loading — no lag on startup
- Background tab pause/resume — saves battery and memory
- Add custom AI URLs
- Material You dynamic theming
- Dark / Light / System theme
- Persistent logins across app restarts

## Building

### Debug APK (no signing needed)
```bash
./gradlew assembleDebug
```

### Release APK
Set up GitHub Secrets:
- `KEYSTORE_BASE64` — base64 encoded keystore file
- `KEYSTORE_PASSWORD` — keystore password
- `KEY_ALIAS` — key alias
- `KEY_PASSWORD` — key password

Then push a tag:
```bash
git tag v1.0.0
git push origin v1.0.0
```
GitHub Actions will build and publish the APK automatically.

## License
GPL-3.0
