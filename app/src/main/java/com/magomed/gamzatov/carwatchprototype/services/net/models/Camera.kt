package com.magomed.gamzatov.carwatchprototype.services.net.models

data class Camera(val id:Long, val url: String, val description:String,
                  val latitude: Double, val longitude: Double, val maxCars: Long,
                  val curCars: Long, val imgCount: Long, val height: Int, val width: Int)