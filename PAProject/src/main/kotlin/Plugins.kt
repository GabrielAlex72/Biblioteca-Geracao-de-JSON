import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Device
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.*
import java.awt.GridLayout
import java.awt.Image
import java.awt.LayoutManager
import java.io.File
import javax.swing.text.html.ImageView
import javax.swing.tree.DefaultTreeCellRenderer



interface Appearance {
    val title: String
    fun execute(window: Window)
}

interface Action {
    val name: String
    fun execute(window: Window)
}

open class DefaultAppearance : Appearance {
    open override val title: String
        get() = "DefaultAppeareance"

    override fun execute(window: Window) {
        //window.shell.text+=" "+title

    }

}

open class CustomAppearance : Appearance {

    open override val title: String
        get() = "CustomAppeareance"

    override fun execute(window: Window) {
        window.shell.text+=" "+title
        ChangeIcon(window)
        ChangeNodesText(window)//muda o nome dos objetos para o nome contido no campo name, quando este existe dentro do objeto e elimina o campo name da arvore
        excludeRedundantNodes(window)//o objetivo era remover nós intermedios quando estes são o unico elemento dentro do pai e passar os seus filhos para o pai
    }
}
open class CustomAppearanceWhitoutProperties : Appearance {

    open override val title: String
        get() = "CustomAppeareance"
    override fun execute(window: Window) {
        ChangeNodesText(window)//muda o nome dos objetos para o nome contido no campo name, quando este existe dentro do objeto e elimina o campo name da arvore
        excludeRedundantNodes(window)//o objetivo era remover nós intermedios quando estes são o unico elemento dentro do pai e passar os seus filhos para o pai
        RemoveProperties(window)
        ChangeIcon(window)
    }
}

open class onlyChangeIcon : Appearance{
    open override val title: String
        get() = "OnlyChangeIcon"

    override fun execute(window: Window) {
        window.shell.text+=" "+title
        ChangeIcon(window)
    }
}
class Edit : Action {
    override val name: String
        get() = "Edit"

    override fun execute(window: Window) {
        EditWindow(window).open()
    }
}
class EditWindow(window:Window) {
    val shell: Shell
    init {

        shell = Shell(Display.getDefault())
        shell.setSize(100, 100)
        shell.location= Point(500,500)
        shell.text = "Edit"
        shell.layout = org.eclipse.swt.layout.GridLayout(2, false)
        shell.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN)
        val label = Label(shell,SWT.NONE)
        label.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN)
        label.text="name"
        val text=Text(shell,SWT.NONE)
        text.text=window.tree.selection.first().text
        val button=Button(shell,SWT.PUSH)
        val TLD = GridData()
        TLD.widthHint=70
        text.layoutData=TLD
        button.text="OK"
        val BLD = GridData()
        BLD.horizontalAlignment=SWT.CENTER
        BLD.horizontalSpan=2
        BLD.widthHint=70
        button.layoutData=BLD

        button.addSelectionListener(  object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                /*
                val Items=window.tree.selection.first()//.getItems()
                //val ItemName=Items.filter{it.data.toString().contains("name:")}
                //println(ItemName)
                if(Items.data.toString().contains("name")/*!ItemName.isEmpty()*/) {
                    println("in")
                    val Data: String = window.tree.selection.first().data.toString()
                    val lines = Data.split("\n")
                    val DataL = lines.filter { it.contains("name:") }

                    val data = DataL.first()
                    val lline = data.split(":")
                    val Nline: String = lline.first() + ":" + text.text
                    var I: Int = -1
                    for (line in lines) {
                        if (line.contains("name:"))
                            I = lines.indexOf(line)
                    }
                    val newLines: MutableList<String> = lines.toMutableList()
                    if (I > 0)
                        newLines[I] = Nline
                    var newData: String = ""
                    for (line in newLines)
                        newLines += line
                    window.tree.selection.first().data = newData
                }
*/
                window.tree.selection.first().text=text.text
                shell.close()
            }
        })



    }
    fun open(){
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        //display.dispose()
    }

}
class Write : Action {
    override val name: String
        get() = "Write"

    override fun execute(window: Window) {

       val Data:String = window.tree.selection.first().data.toString()
        val name:String=window.tree.selection.first().text
        val fileName= "TreeData_$name.txt"
        println("writed to File:"+fileName+"\nContent:\n"+Data)
        File(fileName).writeText(Data)

    }
}
class Open : Action{
    override val name: String
        get() = "Open"

    override fun execute(window: Window) {
        OpenWindow(window).open()

    }
}
class OpenWindow(window: Window){
    val shell: Shell
    init {
        shell = Shell(Display.getDefault())
        shell.setSize(100, 100)
        shell.location= Point(500,500)
        shell.text = "Open"
        shell.layout = org.eclipse.swt.layout.GridLayout(2, false)
        shell.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN)
        val dataLabel = Label(shell,SWT.SINGLE or SWT.BORDER)
        dataLabel.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)

        /*val size=GridData()
        //size.horizontalAlignment = GridData.FILL;
        //size.horizontalIndent=5
        size.verticalAlignment = GridData.FILL;

        size.minimumWidth=200
        size.verticalSpan=2

        size.minimumHeight=300

        size.grabExcessHorizontalSpace=true
        size.grabExcessVerticalSpace=true
        dataLabel.layoutData=size
*/
        dataLabel.text = window.tree.selection.first().data.toString()
    }

    fun open(){
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        //display.dispose()
    }

}
/*class ChangeIcon:Appearance{
    open override val title: String
        get() = "Test"
}
class changeNodeText:Appearance{
    open override val title: String
        get() = "Test"
}
class ExculdeNodeCreation:Appearance{
    open override val title: String
        get() = "Test"
}
*/



fun ChangeIcon(window:Window){
    val allItems:MutableList<TreeItem> = mutableListOf();
    getAllItems(window.tree,allItems)
    val topimage:org.eclipse.swt.graphics.Image=org.eclipse.swt.graphics.Image(Display.getDefault(),"Directory.png")
    window.tree.topItem.image=topimage
    for(ti in allItems){
        if(ti.getItems().isEmpty()) {
            //val display= Display()
            val image:org.eclipse.swt.graphics.Image=org.eclipse.swt.graphics.Image(Display.getDefault(),"Leaf.png")
            ti.image = image
        }else{
            //val display= Display()
            val image:org.eclipse.swt.graphics.Image=org.eclipse.swt.graphics.Image(Display.getDefault(),"Directory.png")
            ti.image = image
        }

    }


}
fun ChangeNodesText(window:Window){
    val allItems:MutableList<TreeItem> = mutableListOf();
    getAllItems(window.tree,allItems)

    val disposeItens:MutableList<TreeItem> = mutableListOf()

    val topItemName=window.tree.topItem.getItems().filter{it.text.contains("name:")}
    if(!topItemName.isEmpty()) {
        val name = topItemName.first().text
        val parts = name.split(":")
        val cparts=parts.last().split('"')
        window.tree.topItem.text =cparts[1]
        disposeItens.add(topItemName.first())
    }

    for(item in allItems){
        val ItemName=item.getItems().filter{it.text.contains("name:")}
        if(!ItemName.isEmpty()) {
            val name = ItemName.first().text
            val parts = name.split(":")

            val cparts=parts.last().split('"')
            item.text =cparts[1]//parts.last()
            disposeItens.add(ItemName.first())
        }
    }
    for(item in disposeItens)
        item.dispose()
}

fun excludeRedundantNodes(window:Window){
    val allItems:MutableList<TreeItem> = mutableListOf();
    getAllItems(window.tree,allItems)
    val disposeItens:MutableList<TreeItem> = mutableListOf()
    for(item in allItems){
        if(item.parent.getItems().size==1){
            val parent=item.parentItem
            val childrens=item.getItems()
            for(item in childrens){
                //item.parentItem=parent
            }
        }
    }
    for(item in disposeItens)
        item.dispose()
}
fun RemoveProperties(window: Window){
    val allItems:MutableList<TreeItem> = mutableListOf();
    getAllItems(window.tree,allItems)

    val disposeItens:MutableList<TreeItem> = mutableListOf()
    for(item in allItems){
        if(item.getItems().isEmpty())
            disposeItens.add(item)
    }
    for(item in disposeItens)
        item.dispose()

}
fun getAllItems(item:TreeItem,allItems:MutableList<TreeItem>) {

    val children = item.getItems();

    for(o:TreeItem in children) {
        allItems.add(o);
        getAllItems(o, allItems);
    }
}

fun getAllItems(tree: Tree, allItems:MutableList<TreeItem>) {

    for (item: TreeItem in tree.getItems()) {
        getAllItems(item,allItems)
    }
}