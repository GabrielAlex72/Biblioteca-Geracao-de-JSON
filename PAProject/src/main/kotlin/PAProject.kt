

fun main(){
    val Carro:JsonObj = JsonObj("")

    val marca:JsonString = JsonString("BMW")

    Carro.setPriority("marca",marca)

    val Roda1:JsonObj = JsonObj("Roda1")
    val Roda2:JsonObj = JsonObj("Roda2")
    val Roda3:JsonObj = JsonObj("Roda3")
    val Roda4:JsonObj = JsonObj("Roda4")

    val Rodas:JsonArray = JsonArray()

    Carro.setPriority("Rodas",Rodas)

    Rodas.add(Roda1)
    Rodas.add(Roda2)
    Rodas.add(Roda3)
    Rodas.add(Roda4)

    val marcaR1:JsonString = JsonString("BMW")
    val marcaR2:JsonString = JsonString("Ford")
    val marcaR3:JsonString = JsonString("Nissan")
    val marcaR4:JsonString = JsonString("Toyota")

    Roda1.setPriority("marca",marcaR1)
    Roda2.setPriority("marca",marcaR2)
    Roda3.setPriority("marca",marcaR3)
    Roda4.setPriority("marca",marcaR4)

    val raioR1:JsonNumber = JsonNumber(0.5)
    val larguraR1:JsonNumber = JsonNumber(0.2)

    val raioR2:JsonNumber = JsonNumber(0.5)
    val larguraR2:JsonNumber = JsonNumber(0.2)

    val raioR3:JsonNumber = JsonNumber(0.5)
    val larguraR3:JsonNumber = JsonNumber(0.2)

    val raioR4:JsonNumber = JsonNumber(0.5)
    val larguraR4:JsonNumber = JsonNumber(0.2)

    Roda1.setPriority("raio",raioR1)
    Roda1.setPriority("largura",larguraR1)

    Roda2.setPriority("raio",raioR2)
    Roda2.setPriority("largura",larguraR2)

    Roda3.setPriority("raio",raioR3)
    Roda3.setPriority("largura",larguraR3)

    Roda4.setPriority("raio",raioR4)
    Roda4.setPriority("largura",larguraR4)

    println(serializeAux(Carro))

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