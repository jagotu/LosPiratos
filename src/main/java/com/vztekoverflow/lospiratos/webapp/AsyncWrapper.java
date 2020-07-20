package com.vztekoverflow.lospiratos.webapp;

public class AsyncWrapper {
    final Object notifier = new Object();

    public Object getNotifier() {
        return notifier;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        this.done = true;
        synchronized (notifier)
        {
            notifier.notify();
        }

    }

    public boolean isDone() {
        return done;
    }

    String result = null;
    boolean done = false;
}
