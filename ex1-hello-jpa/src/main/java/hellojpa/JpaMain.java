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

            Memberzz member = new Memberzz();
            member.setUsername("member1");
            member.changeTeam(team);
            em.persist(member); // Identity는 디비에 넣어야지 pk값을 알 수 있기 떄문에, persist호출한 시점에서 쿼리가 날라간다.

            //team.getMembers().add(member); // 객체지향적으로 생각했을때 양쪽에 다 해줘야 된다. -> 새터하는 것을 추천

            em.flush(); // commit 전에 쿼리 날림
            em.clear(); // 영속성 컨테스트에 있는 캐쉬 클리어

            Team findTeam = em.find(Team.class, team.getId());
            List<Memberzz> members = findTeam.getMembers();
            for (Memberzz m : members) {
                System.out.println("m = " + m.getUsername());
            }

            tx.commit(); // 이때 디비에 쿼리가 날라감
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
