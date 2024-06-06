package com.example.bookwander.BookWanderApplication

import android.app.Application
import com.example.bookwander.data.AppContainer
import com.example.bookwander.data.DefaultContainer

class BookWanderApplication: Application() {
    // We initialize a container that take our AppContainer as a datatype
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer()
    }
}