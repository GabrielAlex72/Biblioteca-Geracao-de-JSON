import org.eclipse.swt.SWT
import org.eclipse.swt.events.*
import org.eclipse.swt.layout.*
import org.eclipse.swt.widgets.*

fun main() {
    //val w=Injector.create(Window::class,start(),"Carro")
    //val w = Injector.create(Window::class,inferênciaPorReflexão(newDataSet()),"WorkersDataSet")
    ShowHierarquieTree(workersSet(),"WorkersDataSet")
    //val w=Injector.create(Window::class,inferênciaPorReflexão(workersSet()),"WorkersDataSet")
    //w.open()
}
fun ShowHierarquieTree(obj: Any, s: String) {
    val w=Injector.create(Window::class,inferênciaPorReflexão(obj),s)
    w.open()
}

data class Dummy(val number: Int)

class Window(obj:JsonEntity,name:String) {
    val shell: Shell
    val tree: Tree
    val obj=obj

    @Inject
    public lateinit var appeareance: Appearance

    @InjectAdd
    public val actions = mutableListOf<Action>()

    init {
        shell = Shell(Display.getDefault())
        shell.setSize(250, 200)

        shell.text = name
        shell.layout = GridLayout(2,false)
        shell.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN)


        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)
        tree.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)
        tree.foreground=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)

        when(obj) {
            is JsonObj->newTreeObj(tree, obj, "(object)")
            is JsonArray->newTreeObj(tree, obj, "(Array)")
        }

        /*val a = TreeItem(tree, SWT.NONE)
        a.text = "A"
        a.data = Dummy(1)

        val b = TreeItem(tree, SWT.NONE)
        b.text = "B"
        b.data = Dummy(2)

        val b1 = TreeItem(b, SWT.NONE)
        b1.text = "b1"
        b1.data = Dummy(3)

        val b2 = TreeItem(b, SWT.NONE)
        b2.text = "b2"
        b2.data = Dummy(4)

        val c = TreeItem(tree, SWT.NONE)
        c.text = "C"
        c.data = Dummy(5)

        val c1 = TreeItem(c, SWT.NONE)
        c1.text = "c1"
        c1.data = Dummy(6)

        val c1a = TreeItem(c1, SWT.NONE)
        c1a.text = "c1a"
        c1a.data = Dummy(7)
        */

        val dataLabel = Label(shell,SWT.SINGLE or SWT.BORDER)
        dataLabel.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)

        val size=GridData()
        //size.horizontalAlignment = GridData.FILL;
        //size.horizontalIndent=5
        size.verticalAlignment = GridData.FILL;

        size.minimumWidth=200
        size.verticalSpan=2

        size.minimumHeight=300

        size.grabExcessHorizontalSpace=true
        size.grabExcessVerticalSpace=true
        dataLabel.layoutData=size

        /*val gridData = GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.widthHint=200
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        dataLabel.layoutData=gridData
        */







        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                dataLabel.text = tree.selection.first().data.toString()

                //tree.selection.first().foreground=Display.getCurrent().getSystemColor(SWT.COLOR_GRAY)
                //dataLabel.update()
                //println("selected: " + tree.selection.first().data)
            }
        }

        )
        tree.layoutData=size

        val text = Text(shell, SWT.NONE)
        text.background=Display.getCurrent().getSystemColor(SWT.COLOR_GRAY)
        val tSize=GridData()
        tSize.verticalAlignment=SWT.BEGINNING
        text.layoutData=tSize

        fun getAllItems(item:TreeItem,allItems:MutableList<TreeItem>) {

            val children = item.getItems();

            for(o:TreeItem in children) {
                allItems.add(o);
                getAllItems(o, allItems);
            }
        }

        fun getAllItems(tree:Tree,allItems:MutableList<TreeItem>) {

            for (item: TreeItem in tree.getItems()) {
                getAllItems(item,allItems)
            }
        }



        text.addKeyListener(object: KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                val text = text.getText()
                val allItems:MutableList<TreeItem> = mutableListOf();
                getAllItems(tree,allItems)
                for(o in allItems) {
                    if (o.text.contains(text)) {
                        o.background = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE)
                        o.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE)
                    }
                    else {
                        o.background = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)
                        o.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)
                    }
                    if (text == "") {
                        o.background = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)
                        o.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)
                    }
                }


            }

        })

        /*val label = Label(shell, SWT.NONE)
        label.text = "skeleton"
        */
        //mudar pra caixa de texto

        /*val button = Button(shell, SWT.PUSH)
        button.text = "depth"
        button.addSelectionListener(object: SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                val item = tree.selection.first()
                label.text = item.depth().toString()
            }
        })*/
    }
    fun newTreeObj(Parent:Tree,obj:JsonEntity,name:String){
        val element = TreeItem(Parent, SWT.NONE)
        element.text = name
        element.data = serialize(obj)
        when(obj) {
            is JsonObj -> for(o in obj.value){newTreeItemObj(element,o.second,o.first)}
            is JsonArray -> for(o in obj.value){when(o){
                is JsonObj -> newTreeItemObj(element,o,"(object)")
                is JsonArray -> newTreeItemObj(element,o,"(Array)")
                is JsonPrimitiveTypeBoolean ->newTreeItemObj(element,o,"(Boolean)")
                is JsonPrimitiveTypeInt ->newTreeItemObj(element,o,"(Int)")
                is JsonPrimitiveTypeDouble ->newTreeItemObj(element,o,"(Double)")
                is JsonPrimitiveTypeChar ->newTreeItemObj(element,o,"(Char)")
                is JsonString -> newTreeItemObj(element,o,"(String)")
            }
            }
        }
    }
    fun newTreeItemObj(Parent:TreeItem,obj:JsonEntity,name:String){
        val element = TreeItem(Parent, SWT.NONE)
        element.text = name
        element.data = serialize(obj)
        when(obj) {
            is JsonObj -> for(o in obj.value){newTreeItemObj(element,o.second,o.first)}
            is JsonArray -> for(o in obj.value){when(o){
                is JsonObj -> newTreeItemObj(element,o,"(object)")
                is JsonArray -> newTreeItemObj(element,o,"(Array)")
                is JsonPrimitiveTypeBoolean ->newTreeItemObj(element,o,"(Boolean)")
                is JsonPrimitiveTypeInt ->newTreeItemObj(element,o,"(Int)")
                is JsonPrimitiveTypeDouble ->newTreeItemObj(element,o,"(Double)")
                is JsonPrimitiveTypeChar ->newTreeItemObj(element,o,"(Char)")
                is JsonString -> newTreeItemObj(element,o,"(String)")
            }
            }
            is JsonString ->element.text=name+": "+'"'+obj.value+'"'
            is JsonPrimitiveTypeBoolean ->element.text=name+": "+obj.value
            is JsonPrimitiveTypeInt ->element.text=name+": "+obj.value
            is JsonPrimitiveTypeDouble ->element.text=name+": "+obj.value
            is JsonPrimitiveTypeChar ->element.text=name+": "+obj.value
        }
    }


    fun open() {
        appeareance.execute(this)
        val group=Group(shell,SWT.NONE)
        val layout=FillLayout()//GridLayout(2,true)

        val size=GridData()
        size.horizontalSpan=2

        group.layoutData=size
        group.layout=layout
        group.background=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN)
        actions.forEach { action ->

            val button = Button(group, SWT.PUSH)
            button.text=action.name

            //button.background = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY)
            //button.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK)
            button.addSelectionListener(  object : SelectionAdapter() {
                    override fun widgetSelected(e: SelectionEvent) {
                        action.execute(this@Window)
                    }
            })

        }
        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }
}


// auxiliares para varrer a árvore

fun Tree.expandAll() = traverse { it.expanded = true }

fun Tree.traverse(visitor: (TreeItem) -> Unit) {
    fun TreeItem.traverse() {
        visitor(this)
        items.forEach {
            it.traverse()
        }
    }
    items.forEach { it.traverse() }
}


