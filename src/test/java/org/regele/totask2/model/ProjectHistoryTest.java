package org.regele.totask2.model;

import static org.junit.Assert.*;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.internal.IdentifierEqAuditExpression;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.regele.totask2.Application;
import org.regele.totask2.service.AuditingService;
import org.regele.totask2.service.AuditingService.EntityRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * testing historical project data access.
 *
 * @see Project
 * @see AuditReader
 * 
 * @author man-at-home
 */
@RunWith(SpringJUnit4ClassRunner.class)
// no! @Transactional
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
public class ProjectHistoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectHistoryTest.class);

    @Autowired
    private ProjectRepository projectRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaTransactionManager transactionManager;
    
    @Autowired
    private AuditingService auditingService;

    private long projectId = 0;

    @Before
    public void ensureProjectDataToQuery() {

        TransactionTemplate transactionTemplate = new TransactionTemplate(
                transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            // need separated transaction to get version entries in same junit
            // test.

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                Project p = new Project();
                p.setName("project, oldest");
                projectId = projectRepository.saveAndFlush(p).getId();
            }
        });

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                Project p = projectRepository.findOne(projectId);
                p.setName("project old");
                projectRepository.saveAndFlush(p);
            }
        });

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                Project p = projectRepository.findOne(projectId);
                p.setName("project current version");
                projectRepository.saveAndFlush(p);
            }
        });

        assertTrue(projectId > 0);
    }

    /** ensure hibernate-envers history creation is enabled. */
    @Test
    public void testProjectHistoryAccess() {

        TransactionTemplate transactionTemplate = new TransactionTemplate(
                transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {

                // tag::developer-manual-history-query[]

                LOG.debug("flash back query for project" + projectId);

                AuditReader ar = AuditReaderFactory.get(entityManager);
                AuditQuery query = ar.createQuery().forRevisionsOfEntity(
                        Project.class, true, true);
                query.add(new IdentifierEqAuditExpression(projectId, true));

                @SuppressWarnings("unchecked")
                List<Project> result = query.getResultList();

                result.stream().forEach(
                        p -> LOG.debug("historic project entry: " + p));

                // end::developer-manual-history-query[]

                assertEquals("3 versions of project: " + projectId
                        + " expected.", 3, result.size());
            }
        });

    }

    /** 
     * retrieve full history trail of test project. 
     * 
     * @see AuditingService
     * 
     */
    @Test
    public void testProjectHistoryAccessDetails() {

        TransactionTemplate transactionTemplate = new TransactionTemplate(
                transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {

                LOG.debug("flash back query for project" + projectId);
                
                List<EntityRevision> history = auditingService.retrieveHistory(Project.class, projectId);
                assertNotNull(history);
                assertTrue("no history found", history.size() >= 1);
                
                for (EntityRevision er : history) {

                    assertNotNull(er.getRevision());
                    assertNotNull(er.getEntity());
                    assertTrue("wrong class",  er.getEntity() instanceof Project);
                    Project p = (Project) er.getEntity();
                    
                    LOG.debug("Revision " + er.getRevision() + ": " + p.getName());
                }
                
            }
        });
        
    }

}
