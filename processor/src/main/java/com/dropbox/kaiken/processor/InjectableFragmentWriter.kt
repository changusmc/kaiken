package com.dropbox.kaiken.processor

import com.dropbox.kaiken.processor.internal.GENERATED_BY_TOP_COMMENT
import com.squareup.javapoet.TypeName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.Filer
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

/**
 * Generates a `Injector` interface definition and an `inject` function for the given annotated
 * fragment.
 *
 * Example:
 *
 * ```
 * @InjectableFragment
 * public class MyFragment : Fragment {
 *     ...
 * }
 * ```
 *
 * will generate:
 *
 * ```
 * interface MyFragmentInjector {
 *    void inject(fragment: MyFragment)
 * }
 *
 * fun MyFragment.inject() {
 *     val injector: MyFragmentInjector = findInjector()
 *     injector.inject(this)
 * }
 * ```
 *
 * Note that the interface definition is written in Java this is because of an issue with Dagger
 * (https://github.com/google/dagger/issues/665). Dagger is not auto-generating code for
 * autogenerated interfaces written in Kotlin, so leave the interface definition in Java for now.
 */
internal class InjectableFragmentWriter(
    private val filer: Filer,
    private val elementUtils: Elements
) {

    fun write(annotatedFragment: InjectableAnnotatedFragment) {
        val annotatedFragmentTypeElement = annotatedFragment.annotatedFragmentElement
        val fragmentInjectorInterfaceName = resolveInterfaceName(annotatedFragment)
        val pack = elementUtils.getPackageOf(annotatedFragmentTypeElement).toString()
        val annotatedFragmentType = annotatedFragmentTypeElement.asType()

        writeInterfaceFile(pack, fragmentInjectorInterfaceName, annotatedFragmentType)
        writeExtensionFunctionFile(pack, fragmentInjectorInterfaceName, annotatedFragmentType)
    }

    private fun writeInterfaceFile(
        pack: String,
        interfaceName: String,
        fragmentType: TypeMirror
    ) {
        val interfaceFileSpec = generateInjectorInterfaceFileSpec(
            pack, interfaceName, "fragment", TypeName.get(fragmentType)
        )
        interfaceFileSpec.writeTo(filer)
    }

    private fun writeExtensionFunctionFile(
        pack: String,
        interfaceName: String,
        fragmentType: TypeMirror
    ) {
        val extensionFunctionFileSpec = generateExtensionFunctionFileSpec(
            pack, interfaceName, fragmentType
        )

        extensionFunctionFileSpec.writeTo(filer)
    }
}

private fun generateExtensionFunctionFileSpec(
    pack: String,
    interfaceName: String,
    fragmentType: TypeMirror
): FileSpec {
    val extensionFunctionSpec = generateInjectExtensionFunction(interfaceName, fragmentType)

    val fileBuilder = FileSpec.builder(pack, interfaceName)

    return fileBuilder.addComment(GENERATED_BY_TOP_COMMENT)
        .addImport("com.dropbox.kaiken.runtime", "findInjector")
        .addFunction(extensionFunctionSpec)
        .build()
}

private fun resolveInterfaceName(
    annotatedFragment: InjectableAnnotatedFragment
): String = resolveInjectorInterfaceName(annotatedFragment.annotatedFragmentElement)

private fun generateInjectExtensionFunction(
    interfaceName: String,
    fragmentType: TypeMirror
): FunSpec {
    return FunSpec.builder("inject")
        .receiver(fragmentType.asTypeName())
        .addStatement("val injector: $interfaceName = findInjector()")
        .addStatement("injector.inject(this)")
        .build()
}
