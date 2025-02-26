package com.dropbox.kaiken.skeleton.core

import com.dropbox.kaiken.scoping.AppServices
import com.dropbox.kaiken.scoping.UserServices
import com.dropbox.kaiken.scoping.UserServicesProvider
import com.dropbox.kaiken.skeleton.dagger.SdkSpec
import com.dropbox.kaiken.skeleton.dependencymanagement.SkeletonScopedServices

class AppSkeletonScopedServices constructor(
    override val component: SdkSpec
) : SkeletonScopedServices {

    val appServices: AppServices = component.getSkeletonConfig()
        .scopedServicesFactory
        .createAppServices(
            component
        )

    override fun provideAppServices(): AppServices = appServices

    override val userServicesFactory = { appServices: AppServices, user: SkeletonUser ->
        component.getSkeletonConfig().scopedServicesFactory.createUserServices(
            appServices,
            user
        )
    }

    override lateinit var userServicesProvider: UserServicesProvider

    override fun provideUserServicesOf(userId: String): UserServices? =
        userServicesProvider.provideUserServicesOf(userId)
}
