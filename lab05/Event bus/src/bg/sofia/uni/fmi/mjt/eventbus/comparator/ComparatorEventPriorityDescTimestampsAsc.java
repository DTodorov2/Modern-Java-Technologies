package bg.sofia.uni.fmi.mjt.eventbus.comparator;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

import java.util.Comparator;

public class ComparatorEventPriorityDescTimestampsAsc<T extends Event<?>> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        int priorityComparison = Integer.compare(o1.getPriority(), o2.getPriority());

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return o2.getTimestamp().compareTo(o1.getTimestamp());
    }
}
