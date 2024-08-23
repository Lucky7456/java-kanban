package util;

import model.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.stream.LongStream;

public class TimeMapper {
    private static final int PERIOD_TO_MAP = 5;
    private static final int SECONDS_IN_MINUTE = 60;

    private final HashMap<Long, Boolean> mapper;
    private final LocalDateTime programStart;

    public TimeMapper(LocalDateTime programStart) {
        this.programStart = programStart;
        mapper = new HashMap<>();
        LongStream.range(0, timeToMap(programStart.plusYears(1)))
                .forEach(i -> mapper.put(i, false));
    }

    public void add(Task task) {
        LongStream.range(timeToMap(task.getStartTime()), timeToMap(task.getEndTime()))
                .forEach(i -> mapper.put(i, true));
    }

    public void remove(Task task) {
        LongStream.range(timeToMap(task.getStartTime()), timeToMap(task.getEndTime()))
                .forEach(i -> mapper.put(i, false));
    }

    public boolean hasCollision(Task task) {
        return LongStream.range(timeToMap(task.getStartTime()), timeToMap(task.getEndTime()))
                .anyMatch(mapper::get);
    }

    private long timeToMap(LocalDateTime time) {
        return timeToPeriod(time) - timeToPeriod(programStart);
    }

    private long timeToPeriod(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond() / SECONDS_IN_MINUTE / PERIOD_TO_MAP;
    }
}
