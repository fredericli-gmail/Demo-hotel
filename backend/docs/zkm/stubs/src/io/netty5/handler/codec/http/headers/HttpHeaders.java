package io.netty5.handler.codec.http.headers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 以 Map 模擬 Netty5 HttpHeaders 的簡化版本。
 */
public class HttpHeaders implements Iterable<Entry<CharSequence, CharSequence>> {

    private final Map<String, List<CharSequence>> values = new LinkedHashMap<>();

    public CharSequence get(CharSequence name) {
        List<CharSequence> list = values.get(normalize(name));
        return (list != null && !list.isEmpty() ? list.get(0) : null);
    }

    public HttpHeaders add(CharSequence name, CharSequence value) {
        if (value != null) {
            values.computeIfAbsent(normalize(name), key -> new ArrayList<>()).add(value);
        }
        return this;
    }

    public HttpHeaders add(CharSequence name, Iterable<? extends CharSequence> newValues) {
        if (newValues != null) {
            newValues.forEach(val -> add(name, val));
        }
        return this;
    }

    public HttpHeaders set(CharSequence name, CharSequence value) {
        if (value == null) {
            remove(name);
        }
        else {
            List<CharSequence> list = new ArrayList<>();
            list.add(value);
            values.put(normalize(name), list);
        }
        return this;
    }

    public HttpHeaders set(CharSequence name, Iterable<? extends CharSequence> newValues) {
        List<CharSequence> list = new ArrayList<>();
        if (newValues != null) {
            newValues.forEach(list::add);
        }
        values.put(normalize(name), list);
        return this;
    }

    public boolean remove(CharSequence name) {
        return values.remove(normalize(name)) != null;
    }

    public HttpHeaders clear() {
        values.clear();
        return this;
    }

    public boolean contains(CharSequence name) {
        return values.containsKey(normalize(name));
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }

    public Set<CharSequence> names() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(values.keySet()));
    }

    public Iterator<CharSequence> valuesIterator(CharSequence name) {
        List<CharSequence> list = values.get(normalize(name));
        if (list == null) {
            return Collections.emptyIterator();
        }
        return list.iterator();
    }

    @Override
    public Iterator<Entry<CharSequence, CharSequence>> iterator() {
        List<Entry<CharSequence, CharSequence>> flattened = new ArrayList<>();
        values.forEach((key, list) -> {
            for (CharSequence value : list) {
                flattened.add(Map.entry(key, value));
            }
        });
        return flattened.iterator();
    }

    @Override
    public void forEach(Consumer<? super Entry<CharSequence, CharSequence>> action) {
        Iterable.super.forEach(action);
    }

    private String normalize(CharSequence name) {
        return name == null ? "" : name.toString().toLowerCase();
    }
}
