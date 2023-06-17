package com.frame.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

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

        //======FlutterPage
        var page = PluginUtil.readFile("BaseFlutterPage.txt")!!
        page = page.replace("&package&", packageName)
            .replace("&Home&", "${className}")
            .replace("&HomeViewModel&", "${className}PageViewModel")
        PluginUtil.writetoFile(page, path, "${className}Page.dart")

        //======FlutterPageViewModel
        var viewmodel = PluginUtil.readFile("BaseFlutterPageViewModel.txt")!!
        viewmodel = viewmodel
            .replace("&Home&", "$className")

        PluginUtil.writetoFile(viewmodel, path, "${className}PageViewModel.dart")


    }


}