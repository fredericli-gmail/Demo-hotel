package org.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import jakarta.persistence.metamodel.Attribute;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;

/**
 * 提供 Hibernate 靜態工具類別的簡化版本，只保留 Spring ORM 會用到的方法，方便 ZKM 解析。
 */
public final class Hibernate {

    private Hibernate() {
        throw new UnsupportedOperationException();
    }

    public static void initialize(Object proxy) throws HibernateException {
        // no-op：僅供型別簽章存在
    }

    public static boolean isInitialized(Object proxy) {
        return true;
    }

    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static <T> boolean contains(Collection<? super T> collection, T element) {
        return collection != null && collection.contains(element);
    }

    public static <K, V> V get(Map<? super K, V> source, K key) {
        return source == null ? null : source.get(key);
    }

    public static <T> T get(List<T> list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getClass(T value) {
        return value == null ? null : (Class<? extends T>) value.getClass();
    }

    public static <T> Class<? extends T> getClassLazy(T value) {
        return getClass(value);
    }

    public static boolean isInstance(Object candidate, Class<?> type) {
        return type != null && type.isInstance(candidate);
    }

    public <E> boolean isPropertyInitialized(E entity, Attribute<? super E, ?> attribute) {
        return true;
    }

    public static boolean isPropertyInitialized(Object entity, String name) {
        return true;
    }

    public static Object unproxy(Object target) {
        return target;
    }

    public static <T> T unproxy(T target, Class<T> type) {
        return target;
    }

    public static <E> E createDetachedProxy(SessionFactory sessionFactory, Class<E> type, Object identifier) {
        return null;
    }

    public static <U> CollectionInterface<Collection<U>> bag() {
        return emptyInterface();
    }

    public static <U> CollectionInterface<Collection<U>> set() {
        return emptyInterface();
    }

    public static <U> CollectionInterface<List<U>> list() {
        return emptyInterface();
    }

    public static <U, V> CollectionInterface<Map<U, V>> map() {
        return emptyInterface();
    }

    public static <U> CollectionInterface<SortedSet<U>> sortedSet() {
        return emptyInterface();
    }

    public static <U, V> CollectionInterface<SortedMap<U, V>> sortedMap() {
        return emptyInterface();
    }

    public static <C> CollectionInterface<C> collection(Class<C> type) {
        return emptyInterface();
    }

    public static void close(Iterator<?> iterator) {
        if (iterator == null) {
            return;
        }
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof AutoCloseable closeable) {
                try {
                    closeable.close();
                }
                catch (Exception ignored) {
                }
            }
        }
    }

    public interface CollectionInterface<C> {

        default C create() { return null; }
    }

    @SuppressWarnings("unchecked")
    private static <T> CollectionInterface<T> emptyInterface() {
        return (CollectionInterface<T>) EmptyCollectionInterface.INSTANCE;
    }

    private static final class EmptyCollectionInterface<C> implements CollectionInterface<C> {

        private static final EmptyCollectionInterface<?> INSTANCE = new EmptyCollectionInterface<>();
    }
}
