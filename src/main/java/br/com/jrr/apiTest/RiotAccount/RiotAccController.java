package br.com.jrr.apiTest.RiotAccount;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.App.ResponseDTO;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccClientDTO;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccConnectDTO;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/lol/account")
public class RiotAccController {

    @Autowired
    private RiotAccService accountService;

    @Autowired
    private RiotAccMapper mapper;

    @Autowired
    private UserService userService;
    
    @PostMapping
    @RolesAllowed("CLIENT")
    public ResponseEntity<RiotAccClientDTO> connect(@RequestBody @Valid RiotAccConnectDTO request) {
        RiotAccEntity entity = accountService.connect(request);
        RiotAccClientDTO response = mapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @RolesAllowed("CLIENT")
    public ResponseEntity<ResponseDTO> disconnect() {
        UserEntity user = userService.getCurrentUser();
        accountService.disconnect(user);

        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Disconnected");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    @RolesAllowed("CLIENT")
    public ResponseEntity<RiotAccClientDTO> findCurrentAccount() {
        RiotAccEntity entity = accountService.findCurrentAccount();
        RiotAccClientDTO response = mapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @RolesAllowed("CLIENT")
    public ResponseEntity<RiotAccClientDTO> findByUser(@RequestParam("user") UUID id) {
        UserEntity user = userService.findById(id);
        RiotAccEntity entity = accountService.findByUser(user);
        RiotAccClientDTO response = mapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }
}
