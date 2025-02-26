package com.dropbox.kaiken.sample.advancedsample.app

import com.dropbox.common.inject.AppScope
import com.dropbox.common.inject.UserScope
import com.dropbox.kaiken.sample.advancedsample.helloworldfeature.HelloWorldMessageProviderUser
import com.dropbox.kaiken.sample.advancedsample.helloworldfeature.RealWorldMessageProviderUser
import com.dropbox.kaiken.sample.advancedsample.helloworldfeature.TimeMessageProvider
import com.dropbox.kaiken.sample.advancedsample.helloworldfeature.UserProfile
import com.dropbox.kaiken.scoping.UserTeardownHelper
import com.dropbox.kaiken.skeleton.scoping.SingleIn
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Inject

// App Services that are need to instance user services
@ContributesTo(AppScope::class)
interface CoreAppServices {
    val timeMessageProvider: TimeMessageProvider
}

@ContributesTo(UserScope::class)
@Module
object UserServicesModule {
    @Provides
    @SingleIn(UserScope::class)
    fun provideHelloWorldMessageProvider(userProfile: UserProfile): HelloWorldMessageProviderUser =
        RealWorldMessageProviderUser("Hello ${userProfile.name}!")
}

// You can implement your own teardown logic here.
@ContributesBinding(UserScope::class)
@SingleIn(UserScope::class)
class AnotherNoOpTeardownHelper @Inject constructor() : UserTeardownHelper {
    override fun teardown() {
        // No op
    }
}
