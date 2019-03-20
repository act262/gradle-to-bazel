package io.zcx.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanBazelWrapperTask extends DefaultTask {
    CleanBazelWrapperTask() {

        setGroup('BazelWrapper')
    }

    @TaskAction
    public void doTask() {

        println "do clean wrapper task"
    }
}