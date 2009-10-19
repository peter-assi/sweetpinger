package appchk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    private static final EntityManagerFactory emfInstance =
        Persistence.createEntityManagerFactory("transactions-optional");

    private EMF() {}

    public static EntityManager getManager() {
        return emfInstance.createEntityManager();
    }

    public static void safeClose(EntityManager mgr) {
        if (mgr!=null) {
            try {
                mgr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
}
