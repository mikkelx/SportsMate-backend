package com.sportevents.sport;

import com.sportevents.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sport")
public class SportController {

    private final SportService sportService;

    @Autowired
    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSport(@RequestBody Sport sport) {
        return ResponseEntity.ok().body(sportService.createSport(sport));
    }

    @GetMapping
    public ResponseEntity<?> getSportById(@RequestParam Long sportId) {
        return ResponseEntity.ok().body(sportService.getSportById(sportId));
    }

    @PostMapping("/name")
    public ResponseEntity<?> getSportByName(@RequestBody Message message) {
        return ResponseEntity.ok().body(sportService.getSportByName(message.getMessage()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Sport>> getAllSports() {
        return ResponseEntity.ok().body(sportService.getAllSports());
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateSport(@RequestBody Sport sport) {
        return ResponseEntity.ok().body(sportService.updateSport(sport));
    }

    @PostMapping("/update/multiple")
    public ResponseEntity<?> updateMultipleSports(@RequestBody List<Sport> sports) {
        return ResponseEntity.ok().body(sportService.updateMultipleSports(sports));
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteSport(@RequestParam Long sportId) {
//        sportRepository.deleteById(sportId);
//        return ResponseEntity.ok().body("Sport deleted");
//    }

}
