package org.manathome.totask2.service;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.internal.IdentifierEqAuditExpression;
import org.manathome.totask2.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/** 
 * query historic data on versioned domain models (e.g. projects, tasks..) 
 * 
 * @see AuditReader
 * 
 * @author man-at-home
 * */
@Service
@Transactional
public class AuditingService {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuditingService.class);
    
    @PersistenceContext private EntityManager entityManager;
    
    /** revision holder. */
    public static class EntityRevision {
        
        private Number revision;
        private Object historicEntity;
        
        /** ctor. */
        public EntityRevision(Number revision, Object historicEntity) {
            this.revision = revision;
            this.historicEntity = historicEntity;
        }

        /** revision number. */
        public Number getRevision() { 
            return this.revision; 
        }
        
        /** domain model (historic values valid in given revision). */
        public Object getEntity()   { 
            return this.historicEntity; 
        }
        
        /** debug. */
        @Override
        public String toString() { 
            return "rev " + getRevision(); 
        }        
    }
    
    

    /** 
     * get revision log for given entity id. 
     * 
     * @param versionedDomainClass class to version
     * @param primaryKeyId         pk
     * @return list of historic data
     * */
    public List<EntityRevision> retrieveHistory(Class<?> versionedDomainClass, long primaryKeyId) {
        
        List<EntityRevision>  revisions = new ArrayList<EntityRevision>();
        
        AuditReader ar = AuditReaderFactory.get(entityManager);
        
        List<Number> revNumbers = ar.getRevisions(Project.class, primaryKeyId);
        
        for (Number rev : revNumbers) {
            AuditQuery query = 
                    ar.createQuery()
                        .forEntitiesAtRevision(Project.class, rev)
                        .add(new IdentifierEqAuditExpression(primaryKeyId, true));
            
            Object o = query.getResultList().get(0);            
            revisions.add(new EntityRevision(rev, o));      
        }
        
        LOG.debug("built full history on " 
                + versionedDomainClass.getName() 
                + "[" + primaryKeyId + "], with " 
                + revisions.size() 
                + "changes");
        
        return revisions;
    }
}
