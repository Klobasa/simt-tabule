package cz.simt.tabule.service;

import cz.simt.tabule.data.Times;
import cz.simt.tabule.repository.TimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TimesService {
    private final TimesRepository timesRepository;

    @Autowired
    public TimesService(TimesRepository timesRepository) {
        this.timesRepository = timesRepository;
    }

    public void saveTime(Times time) {
        timesRepository.save(time);
    }

    public LocalDateTime getTimeById(String id) {
        Optional<Times> time = timesRepository.findById(id);
        return time.map(Times::getTime).orElse(null);

    }

}
