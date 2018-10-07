package app.dto;

import app.models.Stadium;

import java.util.List;

public class StadiumSlotsDTO {

    private Stadium stadium;
    private List<SlotDTO> slotDTO;

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public List<SlotDTO> getSlotDTO() {
        return slotDTO;
    }

    public void setSlotDTO(List<SlotDTO> slotDTO) {
        this.slotDTO = slotDTO;
    }
}
