package com.gimbal.kotlin.ouicannes.data.model

enum class EventType{
    PLACE_ENTER,
    PLACE_EXIT
}
data class GimbalEvent(val type: EventType, val placeName: String, val dwellTime : Long ,val points : Int)
