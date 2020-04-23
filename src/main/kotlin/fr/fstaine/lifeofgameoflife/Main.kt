package fr.fstaine.lifeofgameoflife

import fr.fstaine.lifeofgameoflife.gui.RunningGameView
import tornadofx.App
import tornadofx.*

class Main: App(RunningGameView::class) {

}


class HelloWorldApp : App(HelloWorld::class)

class HelloWorld : View() {
    override val root = hbox {
        label("Hello world")
    }
}

//class HelloWorldApp : App(RootView::class) {
//    init {
//        importStylesheet("org/kordamp/bootstrapfx/bootstrapfx.css")
//        importStylesheet(RootStyles::class)
//        reloadStylesheetsOnFocus()
//        val icon = Image("icon.png")
//        setStageIcon(icon)
//    }
//}
