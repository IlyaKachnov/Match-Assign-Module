package app.comparators;

import app.dto.NotificationDTO;

import java.util.Comparator;

public class NotificationComparator implements Comparator<NotificationDTO> {
    @Override
    public int compare(NotificationDTO o1, NotificationDTO o2) {
        return o1.getStartTime().compareTo(o2.getStartTime());
    }
}
