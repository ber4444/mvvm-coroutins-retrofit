package com.server.pojo

data class GalleryImage(
        val ImageUrls: ImageUrls,
        val OriginalHeight: Int,
        val OriginalWidth: Int,
        var Checked: Boolean = false
)