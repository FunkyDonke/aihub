package com.marufa.aihub.data.model

data class AiTool(
    val key: String,
    val displayName: String,
    val url: String,
    val iconUrl: String = ""
)

object AiTools {
    val all = listOf(
        AiTool("chatgpt",     "ChatGPT",        "https://chatgpt.com"),
        AiTool("claude",      "Claude",          "https://claude.ai"),
        AiTool("gemini",      "Gemini",          "https://gemini.google.com"),
        AiTool("grok",        "Grok",            "https://grok.com"),
        AiTool("deepseek",    "DeepSeek",        "https://chat.deepseek.com"),
        AiTool("mistral",     "Mistral",         "https://chat.mistral.ai"),
        AiTool("perplexity",  "Perplexity",      "https://www.perplexity.ai"),
        AiTool("copilot",     "Copilot",         "https://copilot.microsoft.com"),
        AiTool("huggingchat", "HuggingChat",     "https://huggingface.co/chat"),
        AiTool("metaai",      "Meta AI",         "https://www.meta.ai"),
        AiTool("poe",         "Poe",             "https://poe.com"),
        AiTool("you",         "You.com",         "https://you.com"),
        AiTool("custom",      "Custom",          "")
    )

    fun findByKey(key: String) = all.find { it.key == key }
}
