package com.newrelic.jfr.subst;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className="jdk.jfr.internal.JVM")
final class Target_jdk_jfr_internal_JVM {
    @Substitute
    public long getTypeId(Class<?> clazz) {
        return -1;
    }
}

