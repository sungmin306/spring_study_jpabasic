package hellojpa;

import jakarta.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Memberzz member = new Memberzz();
            member.setUsername("member1");

            em.persist(member);

            Team team = new Team();
            team.setName("teamA");
            // 애매해짐
            team.getMembers().add(member); // 업데이트 쿼리가 어쩔수 없이 날라감 (성능상 단점)

            em.persist(team);


            tx.commit(); // 이때 디비에 쿼리가 날라감
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
