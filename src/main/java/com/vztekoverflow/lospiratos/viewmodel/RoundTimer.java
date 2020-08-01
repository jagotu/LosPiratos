package com.vztekoverflow.lospiratos.viewmodel;

import javafx.application.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoundTimer {
    private int nextRoundLength;
    private Game game;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture = null;

    private Calendar roundEnd = null;

    private void trigger()
    {
        Platform.runLater(() -> game.closeRoundAndEvaluate());
    }

    public void restartTimer()
    {
        if(scheduledFuture != null && !scheduledFuture.isDone())
        {
            scheduledFuture.cancel(false);
        }
        scheduledFuture = scheduler.schedule(this::trigger, nextRoundLength, TimeUnit.SECONDS);
        roundEnd = Calendar.getInstance();
        roundEnd.add(Calendar.SECOND, nextRoundLength);
    }

    public void setNextRoundLength(int nextRoundLength) {
        this.nextRoundLength = nextRoundLength;
    }

    public RoundTimer(Game game) {
        this.game = game;
        game.addOnNextRoundStartedListener((roundNo) -> {
            restartTimer();
        });
    }

    public int getNextRoundLength() {
        return nextRoundLength;
    }

    public void stop()
    {
        if(scheduledFuture != null && !scheduledFuture.isDone())
        {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
            roundEnd = null;
        }
    }

    public long getEndTimestamp()
    {
        if(roundEnd == null)
        {
            return -1;
        }
        return roundEnd.getTimeInMillis() / 1000;
    }


}
