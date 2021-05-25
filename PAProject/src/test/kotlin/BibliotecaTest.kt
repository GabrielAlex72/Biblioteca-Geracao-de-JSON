import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class BibliotecaTest{

    @Test
    fun testSerialize() {
        val Carro=start()
        val result = serialize(Carro)
        val expected = "{ \n" +
                "     marca :\"BMW\",\n" +
                "     Rodas :[ \n" +
                "                { \n" +
                "                     marca :\"BMW\",\n" +
                "                     raio : 0.5 ,\n" +
                "                     largura : 0.2  \n" +
                "                },\n" +
                "                { \n" +
                "                     marca :\"Ford\",\n" +
                "                     raio : 0.4 ,\n" +
                "                     largura : 0.3  \n" +
                "                },\n" +
                "                { \n" +
                "                     marca :\"Nissan\",\n" +
                "                     raio : 0.4 ,\n" +
                "                     largura : 0.2  \n" +
                "                },\n" +
                "                { \n" +
                "                     marca :\"Toyota\",\n" +
                "                     raio : 0.5 ,\n" +
                "                     largura : 0.3  \n" +
                "                } \n" +
                "            ] \n" +
                "}"
        assertEquals(expected,result)
    }

    @Test
    fun testSearch() {
        val Carro=start()
        val result = Search(Carro)
        var expected = listOf<String>("BMW", "BMW", "Ford", "Nissan", "Toyota")
        assertEquals(expected,result)
    }

    @Test
    fun testInferenciaPorReflexao() {
        val DataList=newDataSet()
        val DataMap=newDataMap()
        val ResultList=inferênciaPorReflexão(DataList)
        val ExpectedList = "[ \n" +
                "    { \n" +
                "         Age : 27 ,\n" +
                "         Ative : true ,\n" +
                "         field :\"Finance\",\n" +
                "         name :\"John\" \n" +
                "    },\n" +
                "    { \n" +
                "         Age : 25 ,\n" +
                "         Ative : true ,\n" +
                "         field :\"Marketing\",\n" +
                "         name :\"Ana\" \n" +
                "    },\n" +
                "    { \n" +
                "         Age : 29 ,\n" +
                "         Ative : false ,\n" +
                "         field :\"Finance\",\n" +
                "         name :\"Michel\" \n" +
                "    } \n" +
                "]"
        val ResultMap=inferênciaPorReflexão(DataMap)
        val ExpectedMap= "{ \n" +
                "     John :\"Finance\",\n" +
                "     Ana :\"Marketing\",\n" +
                "     Michel :\"Finance\" \n" +
                "}"
        //assertEquals(expected,ResultList)
        //assertEquals(expected,ResultList)

    }
}

class Calculator {
    fun parse(s: String):Int{
        val (a,op,b) = s.split(" ")
        return when(op) {
            "*" -> a.toInt() * b.toInt()
            else -> throw IllegalArgumentException("Invalid Operator")
        }
    }


}