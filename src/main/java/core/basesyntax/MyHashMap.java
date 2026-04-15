package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int initialCapacity = 16;
    private static final int sizeMultiplier = 2;
    private static final double loadFactor = 0.75;
    private int currentMaxSize = initialCapacity;
    private int threshold = (int) (initialCapacity * loadFactor);
    private Node<K, V>[] buckets;
    private int size = 0;

    public MyHashMap() {
        buckets = new Node[initialCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = getIndexByKey(key);

        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value);
            size++;
            return;
        }

        Node<K, V> current = buckets[index];

        if (current.key == null
                ? key == null
                : current.key.equals(key)) {
            current.value = value;
            return;
        }

        while (current.next != null) {
            if (current.next.key == null
                    ? key == null
                    : current.next.key.equals(key)) {
                current.next.value = value;
                return;
            }
            current = current.next;
        }

        current.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }

        int index = getIndexByKey(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key == null
                    ? key == null
                    : current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNewNode(Node<K, V> newNode) {
        newNode.next = null;

        int index = getIndexByKey(newNode.key);

        if (buckets[index] == null) {
            buckets[index] = newNode;
            return;
        }

        Node<K, V> current = buckets[index];

        if (current.key == null
                ? newNode.key == null
                : current.key.equals(newNode.key)) {
            current.value = newNode.value;
            return;
        }

        while (current.next != null) {
            if (current.next.key == null
                    ? newNode.key == null
                    : current.next.key.equals(newNode.key)) {
                current.next.value = newNode.value;
                return;
            }
            current = current.next;
        }

        current.next = newNode;
    }

    private void resize() {
        currentMaxSize = currentMaxSize * sizeMultiplier;
        threshold = (int) (currentMaxSize * loadFactor);

        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[currentMaxSize];

        for (Node<K, V> node : oldBuckets) {
            while (node != null) {
                Node<K, V> next = node.next;
                node.next = null;
                addNewNode(node);
                node = next;
            }
        }
    }

    private int getIndexByKey(K key) {
        if (key == null) {
            return 0;
        }

        int hash = key.hashCode();
        int HASH_MASK = 0x7fffffff;
        return (hash & HASH_MASK) % currentMaxSize;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
