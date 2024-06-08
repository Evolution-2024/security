package com.acme.learningcenter.security.api;

import com.acme.learningcenter.security.domain.service.UserService;
import com.acme.learningcenter.security.domain.service.communication.AuthenticateRequest;
import com.acme.learningcenter.security.domain.service.communication.RegisterRequest;
import com.acme.learningcenter.security.mapping.UserMapper;
import com.acme.learningcenter.security.resource.UserResource;
import com.acme.learningcenter.shared.exception.ResourceNotFoundException;
import com.acme.learningcenter.shared.exception.ResourceValidationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Users", description = "Create, read, update and delete users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
  private final UserService userService;
  private final UserMapper mapper;

  public UsersController(UserService userService, UserMapper mapper) {
    this.userService = userService;
    this.mapper = mapper;
  }

  @GetMapping("/auth/{userId}")
  public ResponseEntity<?> getById(@PathVariable Long userId) {

    try {
      UserResource resources = mapper.toResource(userService.getById(userId));
      return ResponseEntity.ok(resources);
    } catch (ResourceValidationException | ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

  }

  @PostMapping("/auth/sign-in")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticateRequest request) {
    return userService.authenticate(request);
  }

  @PostMapping("/auth/sign-up")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
    return userService.register(request);
  }

  @GetMapping
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAllUsers(Pageable pageable) {
    Page<UserResource> resources = mapper.modelListToPage(userService.getAll(), pageable);
    return ResponseEntity.ok(resources);
  }

  @GetMapping("/auth/all")
  public ResponseEntity<List<UserResource>> getFilters(
    @RequestParam(required = false) Long id,
    @RequestParam(required = false) Long roleId
  ) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", id);
    parameters.put("roleId", roleId);
    try {
      List<UserResource> list = mapper.modelListToResource(userService.getByFilter(parameters));
      return ResponseEntity.ok(list);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
