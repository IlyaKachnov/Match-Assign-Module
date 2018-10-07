package app.comparators;

import app.dto.SlotDTO;
import app.utils.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Comparator;

public class SlotDTOComparator implements Comparator<SlotDTO> {
    @Override
    public int compare(SlotDTO o1, SlotDTO o2) {
        LocalDateTime firstLocalDateTime = DateTimeUtil
                .convertDatesToLocalDateTime(o1.getSlot().getEventDate(), o1.getSlot().getStartTime());
        LocalDateTime secondLocalDateTime = DateTimeUtil
                .convertDatesToLocalDateTime(o2.getSlot().getEventDate(), o2.getSlot().getStartTime());
        return firstLocalDateTime.compareTo(secondLocalDateTime);
    }
}
