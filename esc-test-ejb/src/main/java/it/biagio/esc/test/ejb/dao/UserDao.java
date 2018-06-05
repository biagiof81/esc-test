package it.biagio.esc.test.ejb.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.biagio.esc.test.ejb.entity.Location;
import it.biagio.esc.test.ejb.entity.User;

@Stateless
public class UserDao {

    /*
     * @Inject
     * 
     * @PersistenceContext(unitName="escPU")
     */
    protected EntityManager entityManager;

    protected EntityManagerFactory factory;

    protected CriteriaBuilder builder;

    @PostConstruct
    protected void initialize() {
        factory = Persistence.createEntityManagerFactory("escPU");
        entityManager = factory.createEntityManager();
        builder = entityManager.getCriteriaBuilder();
    }

    public List<User> getUsersByCoordinate(Location location1, Location location2, Long dateFrom, Long dateTo) {

        CriteriaQuery<User> query = builder.createQuery(User.class);

        Root<User> root = query.from(User.class);

        Join<User, Location> location = root.join("location");
        query.where(builder.and(whereCondition(location, location1, location2, dateFrom, dateTo)));
        query.distinct(true);
        return entityManager.createQuery(query).getResultList();

    }

    /**
     * forse metterei pure il lock
     * 
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User toggle(User user) {

        User savedUser = entityManager.find(User.class, user.getId());
        savedUser.setActive(user.getActive());

        return entityManager.merge(savedUser);

    }

    public Predicate whereCondition(Join<?, ?> root, Location location1, Location location2, Long dateFrom,
            Long dateTo) {
        Path<Double> longitudine = root.get("longitudine");
        Path<Double> latitude = root.get("latitude");
        Path<Long> insdate = root.get("insdate");
        return builder.and(
                builder.and(builder.greaterThanOrEqualTo(longitudine, location1.getLongitudine()),
                        builder.lessThanOrEqualTo(longitudine, location2.getLongitudine())),
                builder.and(builder.lessThanOrEqualTo(latitude, location1.getLatitude()),
                        builder.greaterThanOrEqualTo(latitude, location2.getLatitude())),
                builder.and(builder.greaterThanOrEqualTo(insdate, dateFrom),
                        builder.lessThanOrEqualTo(insdate, dateTo)));

    }

}
