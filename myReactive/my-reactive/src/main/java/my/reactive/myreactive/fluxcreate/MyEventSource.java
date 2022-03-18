package my.reactive.myreactive.fluxcreate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MyEventSource {

    private List<MyEventListener> listeners;

    public MyEventSource() {
        this.listeners = new ArrayList<>();
    }

    public void register(MyEventListener listener) {    // 1
        listeners.add(listener);
    }

    public void newEvent(MyEvent event) {
        for (MyEventListener listener : listeners) {
            listener.onNewEvent(event);     // 2
        }
    }

    public void eventStopped() {
        for (MyEventListener listener : listeners) {
            listener.onEventStopped();      // 3
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyEvent {   // 4
        private Date timeStamp;
        private String message;
    }

}
