package com.dropbox.kaiken.plugin

open class KaikenExtension {
  /**
   * Highly experimental feature that allows you to use Kaiken to generate Factory classes that
   * usually the Dagger annotation processor would generate for @Provides methods, @Inject
   * constructors and @Inject fields.
   *
   * The benefit of this feature is that you don't need to enable the Dagger annotation processor
   * in this module. That often means you can skip KAPT and the stub generating task. In addition
   * Kaiken generates Kotlin instead of Java code, which allows Gradle to skip the Java compilation
   * task. The result is faster builds.
   *
   * This feature can only be enabled in Gradle modules that don't compile any Dagger component.
   * Since Kaiken only processes Kotlin code, you shouldn't enable it in modules with mixed Kotlin /
   * Java sources either.
   *
   * By default this feature is disabled.
   */
  var generateDaggerFactories = false
}
