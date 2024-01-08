package com.frame.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.util.*

class FlutterPage : AnAction("FlutterPage") {

    var project: Project? = null
    var selectGroup: VirtualFile? = null

    override fun actionPerformed(e: AnActionEvent) {
        project = e.project
        val className = PluginUtil.getClassName(project)

        selectGroup = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext)
        if (className == null || className == "") {
            print("请输入类名")
            return
        }
        createMvpBase(className)
        project!!.baseDir.refresh(false, true)
    }

    /**
     * Flutter Mvi Page
     */
    private fun createMvpBase(className: String) {
        val path = selectGroup!!.path + "/${className.toLowerCase()}"
        val packageName = path.substring(path.indexOf("lib") + 4, path.length).replace("/", ".")
        val main = path.substring(0, path.indexOf("lib"))

        var viewModelName = "${className}PageState";
        val output2 = viewModelName.replace("(.)([A-Z])".toRegex(), "$1_$2").lowercase(Locale.getDefault())

        //======FlutterPage
        var page = PluginUtil.readFile("BaseFlutterPage.txt")!!
        page = page.replace("&package&", packageName)
            .replace("&Home&", "${className}")
            .replace("&HomeViewModel&", "${className}PageViewModel")
            .replace("&home_page_view_model&", output2)

        var fileName = "${className}Page";
        val output = fileName.replace("(.)([A-Z])".toRegex(), "$1_$2").lowercase(Locale.getDefault())
        PluginUtil.writetoFile(page, path, "${output}.dart")

        //======FlutterPageViewModel
        var viewmodel = PluginUtil.readFile("BaseFlutterPageViewModel.txt")!!
        viewmodel = viewmodel
            .replace("&Home&", "$className")

        /*var viewModelName = "${className}PageViewModel";
        val output2 = viewModelName.replace("(.)([A-Z])".toRegex(), "$1_$2").lowercase(Locale.getDefault())*/
        PluginUtil.writetoFile(viewmodel, path, "${output2}.dart")


    }


}