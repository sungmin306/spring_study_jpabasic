package hellojpa;

import jakarta.persistence.*;
import java.util.List;
import org.hibernate.Hibernate;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);
            em.persist(parent);
            em.persist(child1);
            em.persist(child2);

            Parent findParent = em.find(Parent.class, parent.getId());
            em.remove(findParent);



            tx.commit(); // 이때 디비에 쿼리가 날라감
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }
    public static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());

    }
}

//n+1 문제 발생
//            List<Member> members = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();


//            Member m = em.find(Member.class, member1.getId());
//            System.out.println("m = " + m.getTeam().getClass()); // Lazy 해서 프록시로 가져옴
//
//            System.out.println("============");
//            System.out.println(m.getTeam().getName()); // 초기화
//            System.out.println("============");

// 프록시 예제(후반)
//            Member reMember = em.getReference(Member.class, member1.getId());
//            System.out.println("reMember = " + reMember.getClass());
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(reMember)); // 초기화 여부 확인
//            //reMember.getUsername();
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(reMember)); // 초기화 여부 확인
//            Hibernate.initialize(reMember); // 강제 초기화







//프록시 예제
////            Member findMember = em.find(Member.class, member.getId());
//            Member findMember = em.getReference(Member.class, member1.getId()); // 이 시점에서는 쿼리가 안날라가고
//            System.out.println("before findMEmber = " + findMember.getClass()); // 얘 이름 Member$HibernateProxy$wKpeG71d -> 프록시 클래스
//            System.out.println("findMember.id" + findMember.getId());
//            System.out.println("findMember.username = " + findMember.getUsername()); // DB조회를 해야하는 시점에서 이때 쿼리가 날라감
//            System.out.println("after findMEmber = " + findMember.getClass());