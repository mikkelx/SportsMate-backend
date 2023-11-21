package com.sportevents.sport;

import com.sportevents.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sport")
public class SportController {

    private SportRepository sportRepository;

    @Autowired
    public SportController(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @PostMapping
    public ResponseEntity<?> createSport(@RequestBody Sport sport) {
        return ResponseEntity.ok().body(sportRepository.save(sport));
    }

    @GetMapping
    public ResponseEntity<?> getSportById(@RequestParam Long sportId) {
        return ResponseEntity.ok().body(sportRepository.findById(sportId));
    }

    @PostMapping("/name")
    public ResponseEntity<?> getSportByName(@RequestBody Message message) {
        return ResponseEntity.ok().body(sportRepository.findBySportName(message.getMessage()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Sport>> getAllSports() {
        return ResponseEntity.ok().body(sportRepository.findAll());
    }

}
