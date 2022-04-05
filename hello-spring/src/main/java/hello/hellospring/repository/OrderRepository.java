package hello.hellospring.repository;


import hello.hellospring.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;


    public void save(Order order)
    {
        em.persist(order);
    }

    public Order findOne(Long id)
    {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch)
    {
        return em.createQuery("select o from Order o join o.member m", Order.class).getResultList();
    }
    
    public List<Order> findAll(OrderSearch orderSearch)
    {
        String jpql = "select o from Order o join o.member m";
        boolean isFirst = true;

        if(orderSearch.getOrderStatus()!=null)
        {
            if(isFirst)
            {
                jpql+=" where";
                isFirst = false;
            }else
            {
                jpql+=" and";
            }
            jpql+=" o.status = :status";
        }

        if(StringUtils.hasText(orderSearch.getMemberName()))
        {
            if(isFirst)
            {
                jpql+=" where";
                isFirst = false;
            }else
            {
                jpql+=" and";
            }
            jpql+=" m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if(orderSearch.getOrderStatus()!=null)
        {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }

        if(StringUtils.hasText(orderSearch.getMemberName()))
        {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();


    }
}
