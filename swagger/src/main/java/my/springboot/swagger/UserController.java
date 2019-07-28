package my.springboot.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/user")
public class UserController {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1, "John", "Terry"));
        users.add(new User(2, "David", "Silva"));
        users.add(new User(3, "Paul", "Pogaba"));
    }

    @GetMapping("/findAll")
    public List<User> findAll() {
        return users;
    }

    @GetMapping("/findById/{id}")
    @ApiOperation(value = "Finds User by id",
            notes = "... function notes ..."
            //response = User.class
            //responseContainer = "List"
            )
    // https://github.com/swagger-api/swagger-core/wiki/annotations#apioperation
    public User findById(@ApiParam(value = "the user id", required = true, defaultValue = "2") @PathVariable long id) {
        return users.stream().filter( user -> user.getId() == id).findFirst()
                .orElseThrow(() -> new RuntimeException("User not found by id " + id));
    }

    @GetMapping("/findAllWithApiKeyInHeader")
    @ApiOperation(value = "Finds all users with api key in header",
            notes = "This function is protected by the api-key in header. If the api-key in header is not provided, it throws an error."
    )
    public List<User> findAllWithApiKeyInHeader(@RequestHeader("api-key") String apiKey) {
        if("API_SECRET_KEY".equalsIgnoreCase(apiKey)) {
            return users;
        }
        throw new RuntimeException("... api-key is not found in header ...");
    }

    @GetMapping("/findByLastName")
    public List<User> findByLastName(
            @ApiParam(defaultValue = "silva") @RequestParam(value="lastName", required = true) String lastName) {
        return users.stream().filter( user -> user.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    public User createUser(@RequestBody User newUser) {
        users.add(newUser);
        //save new user
        return newUser;
    }

    @PutMapping("/")
    public User updateUser(@RequestBody User updateUser) {
        List<Long> usersId = users.stream().map(User::getId).collect(Collectors.toList());
        int index = usersId.indexOf(updateUser.getId());
        users.set(index, updateUser);
        return updateUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@ApiParam(required = true, defaultValue = "2") @PathVariable long id) {
        List<Long> usersId = users.stream().map(User::getId).collect(Collectors.toList());
        int index = usersId.indexOf(id);
        users.remove(index);
    }
}