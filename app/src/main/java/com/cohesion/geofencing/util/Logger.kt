package com.cohesion.geofencing


/**
 * Created by Er Nadeem Bhat on 26/3/21
 *Time : 16 :10
 *Project Name: Geo Fencing
 *Company: Mobinius Technology Pvt Ltd.
 *Email: nadeem.nb@mobinius.com
 * Copyright (c)
 */
data class Logger(var level:LogLevel,var error:String)
enum  class LogLevel {
    Info,Error
}


