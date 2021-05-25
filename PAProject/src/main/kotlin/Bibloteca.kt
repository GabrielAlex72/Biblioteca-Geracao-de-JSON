import org.junit.Test
import javax.swing.text.html.parser.Element
import javax.swing.text.html.parser.Entity
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

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


class JsonObj(): JsonEntity() {

    //val childrenNames : MutableList<String> = mutableListOf() //
    //val children : MutableList<JsonEntity> = mutableListOf() //
    override val value: MutableList<(Pair<String, JsonEntity>)> = mutableListOf()

    fun print(){
        for(o in value)
        println(o.second.value)
    }
    fun setProperty(name:String,element:JsonEntity){
        val pair = Pair(name,element)
        //childrenNames.add(name)
        //children.add(element)
        value.add(pair)
    }

    override fun accept(v: Visitor) {
        if(v.visit(this))
            value.forEach {
                it.second.accept(v)
            }
        v.endVisit(this)
    }
}

class JsonArray():JsonEntity(){

    override val value : MutableList<JsonEntity> = mutableListOf()
    //val printValue : MutableList<Any> = mutableListOf<Any>()


    fun add(element:JsonEntity){
        value.add(element)
        //printValue.add(element.value)
    }
    fun print(){
        for(o in value)
            if(o is JsonObj)
                println(o.print())
    }

    override fun accept(v: Visitor) {
        if(v.visit(this))
            value.forEach {
                it.accept(v)
            }
        v.endVisit(this)
    }
}
class JsonPrimitiveTypeBoolean(valueP:Boolean):JsonEntity(){


    override val value:Boolean = valueP


    override fun accept(v: Visitor) {
        v.visit(this)
    }
}
class JsonPrimitiveTypeInt(valueP:Int):JsonEntity(){


    override val value:Int = valueP


    override fun accept(v: Visitor) {
        v.visit(this)
    }
}
class JsonPrimitiveTypeChar(valueP:Char):JsonEntity(){


    override val value:Char = valueP


    override fun accept(v: Visitor) {
        v.visit(this)
    }
}
class JsonPrimitiveTypeDouble(valueP:Double):JsonEntity(){


    override val value:Double = valueP


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
    fun visit(JN: JsonPrimitiveTypeBoolean) { }
    fun visit(JN: JsonPrimitiveTypeChar) { }
    fun visit(JN: JsonPrimitiveTypeInt) { }
    fun visit(JN: JsonPrimitiveTypeDouble) { }
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
        is JsonObj -> {result+="{ \n";var i=0; for(o in obj.value){if(i>0)result+=",\n";var j=0;while(j<n){result+="    ";j+=1};result+=" "+o.first+" :"+ serializeAux(o.second,n+3); i+=1};result+=" \n";var j=0;while(j<n-1){result+="    ";j+=1};result+="}"}
        is JsonArray -> {result+="[ \n";var i=0; for(o in obj.value){if(i>0)result+=",\n";var j=0;while(j<n){result+="    ";j+=1};result+=serializeAux(o,n+1);i+=1};result+=" \n";var j=0;while(j<n-1){result+="    ";j+=1};result+="]"};
        is JsonPrimitiveTypeBoolean -> result+=" " + obj.value+" "
        is JsonPrimitiveTypeInt -> result+=" " + obj.value+" "
        is JsonPrimitiveTypeChar -> result+=" " + obj.value+" "
        is JsonPrimitiveTypeDouble -> result+=" " + obj.value+" "
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
            if(l.value.map{it.first}.contains("raio")){
                val n=l.value.map{it.first}.indexOf("raio")
                if(l.value.map{it.second}.get(n).value==0.5)
                    list.add(serialize(l))
            }

        }
    }
    c.accept(result)
    return result.list
}


/*fun inferênciaPorReflexão():JsonObj{

    val clazzObj: KClass<JsonObj> = JsonObj::class
    val clazzArray:KClass<JsonArray> = JsonArray::class
    val clazzNumber: KClass<JsonPrimitiveType> = JsonPrimitiveType::class
    val clazzString: KClass<JsonString> = JsonString::class

    val Carro = clazzObj.primaryConstructor!!.call()

    val marca = clazzString.primaryConstructor!!.call("BMW")

    val setPriority: KFunction<*>? = clazzObj.declaredMemberFunctions.find { it.name.contains("setProperty") }

    setPriority?.call(Carro,"marca",marca)

    val Roda1 = clazzObj.primaryConstructor!!.call()
    val Roda2 = clazzObj.primaryConstructor!!.call()
    val Roda3 = clazzObj.primaryConstructor!!.call()
    val Roda4 = clazzObj.primaryConstructor!!.call()

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
}*/
@TestCase
fun inferênciaPorReflexão(obj:Any):JsonEntity{
    val Result :JsonEntity = when(obj){

        is Collection<*> -> ReflexCollection(obj);
        is Map<*,*> ->ReflexMap(obj);
        is Int -> ReflexPrimitiveTypeInt(obj);
        is Double -> ReflexPrimitiveTypeDouble(obj);
        is Boolean -> ReflexPrimitiveTypeBoolean(obj);
        is Char -> ReflexPrimitiveTypeChar(obj);
        is String -> ReflexString(obj);
        is Enum<*> -> ReflexEnum(obj);
        else -> ReflexDataClass(obj);
        }
    return Result
    }

fun ReflexCollection(obj:Collection<*>):JsonArray{
    //val clazzArray:KClass<JsonArray> = JsonArray::class
    //val Result:JsonArray = clazzArray.primaryConstructor!!.call()
    val Result:JsonArray = JsonArray()
    //val add: KFunction<*>? = clazzArray.declaredMemberFunctions.find{it.name.contains("add")}

    for(o in obj)
        if(o!=null)
            //add?.call(Result,inferênciaPorReflexão(o))
            Result.add(inferênciaPorReflexão(o))
    return Result
}

fun ReflexMap(obj:Map<*,*>):JsonObj{
    //val clazzObj: KClass<JsonObj> = JsonObj::class
    //val Result:JsonObj = clazzObj.primaryConstructor!!.call()
    val Result:JsonObj = JsonObj()
    //val setPriority: KFunction<*>? = clazzObj.declaredMemberFunctions.find { it.name.contains("setProperty") }

    for((a,b) in obj.toList())
        if(a!=null && b!=null)
            if( a is String )
                //setPriority?.call(Result,a,inferênciaPorReflexão(b))
                Result.setProperty(a,inferênciaPorReflexão(b))
            else
                //setPriority?.call(Result,a.toString(),inferênciaPorReflexão(b))
                Result.setProperty(a.toString(),inferênciaPorReflexão(b))
    return Result
}

fun ReflexPrimitiveTypeInt(obj:Any):JsonPrimitiveTypeInt{
    //val clazzPrimitiveType: KClass<JsonPrimitiveTypeInt> = JsonPrimitiveTypeInt::class
    //val Result:JsonPrimitiveTypeInt = clazzPrimitiveType.primaryConstructor!!.call(obj)
    val Result:JsonPrimitiveTypeInt = JsonPrimitiveTypeInt(obj as Int)
    return Result
}
fun ReflexPrimitiveTypeDouble(obj:Any):JsonPrimitiveTypeDouble{
    //val clazzPrimitiveType: KClass<JsonPrimitiveTypeDouble> = JsonPrimitiveTypeDouble::class
    //val Result:JsonPrimitiveTypeDouble = clazzPrimitiveType.primaryConstructor!!.call(obj)
    val Result:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(obj as Double)
    return Result
}
fun ReflexPrimitiveTypeBoolean(obj:Any):JsonPrimitiveTypeBoolean{
    //val clazzPrimitiveType: KClass<JsonPrimitiveTypeBoolean> = JsonPrimitiveTypeBoolean::class
    //val Result:JsonPrimitiveTypeBoolean = clazzPrimitiveType.primaryConstructor!!.call(obj)
    val Result:JsonPrimitiveTypeBoolean = JsonPrimitiveTypeBoolean(obj as Boolean)
    return Result
}
fun ReflexPrimitiveTypeChar(obj:Any):JsonPrimitiveTypeChar{
    //val clazzPrimitiveType: KClass<JsonPrimitiveTypeChar> = JsonPrimitiveTypeChar::class
    //val Result:JsonPrimitiveTypeChar = clazzPrimitiveType.primaryConstructor!!.call(obj)
    val Result:JsonPrimitiveTypeChar = JsonPrimitiveTypeChar(obj as Char)
    return Result
}
fun ReflexString(obj:String):JsonString{
    //val clazzString: KClass<JsonString> = JsonString::class
    //val Result:JsonString = clazzString.primaryConstructor!!.call(obj)
    val Result:JsonString = JsonString(obj)
    return Result

}
fun ReflexEnum(obj:Enum<*>):JsonString{
    //val clazzString: KClass<JsonString> = JsonString::class
    //val Result:JsonString = clazzString.primaryConstructor!!.call(obj.toString())
    val Result:JsonString = JsonString(obj.toString())
    return Result
}
fun ReflexDataClass(obj:Any):JsonObj{
    //val clazzObjR: KClass<JsonObj> = JsonObj::class
    //val Result:JsonObj = clazzObjR.primaryConstructor!!.call()
    val Result:JsonObj = JsonObj()
    //val setPriority: KFunction<*>? = clazzObjR.declaredMemberFunctions.find { it.name.contains("setProperty") }

    val clazzObj: KClass<Any> = obj::class as KClass<Any>
    val propValues = clazzObj.declaredMemberProperties.map { var a=it.name;var b=it.call(obj);if(!it.hasAnnotation<Deprecated>()){if(b!=null) /*setPriority?.call(Result,a,inferênciaPorReflexão(b))*/Result.setProperty(a,inferênciaPorReflexão(b))} }

    return Result
}