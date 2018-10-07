package app.services;

import app.dto.SlotDTO;
import app.dto.StadiumSlotsDTO;
import app.models.*;
import app.repositories.SlotRepository;
import app.repositories.SlotSignificationTimeRepository;
import app.repositories.StadiumRepository;
import app.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;
    private final SlotRepository slotRepository;
    private final SlotSignificationTimeRepository slotSignificationTimeRepository;

    @Autowired
    public StadiumServiceImpl(StadiumRepository stadiumRepository, SlotRepository slotRepository,
                              SlotSignificationTimeRepository slotSignificationTimeRepository) {
        this.stadiumRepository = stadiumRepository;
        this.slotRepository = slotRepository;
        this.slotSignificationTimeRepository = slotSignificationTimeRepository;
    }

    @Override
    public Stadium findById(Long id) {
        return this.stadiumRepository.findById(id).orElse(null);
    }

    @Override
    public List<Stadium> findAll() {
        return this.stadiumRepository.findAll();
    }

    @Override
    public void save(Stadium stadium) {
        this.stadiumRepository.save(stadium);
    }

    @Override
    public void delete(Long id) {
        this.stadiumRepository.deleteById(id);
    }

    @Override
    public Stadium findByName(String name) {
        return stadiumRepository.findByName(name);
    }

    @Override
    public List<Stadium> findAllWithSlots() {
        return stadiumRepository.findAllWithSlots();
    }

    @Override
    public List<StadiumSlotsDTO> findAllWithSlotsByUser(User user) {
        List<Stadium> stadiumsWithAllSlots = findAllWithSlotsByDate();
        if (user.getRole().equals(Role.adminRole)) {
            return convertToStadiumSlotsDTO(stadiumsWithAllSlots, user);
        }
        stadiumsWithAllSlots.forEach(stadium -> {
            List<Slot> resultSlotsForStadium = new ArrayList<>();
            stadium.getSlots().forEach(slot -> {
                if (slot.getMatch() == null && slot.getSlotType().getSignifiable()) {
                    resultSlotsForStadium.add(slot);
                } else if (slot.getMatch() != null &&
                        (slot.getMatch().getHomeTeam().getUser().equals(user) ||
                                slot.getMatch().getGuestTeam().getUser().equals(user))) {
                    resultSlotsForStadium.add(slot);
                }
                stadium.setSlots(resultSlotsForStadium);
            });
        });
        return convertToStadiumSlotsDTO(stadiumsWithAllSlots, user);
    }

    @Override
    public List<Stadium> findAllWithSlotsByDate() {
        LocalDate date = LocalDate.now();
        DayOfWeek curDayOfWeek = date.getDayOfWeek();
        LocalDate startDate = date.minusDays(curDayOfWeek.getValue() - 1);
        LocalDate endDate = startDate.plusDays(Calendar.DAY_OF_WEEK - 1);
        List<Stadium> stadiums = stadiumRepository.findAll();
        stadiums.forEach(stadium ->
                stadium.setSlots(slotRepository.findAllSlotsByStadiumAndDate(stadium,
                        Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))));
        List<Stadium> resultStadiums = new ArrayList<>();
        stadiums.forEach(stadium -> {
            if (stadium.getSlots() != null && !stadium.getSlots().isEmpty()) {
                resultStadiums.add(stadium);
            }
        });
        return resultStadiums;
    }

    private List<StadiumSlotsDTO> convertToStadiumSlotsDTO(List<Stadium> stadiumList, User user) {
        List<StadiumSlotsDTO> result;
        if (user.getRole().equals(Role.adminRole)) {
            result = getStadiumSlotDTOForAdmin(stadiumList);
        } else {
            result = getStadiumSlotDTOForManager(stadiumList, user);
        }
        return result;
    }

    private List<StadiumSlotsDTO> getStadiumSlotDTOForAdmin(List<Stadium> stadiumList) {
        List<StadiumSlotsDTO> result = new ArrayList<>();
        stadiumList.forEach(stadium -> {
            StadiumSlotsDTO stadiumSlotsDTO = new StadiumSlotsDTO();
            stadiumSlotsDTO.setStadium(stadium);
            List<SlotDTO> slotDTOList = new ArrayList<>();
            stadium.getSlots().forEach(slot -> {
                SlotDTO slotDTO = new SlotDTO();
                slotDTO.setSlot(slot);
                if (slot.getMatch() == null) {
                    slotDTO.setUrl("<a href=\"/stadium/" + stadium.getId() + "/signify/" + slot.getId() + "\">Занять</a>");
                } else {
                    slotDTO.setUrl("<a href=\"/stadium/" + stadium.getId() + "/reject/" + slot.getId() + "\">Отменить</a>");
                }
                slotDTOList.add(slotDTO);
            });
            stadiumSlotsDTO.setSlotDTO(slotDTOList);
            result.add(stadiumSlotsDTO);
        });
        return result;
    }

    private List<StadiumSlotsDTO> getStadiumSlotDTOForManager(List<Stadium> stadiumList, User user) {
        List<StadiumSlotsDTO> result = new ArrayList<>();
        stadiumList.forEach(stadium -> {
            StadiumSlotsDTO stadiumSlotsDTO = new StadiumSlotsDTO();
            stadiumSlotsDTO.setStadium(stadium);
            List<SlotDTO> slotDTOList = new ArrayList<>();
            stadium.getSlots().forEach(slot -> {
                SlotDTO slotDTO = new SlotDTO();
                slotDTO.setSlot(slot);
                if (slot.getMatch() == null && isTimeToSignifySlots(user)) {
                    slotDTO.setUrl("<a href=\"/stadium/" + stadium.getId() + "/signify/" + slot.getId() + "\">Занять</a>");
                } else if (isTimeToSignifySlots(user) && slot.getMatch().getHomeTeam().getUser().equals(user)) {
                    slotDTO.setUrl("<a href=\"/stadium/" + stadium.getId() + "/reject/" + slot.getId() + "\">Отменить</a>");
                }
                slotDTOList.add(slotDTO);
            });
            stadiumSlotsDTO.setSlotDTO(slotDTOList);
            result.add(stadiumSlotsDTO);
        });
        return result;
    }

    private boolean isTimeToSignifySlots(User user) {
        List<SlotSignificationTime> slotSignificationTimes = slotSignificationTimeRepository.getUserSessions(user.getId());
        return slotSignificationTimes.stream().anyMatch(slotSignificationTime -> {
            LocalDateTime startTime = DateTimeUtil.convertDatesToLocalDateTime(slotSignificationTime.getStartDate(),
                    slotSignificationTime.getStartTime());
            LocalDateTime endTime = DateTimeUtil.convertDatesToLocalDateTime(slotSignificationTime.getEndDate(),
                    slotSignificationTime.getEndTime());
            LocalDateTime localDateTime = LocalDateTime.now();
            return localDateTime.isAfter(startTime) && localDateTime.isBefore(endTime);
        });
    }

//    public List<Stadium> findAllWithSlots() {
//        List<Stadium> stadiumList = stadiumRepository.findAll();
//        List<Stadium> stadiumsWithSlots = new ArrayList<>();
//        stadiumList.forEach(stadium -> {
//            if (!stadium.getSlots().isEmpty()) {
//                stadiumsWithSlots.add(stadium);
//            }
//        });
//
//        return stadiumsWithSlots;
//    }
}
