package org.flippers.dissemination;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityBlockingQueueSet<T extends Comparable<? super T>> {

    private int maxQueueSize = Integer.MAX_VALUE - 8;
    private AtomicInteger allocationLock;
    private int defaultInitialCapacity = 11;
    private transient Object[] queue;
    private transient int size;
    private Set<T> elementSet;
    private ReentrantLock lock;

    public PriorityBlockingQueueSet() {
        this(Integer.MAX_VALUE - 8);
    }

    public PriorityBlockingQueueSet(Integer maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.queue = new Object[defaultInitialCapacity];
        this.elementSet = new HashSet<>();
        this.lock = new ReentrantLock();
        this.allocationLock = new AtomicInteger(0);
    }

    private void tryGrowing(Object[] array, int oldCap) {
        lock.unlock();

        Object[] newArray = null;
        try {
            if (allocationLock.get() == 0 && allocationLock.compareAndSet(0, 1)) {
                int newSizeCap = oldCap + (oldCap < 64 ? oldCap : oldCap << 2);
                if (newSizeCap - maxQueueSize > 0) {
                    int nextMinCap = oldCap + 1;
                    if (nextMinCap < 0 || nextMinCap > maxQueueSize)
                        throw new OutOfMemoryError();
                    newSizeCap = maxQueueSize;
                }

                if (newSizeCap > oldCap && queue == array) {
                    newArray = new Object[newSizeCap];
                }
            }
        } finally {
            allocationLock.set(0);
        }

        if (newArray == null)
            Thread.yield();

        lock.lock();
        if (newArray != null && queue == array) {
            queue = newArray;
            System.arraycopy(array, 0, newArray, 0, oldCap);
        }
    }

    public boolean enqueue(T element) {
        if (element == null)
            throw new NullPointerException();

        final ReentrantLock lock = this.lock;
        lock.lock();
        int n, cap;
        Object[] array;
        while ((n = size) >= (cap = (array = queue).length))
            tryGrowing(array, cap);

        try {
            if (elementSet.contains(element)) return true;
            siftUp(n, element, array);
            this.elementSet.add(element);
            size = n + 1;
        } finally {
            lock.unlock();
        }

        return true;
    }

    public T dequeue() {
        return take();
    }

    private T take() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = size - 1;
            if (n < 0) {
                return null;
            } else {
                Object[] array = queue;
                T result = (T) array[0];
                T elementToMove = (T) array[n];
                array[n] = null;
                siftDown(0, elementToMove, array, n);
                this.elementSet.remove(result);
                size = n;
                return result;
            }
        } finally {
            lock.unlock();
        }
    }

    private void siftDown(int positionToFill, T element, Object[] array, int arrSize) {
        if (arrSize > 0) {
            int lastNonLeafNode = arrSize >>> 1;
            int pos = positionToFill;
            while (pos < lastNonLeafNode) {
                int lftChildIdx = (pos << 1) + 1;
                int rgtChildIdx = lftChildIdx + 1;
                int childPosToFill = lftChildIdx;
                if (rgtChildIdx < arrSize && ((T) array[rgtChildIdx]).compareTo((T) array[lftChildIdx]) <= 0) {
                    childPosToFill = rgtChildIdx;
                }
                if (element.compareTo((T) array[childPosToFill]) < 0) break;
                array[pos] = array[childPosToFill];
                pos = childPosToFill;
            }
            array[pos] = element;
        }
    }

    private void siftUp(int positionToFill, T element, Object[] array) {
        while (positionToFill > 0) {
            int parent = (positionToFill - 1) >>> 1;
            T parentObj = (T) array[parent];
            if (element.compareTo(parentObj) >= 0) break;
            array[positionToFill] = parentObj;
            positionToFill = parent;
        }
        array[positionToFill] = element;
    }

}
