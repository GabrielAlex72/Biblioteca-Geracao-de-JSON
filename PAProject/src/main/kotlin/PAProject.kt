@Target(AnnotationTarget.PROPERTY,AnnotationTarget.VALUE_PARAMETER)
annotation class Deprecated

data class Worker (val name:String,
                   val age:Int,
                   val ative:Boolean, val field:Enum<Field>){
    @Deprecated
    val Social_Class=SocialClass.Middle
}
enum class SocialClass{
    Upper,Middle,Lower

}

fun newDataMap(): Any {
    val map:MutableMap<String,Field> = mutableMapOf()
    map.put("John",Field.Finance)
    map.put("Ana",Field.Marketing)
    map.put("Michel",Field.Finance)

    return map
}
fun newDataSet(): Any {
    val John:Worker=Worker("John",27,true,Field.Finance)
    val Ana:Worker=Worker("Ana",25,true,Field.Marketing)
    val Michel:Worker=Worker("Michel",29,false,Field.Finance)

    val workersList:MutableList<Worker> = mutableListOf()
    workersList.add(John)
    workersList.add(Ana)
    workersList.add(Michel)
    return workersList
}

class WorkerData(val age:Int, val ative:Boolean, val field:Enum<Field>){}


class Worker_(val name:String,val workerData:WorkerData){

}
class Companie(name:String){
    val name=name
    val workersList:MutableList<Worker_> = mutableListOf()
    fun add(W:Worker_){
        workersList.add(W)
    }

}
fun workersSet():Any{
    val John:WorkerData=WorkerData(27,true,Field.Finance)
    val workerJohn:Worker_=Worker_("John",John)
    val Ana:WorkerData=WorkerData(25,true,Field.Marketing)
    val workerAna:Worker_=Worker_("Ana",Ana)
    val Michel:WorkerData=WorkerData(29,false,Field.Finance)
    val workerMichel:Worker_=Worker_("Michel",Michel)
    val Google:Companie=Companie("Google")
    Google.add(workerJohn)
    Google.add(workerAna)
    Google.add(workerMichel)

    return Google
}

enum class Field{
    Finance,HumanResources,Manufacturing,Marketing

}
 fun start():JsonObj{
     val Carro:JsonObj = JsonObj()

     val marca:JsonString = JsonString("BMW")

     Carro.setProperty("marca",marca)

     val Roda1:JsonObj = JsonObj()
     val Roda2:JsonObj = JsonObj()
     val Roda3:JsonObj = JsonObj()
     val Roda4:JsonObj = JsonObj()

     val Rodas:JsonArray = JsonArray()

     Carro.setProperty("Rodas",Rodas)

     Rodas.add(Roda1)
     Rodas.add(Roda2)
     Rodas.add(Roda3)
     Rodas.add(Roda4)

     val marcaR1:JsonString = JsonString("BMW")
     val marcaR2:JsonString = JsonString("Ford")
     val marcaR3:JsonString = JsonString("Nissan")
     val marcaR4:JsonString = JsonString("Toyota")

     Roda1.setProperty("marca",marcaR1)
     Roda2.setProperty("marca",marcaR2)
     Roda3.setProperty("marca",marcaR3)
     Roda4.setProperty("marca",marcaR4)

     val raioR1:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.5)
     val larguraR1:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.2)

     val raioR2:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.4)
     val larguraR2:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.3)

     val raioR3:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.4)
     val larguraR3:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.2)

     val raioR4:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.5)
     val larguraR4:JsonPrimitiveTypeDouble = JsonPrimitiveTypeDouble(0.3)

     Roda1.setProperty("raio",raioR1)
     Roda1.setProperty("largura",larguraR1)

     Roda2.setProperty("raio",raioR2)
     Roda2.setProperty("largura",larguraR2)

     Roda3.setProperty("raio",raioR3)
     Roda3.setProperty("largura",larguraR3)

     Roda4.setProperty("raio",raioR4)
     Roda4.setProperty("largura",larguraR4)

     return Carro
 }

fun main(){

    val Carro:JsonObj=start()
    val DataList=newDataSet()
    val DataMap=newDataMap()
    val JsonObjList:JsonEntity=inferênciaPorReflexão(DataList)
    /*if(JsonObjList is JsonArray)
    println(JsonObjList.print())*/
    val JsonObjMap:JsonEntity=inferênciaPorReflexão(DataMap)

    println(serialize(Carro))
    println(Search(Carro))
    /*println("serializeAux:")
    println(serialize(Carro))
    println("search:")
    println(Search(Carro))
    println("search2:")
    println(Search2(Carro))*/

    println("DataList")
    println(serialize(JsonObjList))
    println("DataMap")
    println(serialize(JsonObjMap))

    /*println(serialize2(object1))
    println(serialize2(object2))
    println(serialize2(object3))
    println(serialize2(object4))
    println(serialize2(object5))
    println(serialize2(Array1))
    println(serialize2(Array2))
    println(serialize2(Array3))
    println(serialize2(Array4))
    println(serialize2(numb11))
    println(serialize2(numb12))
    println(serialize2(numb21))
    println(serialize2(numb22))
    println(serialize2(numb31))
    println(serialize2(numb32))
    println(serialize2(numb41))
    println(serialize2(numb42))
    println(serialize2(String1))
    println(serialize2(String2))
    println(serialize2(String3))
    println(serialize2(String4))
    println(serialize2(String5))*/

}


