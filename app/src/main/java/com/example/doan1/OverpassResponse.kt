package com.example.doan1

data class OverpassResponse(
    val elements: List<Element>
)

data class Element(
    val id: Long,
    val type: String,
    val lat: Double?,   // Vĩ độ (cho node)
    val lon: Double?,   // Kinh độ (cho node)
    val center: Center?, // Vị trí trung tâm (cho way/relation khi dùng out center)
    val tags: Map<String, String>? // Chứa tên, loại nhà hàng, địa chỉ...
)

data class Center(
    val lat: Double,
    val lon: Double
)
