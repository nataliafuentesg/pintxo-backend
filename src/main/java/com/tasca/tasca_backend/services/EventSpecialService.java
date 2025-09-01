package com.tasca.tasca_backend.services;
import com.tasca.tasca_backend.dtos.*;
import com.tasca.tasca_backend.models.*;
import com.tasca.tasca_backend.repositories.EventSpecialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventSpecialService {

    private final EventSpecialRepository repo;

    // --- ADMIN ---
    public Page<EventSpecial> list(String type, String q, int page, int size) {
        EventType t = (type == null || type.isBlank()) ? null : EventType.valueOf(type);
        return repo.search(t, q, PageRequest.of(page, size));
    }

    public Optional<EventSpecial> findById(Long id){ return repo.findById(id); }

    public EventSpecial create(EventSpecialUpsertDTO dto) { return repo.save(apply(new EventSpecial(), dto)); }
    public EventSpecial update(Long id, EventSpecialUpsertDTO dto) {
        EventSpecial e = repo.findById(id).orElseThrow();
        return repo.save(apply(e, dto));
    }
    public void delete(Long id){ repo.deleteById(id); }
    public void setActive(Long id, boolean value){
        EventSpecial e = repo.findById(id).orElseThrow();
        e.setActive(value);
        repo.save(e);
    }

    private EventSpecial apply(EventSpecial e, EventSpecialUpsertDTO dto){
        e.setType(EventType.valueOf(dto.getType()));
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setImageUrl(dto.getImageUrl());
        e.setCtaLabel(dto.getCtaLabel());
        e.setCtaTo(dto.getCtaTo());

        e.setScheduleKind(ScheduleKind.valueOf(dto.getScheduleKind()));
        e.setStartsAt(dto.getStartsAt());
        e.setEndsAt(dto.getEndsAt());
        if (dto.getAllDay()!=null) e.setAllDay(dto.getAllDay());
        e.setWeeklyDays(dto.getWeeklyDays());
        e.setWeeklyTime(dto.getWeeklyTime());

        if (dto.getActive()!=null) e.setActive(dto.getActive());
        e.setDisplayOrder(dto.getDisplayOrder());
        return e;
    }

    // --- PUBLIC ---
    public List<EventPublicDTO> listPublic(Integer limit) {
        ZoneId tz = ZoneId.of("America/New_York");     // Boston
        LocalDate today = LocalDate.now(tz);
        DayOfWeek dow = ZonedDateTime.now(tz).getDayOfWeek();

        List<EventSpecial> allActive = repo.findByActiveTrueOrderByDisplayOrderAscIdAsc();

        List<EventSpecial> filtered = allActive.stream()
                .filter(e -> {
                    if (e.getScheduleKind() == ScheduleKind.FIXED_DATE) {
                        LocalDate startD = e.getStartsAt()!=null ? e.getStartsAt().toLocalDate() : null;
                        LocalDate endD   = e.getEndsAt()!=null   ? e.getEndsAt().toLocalDate()   : null;
                        boolean afterStart = (startD == null) || !today.isBefore(startD);
                        boolean beforeEnd  = (endD   == null)   || !today.isAfter(endD);
                        return afterStart && beforeEnd;
                    } else {
                        // WEEKLY: acepta MON|TUE|... / LUN|MAR|... / 1..7
                        String days = Optional.ofNullable(e.getWeeklyDays()).orElse("").trim();
                        if (days.isEmpty()) return true; // permisivo
                        return Arrays.stream(days.split("\\|"))
                                .map(String::trim)
                                .map(this::parseDayToken)    // ← mapea token a DayOfWeek
                                .filter(Objects::nonNull)
                                .anyMatch(d -> d == dow);
                    }
                })
                .toList();

        if (limit != null && limit > 0 && filtered.size() > limit) {
            filtered = filtered.subList(0, limit);
        }

        return filtered.stream().map(e -> toPublic(e, tz)).toList();
    }

    private DayOfWeek parseDayToken(String token) {
        if (token == null || token.isBlank()) return null;
        String t = token.trim().toUpperCase(Locale.ROOT);

        // numérico 1..7 (ISO: 1=MON)
        if (t.length() == 1 && Character.isDigit(t.charAt(0))) {
            int v = Character.digit(t.charAt(0), 10);
            if (v >= 1 && v <= 7) return DayOfWeek.of(v);
        }

        switch (t) {
            case "MON", "MONDAY", "LUN", "LUNES": return DayOfWeek.MONDAY;
            case "TUE", "TUES", "TUESDAY", "MAR", "MARTES": return DayOfWeek.TUESDAY;
            case "WED", "WEDS", "WEDNESDAY", "MIE", "MIER", "MIERCOLES", "MIÉRCOLES": return DayOfWeek.WEDNESDAY;
            case "THU", "THUR", "THURS", "THURSDAY", "JUE", "JUEVES": return DayOfWeek.THURSDAY;
            case "FRI", "FRIDAY", "VIE", "VIERNES": return DayOfWeek.FRIDAY;
            case "SAT", "SATURDAY", "SAB", "SÁB", "SABADO", "SÁBADO": return DayOfWeek.SATURDAY;
            case "SUN", "SUNDAY", "DOM", "DOMINGO": return DayOfWeek.SUNDAY;
            default: return null;
        }
    }

    private EventPublicDTO toPublic(EventSpecial e, ZoneId tz){
        return EventPublicDTO.builder()
                .id(e.getId())
                .type(e.getType().name().toLowerCase()) // "event" | "special"
                .date(buildDateLabel(e, tz))
                .title(e.getTitle())
                .desc(e.getDescription())
                .image(e.getImageUrl())
                .ctaTo(Optional.ofNullable(e.getCtaTo()).orElse("/events"))
                .build();
    }

    private String buildDateLabel(EventSpecial e, ZoneId tz){
        DateTimeFormatter tFmt = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
        DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("EEE", Locale.US);
        LocalDate today = LocalDate.now(tz);

        if (e.getScheduleKind() == ScheduleKind.FIXED_DATE) {
            LocalDateTime start = e.getStartsAt();
            boolean isToday = (start != null && start.toLocalDate().isEqual(today));
            String day = isToday ? "Today" : (start != null ? start.format(dFmt) : "");
            String time = (e.isAllDay() || start == null) ? "All Day" : start.toLocalTime().format(tFmt);
            return (day.isBlank() ? "Today" : day) + " · " + time;
        } else {
            String days = Optional.ofNullable(e.getWeeklyDays()).orElse("").trim();
            String dayLbl = "This Week";
            if (!days.isBlank()) {
                String first = days.split("\\|")[0].trim();
                DayOfWeek firstDow = parseDayToken(first);
                if (firstDow != null) {
                    // Usamos abreviatura en-US: Mon, Tue, ...
                    dayLbl = firstDow.getDisplayName(TextStyle.SHORT, Locale.US);
                }
            }
            String time = (e.getWeeklyTime() != null) ? e.getWeeklyTime().format(tFmt) : "All Day";
            return dayLbl + " · " + time;
        }
    }
}
