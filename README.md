## Veterinarian CRUD APP

**Project description:** CRUD app using Spring Boot and Spring MVC for a veterinarian clinic's customer and pet information stored into MySQL. Has login information stored into database with BCrypt passwords using Spring Security. Table is set with jQuery data tables. View pages are created using HTML, CSS, Bootstrap and Thymeleaf. For the complete project code, see it on Github [here](https://github.com/kj2386/vetapp).

### Login and registering a new user
New user is created as a POJO and is saved into the database with a Bcrypted password using Spring Security. 
```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
}
```
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/owners/list")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and().exceptionHandling()
                .accessDeniedPage("/access-denied");
    }
```
[<img src="images/registrationform.png?raw=true" width="40%" height="40%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/registrationform.png)
[<img src="images/usersql.png?raw=true" width="40%" height="40%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/usersql.png)

### Main Page
[<img src="images/vetappmain.png?raw=true" width="100%" height="100%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/vetappmain.png)

Table was created using jQuery Data Tables and has the function to alphabatize owner name in ascending and descending order and has a search function. On this page you can add a new owner which is created as a POJO.

```java
@Entity
@Table(name = "owner")
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    private String dob;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(cascade = {CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private List<Pet> pets = new ArrayList<>();
```
### Controllers for Owner and Pet 
```java
@Controller
@RequestMapping("/owners")
public class OwnerController {

    private OwnerService ownerService;


    public OwnerController(OwnerService theOwnerService) {
        ownerService = theOwnerService;
    }

    @GetMapping("/list")
    public String listOwners(Model theModel) {

        List<Owner> theOwners = ownerService.findAll();

        theModel.addAttribute("owners", theOwners);

        return "owners/list-owners";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        Owner theOwner = new Owner();
        theModel.addAttribute("owner", theOwner);

        return "owners/owner-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("ownerId") int theId, Model theModel) {
        Owner theOwner = ownerService.findById(theId);
        theModel.addAttribute("owner", theOwner);
        return "owners/owner-form";
    }

    @PostMapping("/save")
    public String saveOwner(@ModelAttribute("owner") Owner theOwner) {
        if (theOwner.getId() == 0) {
            ownerService.save(theOwner);
            return "redirect:/owners/list";
        }
        Owner currOwner = ownerService.findById(theOwner.getId());
        theOwner.setPets(currOwner.getPets());
        ownerService.save(theOwner);
        return "redirect:/owners/list";
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("ownerId") int theId) {
        ownerService.deleteById(theId);
        return "redirect:/owners/list";
    }

    @GetMapping("/ownerDetails")
    public String ownerDetails(@RequestParam("ownerId") int theId, Model theModel) {
        Owner theOwner = ownerService.findById(theId);
        theModel.addAttribute("owner", theOwner);
        return "owners/owner-details";

    }
}
```
```java
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private PetService petService;

    @Autowired
    private OwnerService ownerService;

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") int theId) {
        return ownerService.findById(theId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    public PetController(PetService thePetService) {
        petService = thePetService;
    }

    @GetMapping("/list")
    public String listPets(Model theModel) {

        List<Pet> thePets = petService.findAll();

        theModel.addAttribute("pets", thePets);

        return "pets/list-pets";
    }

    @GetMapping("/pets/new")
    public String showFormForAdd(Owner owner, Model theModel) {
        Pet thePet = new Pet();
        owner.addPet(thePet);
        theModel.addAttribute("pet", thePet);

        return "pets/pet-form";
    }

    @PostMapping("/pets/save")
    public String savePet(@ModelAttribute("pet") Pet thePet, Owner theOwner) {
        theOwner.getPets().add(thePet);
        thePet.setOwner(theOwner);
        petService.save(thePet);

        return "redirect:/owners/list";
    }

    @GetMapping("/petDetails")
    public String petDetails(@RequestParam("petId") int theId, Model theModel) {
        Pet thePet = petService.findById(theId);
        theModel.addAttribute("pet", thePet);
        return "pets/pet-details";
    }

    @GetMapping("/pets/update")
    public String showFormForUpdate(@RequestParam("petId") int theId, Model theModel) {
        Pet thePet = petService.findById(theId);
        theModel.addAttribute("pet", thePet);
        return "pets/pet-form";
    }

    @GetMapping("/pets/delete")
    public String deletePet(@RequestParam("petId") int theId) {
        petService.deleteById(theId);
        return "redirect:/owners/list";
    }

}
```

### Owner Details
Clicking on an owner's name on the mainpage will bring up the owner's details. From here you can add any pets and update the owner's information. Any updates will be saved into the database. 

[<img src="images/ownerdetails.png?raw=true" width="75%" height="75%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/ownerdetails.png)

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">


    <title>Owner Information</title>
</head>

<body>

<div class="container">

    <h3>Owner Information</h3>
    <hr>

    <table id="owner_details" class="display">
        <thead class="thead-dark">
        <tr>
            <th>Name</th>
            <th>DOB</th>
            <th>Address</th>
            <th>City</th>
            <th>State</th>
            <th>Zip</th>
            <th>Email</th>
            <th>Phone Number</th>
            <th>Pets</th>
        </thead>

        <tbody>
        <tr th:object="${owner}">
            <td th:text="${owner.firstName + ' ' + owner.lastName}"/>
            <td th:text="${owner.dob}"/>
            <td th:text="${owner.address}"/>
            <td th:text="${owner.city}"/>
            <td th:text="${owner.state}"/>
            <td th:text="${owner.zip}"/>
            <td th:text="${owner.email}"/>
            <td th:text="${owner.phoneNumber}"/>
            <td>
                <a th:each="pet : ${owner.pets}"
                   th:href="@{{id}}/petDetails(petId=${pet.id}, id=${owner.id})}"
                   th:text="${pet.name + ' '}"/>
            </td>
        </tr>
        </tbody>
    </table>

    <!-- Update -->
    <a th:href="@{/owners/showFormForUpdate(ownerId=${owner.id})}"
       class="btn btn-info btn-sm">
        Update
    </a>

    <!-- Delete -->
    <a th:href="@{/owners/delete(ownerId=${owner.id})}"
       class="btn btn-danger btn-sm"
       onclick="if (!(confirm('Are you sure you want to delete this owner?'))) return false">
        Delete
    </a>

    <!-- Add Pet -->
    <a th:href="@{{id}}/pets/new(id=${owner.id})}"
       class="btn btn-primary btn-sm">
        Add Pet
    </a>

    <hr>
    <a th:href="@{/owners/list}">Back to Owners List</a>

</div>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
   $(document).ready(function() {
    $('#owner_details').DataTable();
} );


</script>
</body>
</html>
```
The new pet form will populate the owner's name.

[<img src="images/newpet.png?raw=true" width="75%" height="75%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/newpet.png)

### Pet Details
After adding a pet, clicking on the pet's name will bring up the pet's details similar to the owner's detail page. From there you may update or delete the pet. 

[<img src="images/petdetails.png?raw=true" width="75%" height="75%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/petdetails.png)

---

For the complete project code, see it [here](https://github.com/kj2386/vetapp).
