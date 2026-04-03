package lab7.controller;

import lab6.model.Actor;
import lab7.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    
    private final ActorService actorService;
    
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }
    
    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Integer id) {
        return actorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Actor createActor(@RequestBody Actor actor) {
        return actorService.save(actor);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable Integer id, @RequestBody Actor actor) {
        return actorService.findById(id)
                .map(existingActor -> {
                    actor.setId(id);
                    return ResponseEntity.ok(actorService.save(actor));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        if (actorService.findById(id).isPresent()) {
            actorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
