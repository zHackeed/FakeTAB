package me.zhacked.faketab.cache;

import java.util.Collection;

public interface Cache<K, V>{

    void add(K key, V value);

    void remove(K key);

    V get(K key);

    Collection<K> keySet();

    boolean exists(K key);

    Collection<V> values();

    void removeAll();
}