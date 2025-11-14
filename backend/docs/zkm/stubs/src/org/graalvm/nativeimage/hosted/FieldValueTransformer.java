package org.graalvm.nativeimage.hosted;

/**
 * Minimal stub for Zelix classpath resolution only.
 */
@FunctionalInterface
public interface FieldValueTransformer {

    Object transform(Object receiver, Object originalValue);
}
