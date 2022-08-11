package com.vinh.wondermusic.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class StackOverFlowSite(
)

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonPlaceHolderSite()