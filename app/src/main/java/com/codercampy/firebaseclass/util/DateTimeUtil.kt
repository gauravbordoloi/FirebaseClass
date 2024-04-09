package com.codercampy.firebaseclass.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatTimestamp(t: Long?): String {
    if (t == null) return ""
    return SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(t)
}

fun formatTimestampForChat(t: Long?): String {
    if (t == null) return ""
    return SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault()).format(t)
}