import javax.swing.text.html.parser.Entity
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

@Target(AnnotationTarget.FUNCTION)
annotation class TestCase

abstract class JsonEntity(){

    abstract val value: Any

    /*init {
            if(JO!=null) {
                JO.children.add(this)
                JO.childrenNames.add(name)
            }
        }*/
    abstract fun accept(v: Visitor)
}


class JsonObj(override val value: Any): JsonEntity() {


    val childrenNames : MutableList<String> = mutableListOf()
    val children : MutableList<JsonEntity> = mutableListOf()

    fun setPriority(name:String,value:JsonEntity){
        childrenNames.add(name)
        children.add(value)
    }

    override fun accept(v: Visitor) {
        if(v.visit(this))
            children.forEach {
                it.accept(v)
            }
        v.endVisit(this)
    }
}

class JsonArray():JsonEntity(){

    override val value : MutableList<JsonEntity> = mutableListOf()
    val printValue : MutableList<Any> = mutableListOf<Any>()


    fun add(element:JsonEntity){
        value.add(element)
        printValue.add(element.value)
    }

    override fun accept(v: Visitor) {
        if(v.visit(this))
            value.forEach {
                it.accept(v)
            }
        v.endVisit(this)
    }
}
class JsonNumber(value:Double):JsonEntity(){

    override val value = value

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}
class JsonString(value:String):JsonEntity(){

    override val value=value

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

interface Visitor {
    fun visit(JO: JsonObj): Boolean = true
    fun endVisit(JO: JsonObj) { }
    fun visit(JA: JsonArray): Boolean = true
    fun endVisit(JA: JsonArray) { }
    fun visit(JN: JsonNumber) { }
    fun visit(JS: JsonString) { }
}
@TestCase
fun serialize(obj:JsonEntity):String{
    val result =serializeAux(obj,1)
    return result
}
fun serializeAux(obj:JsonEntity,n:Int):String{
    var result=""

    when(obj){
        is JsonObj -> {result+="{ \n";var i=0; for(o in obj.children){if(i>0)result+=",\n";var j=0;while(j<n){result+="    ";j+=1};result+=" "+obj.childrenNames[i]+" :"+ serializeAux(o,n+3); i+=1};result+=" \n";var j=0;while(j<n-1){result+="    ";j+=1};result+="}"}
        is JsonArray -> {result+="[ \n";var i=0; for(o in obj.value){if(i>0)result+=",\n";var j=0;while(j<n){result+="    ";j+=1};result+=serializeAux(o,n+1);i+=1};result+=" \n";var j=0;while(j<n-1){result+="    ";j+=1};result+="]"};
        is JsonNumber -> result+=" " + obj.value+" "
        is JsonString -> result+='"'+ obj.value+'"'
    }
    return result

}
@TestCase
fun Search(c:JsonEntity):MutableList<String>{
    val getAllStrings = object : Visitor {
        val list: MutableList<String> = mutableListOf()
        override fun visit(l: JsonString) {
            list.add(l.value)
        }
    }
    c.accept(getAllStrings)
    return getAllStrings.list

}

@TestCase //funcao que retorna todas as rodas com 0.5 de raio
fun Search2(c:JsonEntity):MutableList<String>{
    val result = object : Visitor {
        val list: MutableList<String> = mutableListOf()
        override fun endVisit(l: JsonObj) {
            if(l.childrenNames.contains("raio")){
                val n=l.childrenNames.indexOf("raio")
                if(l.children.get(n).value==0.5)
                    list.add(serialize(l))
            }

        }
    }
    c.accept(result)
    return result.list
}
@TestCase
fun inferênciaPorReflexão():JsonObj{
    val clazzObj: KClass<JsonObj> = JsonObj::class
    val clazzArray:KClass<JsonArray> = JsonArray::class
    val clazzNumber: KClass<JsonNumber> = JsonNumber::class
    val clazzString: KClass<JsonString> = JsonString::class

    val Carro = clazzObj.primaryConstructor!!.call("Carro")

    val marca = clazzString.primaryConstructor!!.call("BMW")

    val setPriority: KFunction<*>? = clazzObj.declaredMemberFunctions.find { it.name.contains("setPriority") }

    setPriority?.call(Carro,"marca",marca)

    val Roda1 = clazzObj.primaryConstructor!!.call("Roda1")
    val Roda2 = clazzObj.primaryConstructor!!.call("Roda2")
    val Roda3 = clazzObj.primaryConstructor!!.call("Roda3")
    val Roda4 = clazzObj.primaryConstructor!!.call("Roda4")

    val Rodas = clazzArray.primaryConstructor!!.call()

    setPriority?.call(Carro,"Rodas",Rodas)

    val add: KFunction<*>? = clazzArray.declaredMemberFunctions.find{it.name.contains("add")}

    add?.call(Rodas,Roda1)
    add?.call(Rodas,Roda2)
    add?.call(Rodas,Roda3)
    add?.call(Rodas,Roda4)

    val marcaR1 = clazzString.primaryConstructor!!.call("BMW")
    val marcaR2 = clazzString.primaryConstructor!!.call("Ford")
    val marcaR3 = clazzString.primaryConstructor!!.call("Nissan")
    val marcaR4 = clazzString.primaryConstructor!!.call("Toyota")

    setPriority?.call(Roda1,"marca",marcaR1)
    setPriority?.call(Roda2,"marca",marcaR2)
    setPriority?.call(Roda3,"marca",marcaR3)
    setPriority?.call(Roda4,"marca",marcaR4)

    val raioR1 = clazzNumber.primaryConstructor!!.call(0.5)
    val larguraR1 = clazzNumber.primaryConstructor!!.call(0.2)

    val raioR2 = clazzNumber.primaryConstructor!!.call(0.4)
    val larguraR2 = clazzNumber.primaryConstructor!!.call(0.3)

    val raioR3 = clazzNumber.primaryConstructor!!.call(0.4)
    val larguraR3 = clazzNumber.primaryConstructor!!.call(0.2)

    val raioR4 = clazzNumber.primaryConstructor!!.call(0.5)
    val larguraR4 = clazzNumber.primaryConstructor!!.call(0.3)

    setPriority?.call(Roda1,"raio",raioR1)
    setPriority?.call(Roda1,"largura",larguraR1)

    setPriority?.call(Roda2,"raio",raioR2)
    setPriority?.call(Roda2,"largura",larguraR2)

    setPriority?.call(Roda3,"raio",raioR3)
    setPriority?.call(Roda3,"largura",larguraR3)

    setPriority?.call(Roda4,"raio",raioR4)
    setPriority?.call(Roda4,"largura",larguraR4)

    return Carro
}