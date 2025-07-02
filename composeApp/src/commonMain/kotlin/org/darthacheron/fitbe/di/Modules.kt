package org.darthacheron.fitbe.di

import org.darthacheron.fitbe.dependencies.MyRepository
import org.darthacheron.fitbe.dependencies.MyRepositoryImp
import org.darthacheron.fitbe.dependencies.MyViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
//    single {
//        MyRepositoryImp(get())
//    }.bind<MyRepository>()
    singleOf(::MyRepositoryImp).bind<MyRepository>()
    viewModelOf(::MyViewModel)
}