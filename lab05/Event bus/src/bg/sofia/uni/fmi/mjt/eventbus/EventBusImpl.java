package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {

    private Map<Class<?>, Set<Subscriber<?>>> subscriptionLog;
    private Set<Event<?>> eventLog;

    public EventBusImpl() {
        subscriptionLog = new HashMap<>();
        eventLog = new HashSet<>();
    }

    private <T extends Event<?>> void addSubscriber(Class<T> eventType, Subscriber<? super T> subscriber) {
        Set<Subscriber<?>> subscribers = subscriptionLog.get(eventType);

        if (subscribers == null) {
            subscribers = new HashSet<>();
        }

        subscribers.add(subscriber);
        subscriptionLog.put(eventType, subscribers);
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null) {
            throw new IllegalArgumentException("The eventType can not be null!");
        }
        if (subscriber == null) {
            throw new IllegalArgumentException("The subscriber can not be null!");
        }

        addSubscriber(eventType, subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {
        if (eventType == null) {
            throw new IllegalArgumentException("The eventType can not be null!");
        }
        if (subscriber == null) {
            throw new IllegalArgumentException("The subscriber can not be null!");
        }

        if (subscriptionLog.containsKey(eventType)) {

            if (!subscriptionLog.get(eventType).contains(subscriber)) {
                throw new MissingSubscriptionException("The subscriber hasn't got a subscription!");
            }

            Set<Subscriber<?>> subscribers = subscriptionLog.get(eventType);
            subscribers.remove(subscriber);
            if (subscriptionLog.get(eventType).isEmpty()) {
                subscriptionLog.remove(eventType);
            }
        } else {
            throw new MissingSubscriptionException("The subscriber hasn't got a subscription!");
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The event can not be null!");
        }

        if (subscriptionLog.containsKey(event.getClass())) {
            for (Subscriber<?> sub : subscriptionLog.get(event.getClass())) {
                ((Subscriber<T>) sub).onEvent(event);
            }
        }

        eventLog.add(event);
    }

    @Override
    public void clear() {
        eventLog.clear();
        subscriptionLog.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type can not be null!");
        }

        if (from == null) {
            throw new IllegalArgumentException("The from timestamp can not be null!");
        }

        if (to == null) {
            throw new IllegalArgumentException("The to timestamp can not be null!");
        }

        List<Event<?>> eventList = new ArrayList<>();
        for (Event<?> event : eventLog) {
            if (event.getClass().equals(eventType) &&
                    !event.getTimestamp().isBefore(from) &&
                    event.getTimestamp().isBefore(to)) {
                eventList.add(event);
            }
        }
        eventList.sort(new Comparator<Event<?>>() {
            @Override
            public int compare(Event<?> o1, Event<?> o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
        return Collections.unmodifiableCollection(eventList);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type can not be null!");
        }

        if (!subscriptionLog.containsKey(eventType)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(subscriptionLog.get(eventType));
    }
}
