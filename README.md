# PAProject
#Esta Bibilioteca em kotlin tem como objetivo: 

#-> serializar objetos para Json
#-> visualizar a hierarquia de objetos Json permitindo a adicao de plugins

#para serializar os objetos primeiro é preciso transformar os objetos em objetos tipo Json, isso faz se dando o objeto Json á funcao inferenciaPorReflexão,
#esta funcao recebe um objeto normal e devolve um objeto tipo Json

#val jsonObject = inferenciaPorReflexao(NormalObject)

#para depois obter a string com o objeto serializado é só dar á funcao serialize o jsonObject obtido anteriormente

#val stringJson:String = serialize(jsonObject)

#depois podemos por exemplo exportalo para um ficheiro normalmente

#para visualizar a hierarquia de um objeto em jsonObject chamamos a funcao ShowHierarquieTree dando lhe um objeto e o nome que queremos dar á tree 

#ShowHierarquieTree(NormalObject,"ObjTree")

#esta arvore permite a criacao de dois tipos de plugins:
#plugins que mudam a apresentçao da arvore e plugins que acrescentao butoes que podem fazer acoes na arvore
#para criar estes dois tipos de plugins é preciso criar uma classe que implemente a interface Appearance ou a interface Action

#depois, alterar no ficheiro di.properties o campo Window.appeareance para a classe do tipo appearance criada 

#e/ou adicionar/alterar o campo Window.actions com o nome das classes criadas que utilizam a interface Actions, 
#cada classe adicionada a este campo vai criar um botao que quando clicado irá executar o codigo presente na funcao execute da classe correspondente
