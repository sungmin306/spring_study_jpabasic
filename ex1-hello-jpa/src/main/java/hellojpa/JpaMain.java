package hellojpa;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("hoemcity1", "street", "100000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("old1", "street", "100000"));
            member.getAddressHistory().add(new Address("old2", "street", "100000"));

            em.persist(member);

            em.flush();
            em.clear();
            System.out.println("===========start===============");
            Member findMember = em.find(Member.class, member.getId());

            // 수정
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

            //치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");


            findMember.getAddressHistory().remove(new Address("old1", "street", "100000")); // equals 가 중요함
            findMember.getAddressHistory().add(new Address("newCity1", "street", "100000")); // equals 가 중요함

            // 조회
//            List<Address> addressHisory = findMember.getAddressHistory();
//            for(Address address : addressHisory) {
//                System.out.println("address = " + address.getCity());
//            }
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for(String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }
            tx.commit(); // 이때 디비에 쿼리가 날라감
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
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