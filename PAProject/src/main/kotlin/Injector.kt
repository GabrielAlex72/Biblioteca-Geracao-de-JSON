import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.PROPERTY)
annotation class InjectAdd

class Injector {
    companion object {
        val map: MutableMap<String, List<KClass<*>>> =
            mutableMapOf()

        init {
            val scanner = Scanner(File("di.properties"))
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val parts = line.split("=")

                val classNames = parts[1].split(",")
                val mutableList: MutableList<KClass<*>> = mutableListOf()
                for (a in classNames) {
                    mutableList.add(Class.forName(a).kotlin)
                }
                map[parts[0]] = mutableList

            }
            scanner.close()
        }
        fun <T : Any> create(type: KClass<T>,content:JsonEntity,name:String): T {
            val o: T = type.primaryConstructor!!.call(content,name)
            type.declaredMemberProperties.forEach {
                if (it.hasAnnotation<Inject>()) {
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    val obj = map[key]!![0].createInstance()
                    (it as KMutableProperty<*>).setter.call(o, obj)
                }
                if (it.hasAnnotation<InjectAdd>()) {
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    val classList = it.getter.call(o) as MutableList<Any>
                    for(ob in map[key]!!) {
                        val obj = ob.createInstance()
                        classList.add(obj)
                    }
                }
            }

            return o
        }



    }

}