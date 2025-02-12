package src.main.java.com.notice.model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Task implements Comparable<Task> {

    private Runnable job;
    private LocalTime setTime;

    public Task(Runnable job, LocalTime setTime) {
        this.job = job;
        this.setTime = setTime;
    }

    public LocalTime getSetTime() {
        return setTime;
    }

    public void setSetTime(LocalTime setTime) {
        this.setTime = setTime;
    }

    public Runnable getJob() {
        return job;
    }

    public void setJob(Runnable job) {
        this.job = job;
    }

    @Override
    public int compareTo(Task o) {
        return this.setTime.compareTo(o.setTime);
    }
}
