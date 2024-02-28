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

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            member.setTeam(teamB);
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();
            String name = findTeam.getName();
            System.out.println(name);

            // 둘의 연관관계 생성

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
