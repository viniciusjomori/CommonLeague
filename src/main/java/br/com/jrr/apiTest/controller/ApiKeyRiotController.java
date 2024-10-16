// package br.com.jrr.apiTest.controller;

// import br.com.jrr.apiTest.domain.Account.AccountRiotRepository;
// import br.com.jrr.apiTest.infra.configsAPI.ApiKeyManager;
// import br.com.jrr.apiTest.domain.API.KeyRiotRegistrationAPI;
// import br.com.jrr.apiTest.domain.Account.AccountRiotWebService;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.util.UriComponentsBuilder;

// @RestController
// @RequestMapping("api/v1/ApiKeyRiot")
// public class ApiKeyRiotController {

// @Autowired
// private AccountRiotWebService service;

//     @Autowired
//     private AccountRiotRepository Repository;
//     @PostMapping("/post")
//     public String postByAPI(@RequestBody @Valid KeyRiotRegistrationAPI data, UriComponentsBuilder uriBuilder){
//         return ApiKeyManager.setApiKey(data);
//     }



// }
