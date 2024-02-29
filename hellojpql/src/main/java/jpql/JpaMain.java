package jpql;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }
//            Member member = new Member();
//            member.setUsername("member1");
//            member.setAge(10);
//            em.persist(member);

            em.flush();
            em.clear();

            //페이징

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size = " + result.size());
            for(Member member1 : result) {
                System.out.println("member 1 = " + member1);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

}

//// 반환 예제
//TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//    TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//    Query query3 = em.createQuery("select m.username, m.age from Member m", String.class); // 반환 타입 명확하지 않음


// 파라미터 바인딩
//Member result = em.createQuery("select m from Member m where.m.username = ?1", Member.class)
//        .setParameter("username", "member1")
//        .getSingleResult();
//
//            System.out.println("result = " + result.getUsername());


//    List<Member> result = em.createQuery("select m from Member m ", Member.class)
//                            .getResultList();
//
//            Member findMember = result.get(0); // 바뀌면 영속성 컨텍스트에서 관리
//            findMember.setAge(20);

////조인
//List<Member> result = em.createQuery("select t from Member m join m.team t ", Member.class) // 눈에 확보이지 않음 join 명시 해야함
//        .getResultList();