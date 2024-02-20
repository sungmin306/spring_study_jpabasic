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
            Member member1 = new Member();
            member1.setId(1L);
            member1.setUsername("홍길동");
            em.persist(member1); // 영속 상태 쓰기 지연 SQL 저장소에 쿼리문 저장

            System.out.println("=======아래부터 쿼리문 날라감======");
            em.flush(); // 강제로 쿼리를 날림
            em.clear(); // 1차 캐시 비우기

            Member findMember = em.find(Member.class, 1L);
            findMember.setUsername("이순신");
            System.out.println("====update 쿼리 날라감 === ");
            tx.commit(); // 이때 디비에 쿼리가 날라감
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
