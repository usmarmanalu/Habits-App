package com.dicoding.habitapp.setting

import com.dicoding.habitapp.data.repository.*
import org.koin.dsl.*

val storageModule = module {
    factory {
        SessionManager(get())
    }

    factory {
        UserRepository(get())
    }
}