import javax.swing.text.html.parser.Entity

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

fun serializeAux(obj:JsonEntity):String{
    var result=""

    when(obj){
        is JsonObj -> {result+="{ ";var i=0; for(o in obj.children){if(i>0)result+=",";result+=" "+obj.childrenNames[i]+" :"+ serializeAux(o); i+=1};result+=" }"}
        is JsonArray -> {result+="[";var i=0; for(o in obj.value){if(i>0)result+=",";result+=serializeAux(o);i+=1};result+="]"}
        is JsonNumber -> result+=" " + obj.value+" "
        is JsonString -> result+='"'+ obj.value+'"'
    }
    return result
}
fun Search(){

}
