## Veterinarian CRUD APP

**Project description:** CRUD app using Spring Boot for a veterinarian clinic stored into MySQL. Has login information stored into database with BCrypt passwords. Table is set with jQuery data tables.

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
Owner and pet are created as POJOs and linked together by a foreign key. Deleting an owner will delete the pet/pets associate with that owner. Table was created using jQuery Data Tables and has the function to alphabatize owner name in ascending and descending order and has a search function. 

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
```java
@Entity
@Table(name = "pet")
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type_of_pet")
    private String typeOfPet;

    @Column(name = "breed")
    private String breed;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
```
[<img src="images/vetappmain.png?raw=true" width="100%" height="100%"/>](https://raw.githubusercontent.com/kj2386/vetapp/master/images/vetappmain.png)

Clicking on an owner's name will bring up more details about that owner with their pets. 

 


### 3. Support the selection of appropriate statistical tools and techniques

<img src="images/dummy_thumbnail.jpg?raw=true"/>

### 4. Provide a basis for further data collection through surveys or experiments

Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. 

For more details see it on Github [here](https://github.com/kj2386/vetapp).
