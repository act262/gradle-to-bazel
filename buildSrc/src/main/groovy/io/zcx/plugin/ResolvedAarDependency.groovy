package io.zcx.plugin

import org.gradle.api.artifacts.ResolvedDependency

class ResolvedAarDependency {

    ResolvedDependency resolvedDependency

    ResolvedAarDependency(ResolvedDependency resolvedDependency) {
        this.resolvedDependency = resolvedDependency
    }

    public static ResolvedAarDependency wrap(ResolvedDependency resolvedDependency) {
        new ResolvedAarDependency(resolvedDependency)
    }

    int hashCode() {
        return (resolvedDependency != null ? resolvedDependency.name.hashCode() : 0)
    }

    @Override
    boolean equals(Object o) {
        if (o != null && o instanceof ResolvedAarDependency) {
            return this.resolvedDependency.name == o.resolvedDependency.name
        }
        return super.equals(o)
    }

    @Override
    public String toString() {
        return "ResolvedAarDependency{" +
                "resolvedDependency=" + resolvedDependency +
                '}';
    }
}