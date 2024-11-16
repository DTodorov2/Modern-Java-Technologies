package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.eventbus.comparator.ComparatorEventPriorityDescTimestampsAsc;
import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private Set<T> events;

    public DeferredEventSubscriber() {
        events = new HashSet<>();
    }
    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */

    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The event can not be null!");
        }

        events.add(event);
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted by
     * their priority in descending order. Events with equal priority are ordered in ascending order
     * of their timestamps.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        List<T> sortedList = new ArrayList<>(events);
        sortedList.sort(new ComparatorEventPriorityDescTimestampsAsc<>());
        return sortedList.iterator();
    }

    /**
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }

}