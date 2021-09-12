package EveEventManager;

import Enums.TimezonesEnum;

public class GameTime {
    Integer hour;
    Integer minute;
    String dayOrNight;
    TimezonesEnum timezone;

    public GameTime(String[] time, TimezonesEnum timezone) {
        String[] timeSplit = time[0].split(":");
        hour = formatHour(timeSplit[0]);
        minute = Integer.parseInt(timeSplit[1]);
        dayOrNight = time[1];
        this.timezone = timezone;
    }

    public Integer formatHour(String hour) {
        int hourNum = Integer.parseInt(hour);

        if(hourNum > 12) {
            return hourNum - 12;
        }

        return hourNum;
    }

    @Override
    public String toString() {
        if(minute < 10) {
            return hour.toString() + ":0" + minute + " " + dayOrNight + " " + timezone;
        }

        return hour.toString() + ":" + minute + " " + dayOrNight + " " + timezone;
    }
}
