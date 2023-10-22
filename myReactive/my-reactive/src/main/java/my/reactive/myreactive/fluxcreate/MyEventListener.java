package my.reactive.myreactive.fluxcreate;

public interface MyEventListener {
    void onNewEvent(MyEventSource.MyEvent event);
    void onEventStopped();
}
