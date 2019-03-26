package io.zcx.plugin.bazel.task

import io.zcx.plugin.bazel.task.AbstractBazelGradleTask

/**
 * Full clean all bazel files.
 */
class FullCleanBazelTask extends AbstractBazelGradleTask {

    @Override
    void doTask() {
        project.delete {
            delete 'bazel-bin', 'bazel-genfiles', 'bazel-out', 'bazel-testlogs'
            followSymlinks = true
        }
    }
}