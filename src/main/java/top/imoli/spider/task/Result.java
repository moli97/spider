package top.imoli.spider.task;

/**
 * @author moli@hulai.com
 * @date 2022/1/25 2:54 PM
 */
public class Result {

    private String name;
    private boolean cancelled;
    private boolean done;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
