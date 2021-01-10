package liveproject.m2k8s.web;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Value("${images.directory:/tmp}")
    private String uploadFolder;

    @Value("classpath:ghost.jpg")
    private Resource defaultImage;

    @Autowired
    public ProfileController(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public void saveUser(@RequestBody @Validated final Profile newProfile) {
        profileService.save(newProfile);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<Profile> showProfile(@PathVariable @NotNull final String username) {
        log.debug("Reading model for: '{}'", username);
        return Optional.ofNullable(profileService.getProfile(username))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping(value = "/{username}")
    @Transactional
    public void updateProfile(@PathVariable @NotNull final String username,
                              @RequestBody @Validated Profile user) {
        if (!username.equals(user.getUsername())) {
            throw new RuntimeException("Cannot change username for Profile");
        }
        log.debug("Updating model for: '{}' ", username);
        profileService.update(user);
    }

}
