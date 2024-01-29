package com.sportevents.sport;

import com.sportevents.exception.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportService {

    private final SportRepository sportRepository;

    @Autowired
    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public Sport getSportById(Long sportId) {
        return sportRepository.findById(sportId).orElseThrow(() -> new RuntimeException("Sport not found"));
    }

    public Sport getSportByName(String sportName) {
        return sportRepository.findBySportName(sportName);
    }

    public Sport createSport(Sport sport) {
        if (sportRepository.findBySportName(sport.getSportName()) != null) {
            throw new RequestException("Sport already exists");
        }

        if(sport.getAttributes() == null) {
            throw new RequestException("Sport must have attributes");
        }

        if(sport.getAttributes().isEmpty()) {
            throw new RequestException("Sport must have attributes");
        }

        for(String attribute : sport.getAttributes()) {
            if(attribute.equals("")) {
                throw new RequestException("Sport attribute cannot be empty");
            }
        }

        return sportRepository.save(sport);
    }



    public Sport updateSport(Sport sport) {
        return sportRepository.save(sport);
    }

    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    public Object updateMultipleSports(List<Sport> sports) {
        return sportRepository.saveAll(sports);
    }
}

