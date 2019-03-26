package io.zcx.plugin.bazel.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public abstract class AbstractBazelGradleTask extends DefaultTask {

    public AbstractBazelGradleTask() {

        setGroup("gradle-to-bazel");
        setDescription(getClass().getCanonicalName());
    }

    @TaskAction
    public final void doAction() {
        doTask();
    }

    abstract void doTask();
}
