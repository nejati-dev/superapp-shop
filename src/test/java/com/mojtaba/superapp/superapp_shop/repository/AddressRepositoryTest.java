package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User persistedUser;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("repo@user.com");
        user.setPhone("+93700123456");
        user.setPasswordHash("ignored");
        user.setPreferredLang("en");
        persistedUser = userRepository.save(user);
        entityManager.flush();
    }

    @Test
    void shouldFindAddressByIdAfterSaving() {
        Address address = new Address();
        address.setUser(persistedUser);
        address.setLabel("Home");
        address.setFullAddress("123 Repo St");
        address.setCity("Reposville");
        address.setProvince("Test Province");
        address.setPostalCode("REP123");


        Point point = geometryFactory.createPoint(new Coordinate(51.3890, 35.6895));
        address.setLocation(point);

        Address saved = addressRepository.save(address);
        entityManager.flush();

        Optional<Address> found = addressRepository.findById(saved.getAddressId());
        assertThat(found).isPresent();

        Address fetched = found.get();
        assertThat(fetched.getLabel()).isEqualTo("Home");
        assertThat(fetched.getUser().getUserId()).isEqualTo(persistedUser.getUserId());
        assertThat(fetched.getLocation()).isNotNull();
        assertThat(fetched.getLocation().getX()).isEqualTo(point.getX());
        assertThat(fetched.getLocation().getY()).isEqualTo(point.getY());
    }

    @Test
    void shouldFindAllAddressesByUserId() {
        Address address1 = new Address();
        address1.setUser(persistedUser);
        address1.setLabel("Office");
        address1.setFullAddress("Office Rd");
        address1.setCity("Reposville");
        address1.setProvince("Province");
        address1.setPostalCode("OFF001");
        address1.setLocation(null);
        addressRepository.save(address1);

        Address address2 = new Address();
        address2.setUser(persistedUser);
        address2.setLabel("Secondary");
        address2.setFullAddress("Second St");
        address2.setCity("Reposville");
        address2.setProvince("Province");
        address2.setPostalCode("SEC002");
        address2.setLocation(null);
        addressRepository.save(address2);

        entityManager.flush();

        List<Address> results = addressRepository.findByUserUserId(persistedUser.getUserId());
        assertThat(results).hasSize(2)
                .extracting(Address::getLabel)
                .containsExactlyInAnyOrder("Office", "Secondary");
    }
}
