package socialnetwork.utils.observers;


import socialnetwork.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}