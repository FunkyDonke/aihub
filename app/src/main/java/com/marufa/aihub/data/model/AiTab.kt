package com.marufa.aihub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "ai_tabs")
data class AiTab(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,           // e.g. "ChatGPT - Work"
    val url: String,            // e.g. "https://chatgpt.com"
    val toolKey: String,        // e.g. "chatgpt" — used for icon lookup
    val accountLabel: String = "",  // e.g. "Work Account"
    val sessionId: String = UUID.randomUUID().toString(), // unique per tab = isolated cookies
    val isEnabled: Boolean = true,
    val order: Int = 0
)
