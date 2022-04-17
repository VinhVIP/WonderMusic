package com.team28.wondermusic.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class StackOverFlowSite(
)

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonPlaceHolderSite()