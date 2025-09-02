package com.pintxo.services;

import com.pintxo.dtos.Tapas5DTO;
import com.pintxo.models.Tapas5;
import com.pintxo.repositories.Tapas5Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional
public class Tapas5Service {
    private final Tapas5Repository repo;

    public Tapas5Service(Tapas5Repository repo) { this.repo = repo; }

    public Tapas5 getOrCreate() {
        return repo.findByCode("TAPAS5").orElseGet(() -> {
            var e = new Tapas5();
            e.setCode("TAPAS5");
            return repo.save(e);
        });
    }

    public Tapas5DTO getDTO(){
        var e = getOrCreate();
        var d = new Tapas5DTO();
        d.setTitle(e.getTitle());
        d.setEnabled(e.isEnabled());
        d.setDaysCsv(e.getDaysCsv());
        d.setTimezone(e.getTimezone());
        d.setStartTime(e.getStartTime().toString());
        d.setEndTime(e.getEndTime().toString());
        d.setPrice(e.getPrice());
        return d;
    }

    public Tapas5DTO save(Tapas5DTO b){
        var e = getOrCreate();
        e.setTitle(b.getTitle());
        e.setEnabled(b.isEnabled());
        e.setDaysCsv((b.getDaysCsv()==null?"":b.getDaysCsv()).toUpperCase().replaceAll("\\s+",""));
        e.setTimezone(b.getTimezone());
        e.setStartTime(LocalTime.parse(b.getStartTime()));
        e.setEndTime(LocalTime.parse(b.getEndTime()));
        e.setPrice(b.getPrice());
        repo.save(e);
        return getDTO();
    }

    public boolean isActiveNow(){
        var e = getOrCreate();
        if (!e.isEnabled()) return false;
        try {
            var zone = ZoneId.of(e.getTimezone());
            var nowZ = ZonedDateTime.now(zone);
            if (!parseDays(e.getDaysCsv()).contains(nowZ.getDayOfWeek())) return false;
            var t = nowZ.toLocalTime();
            return !t.isBefore(e.getStartTime()) && !t.isAfter(e.getEndTime());
        } catch (Exception ex) { return false; }
    }

    public String windowText(){
        var e = getOrCreate();
        String days = e.getDaysCsv()==null? "" : e.getDaysCsv().replace(",", "·");
        return days + " · " + e.getStartTime() + "–" + e.getEndTime() + " (" + e.getTimezone() + ")";
    }

    private Set<DayOfWeek> parseDays(String csv){
        var out = new LinkedHashSet<DayOfWeek>();
        if (csv == null) return out;
        for (var s : csv.toUpperCase().split(",")) {
            s = s.trim();
            switch (s) {
                case "MON" -> out.add(DayOfWeek.MONDAY);
                case "TUE" -> out.add(DayOfWeek.TUESDAY);
                case "WED" -> out.add(DayOfWeek.WEDNESDAY);
                case "THU" -> out.add(DayOfWeek.THURSDAY);
                case "FRI" -> out.add(DayOfWeek.FRIDAY);
                case "SAT" -> out.add(DayOfWeek.SATURDAY);
                case "SUN" -> out.add(DayOfWeek.SUNDAY);
            }
        }
        return out;
    }
}
