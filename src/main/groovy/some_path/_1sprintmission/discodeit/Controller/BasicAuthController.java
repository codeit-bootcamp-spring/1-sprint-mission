package some_path._1sprintmission.discodeit.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import some_path._1sprintmission.discodeit.dto.UserDTO;
import some_path._1sprintmission.discodeit.service.AuthService;

@RestController
@RequestMapping("/auth")
public class BasicAuthController {

    private final AuthService authService;

    public BasicAuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody String username, String password){
        UserDTO loginUser = authService.login(username, password);

        return new ResponseEntity(loginUser, HttpStatus.OK);
    }

}
