package com.dropbox.kaiken.skeleton.core

import com.dropbox.kaiken.skeleton.dagger.SdkSpec
import com.dropbox.kaiken.skeleton.dependencymanagement.SkeletonScopedServices

class AppSkeletonInitializer(val appSkeletonDelegate: AppSkeletonScopedServices) :
    SkeletonScopedServices by appSkeletonDelegate {

    init {
        userServicesProvider = KaikenUserServicesProvider(
            provideAppServices(),
            userServicesFactory
        )
    }

    companion object {
        internal lateinit var appSkeleton: AppSkeletonInitializer

        /**
         * WARNING
         *
         * As tempting as it may be. DO NOT ever increase the visibility of this method. No one should be able to
         * initialize except the Skeleton itself.
         *
         * WARNING
         */
        internal fun init(
            component: SdkSpec,
        ): AppSkeletonInitializer {
            check(!this::appSkeleton.isInitialized)
            appSkeleton = AppSkeletonInitializer(AppSkeletonScopedServices(component))
            return appSkeleton
        }

        /**
         * WARNING
         *
         * As tempting as it may be. DO NOT ever increase the visibility of this method. No one should be able to
         * directly obtain an instance of this object except for Skeleton code itself.
         *
         * WARNING
         */
        fun getInstance(): AppSkeletonInitializer {
            check(this::appSkeleton.isInitialized)
            return appSkeleton
        }
    }
}
