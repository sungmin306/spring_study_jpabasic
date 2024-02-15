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
            Member member = new Member();
            member.setId(1L);
            member.setUsername("홍길동");

            em.persist(member); // 영속 상태 -> 엔티니매니저 1차 캐시에 저장

            em.flush(); // 쓰기 지연 저장소에 저장되었던 쿼리문을 DB에 반영
            em.clear(); // 캐시 클리어
            Member findMember = em.find(Member.class, 1L);
            System.out.println("이름은 = " + findMember.getUsername());

            tx.commit(); // 이때 디비에 쿼리가 날라감
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
