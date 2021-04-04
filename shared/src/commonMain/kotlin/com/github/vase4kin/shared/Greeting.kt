package com.github.vase4kin.shared

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}