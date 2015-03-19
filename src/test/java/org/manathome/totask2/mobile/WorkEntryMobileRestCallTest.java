package org.manathome.totask2.mobile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.model.WorkEntry;
import org.manathome.totask2.model.WorkEntryRepository;
import org.manathome.totask2.model.WorkEntryTransfer;
import org.manathome.totask2.util.LocalDateConverter;
import org.manathome.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * testing mobile access via real http requests. 
 * 
 * this unit test will start the actual web application on port 8080 (see WebIntegrationTest) and 
 * test some REST endpoints provided by WorkEntryController via http.
 * 
 * @see    org.manathome.totask2.controller.WorkEntryController
 * @author man-at-home
 * */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class WorkEntryMobileRestCallTest {
  
    private static final Logger LOG = LoggerFactory.getLogger(WorkEntryMobileRestCallTest.class);
    
    @Autowired          private WorkEntryRepository workEntryRepository;
    @PersistenceContext private EntityManager       entityManager;

        
    private String baseUrl = "http://localhost:8080/APP/REST/";

    /** testing data retrieval. REST GET workEntries. */
    @Test
    public void testRetrieveWorkEntriesViaRest() {
        RestTemplate restTemplate = new TestRestTemplate(TestConstants.TEST_USER_NAME, TestConstants.TEST_USER_PW);
        String url = baseUrl + "workEntries";
        LOG.trace("calling out: " + url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertThat(response, is(notNullValue()));
        assertThat("http ok expected", response.getStatusCode(), is(HttpStatus.OK));
        
        LOG.trace("response...: " + response.getBody());
        assertThat("no body", response.getBody(), is(notNullValue()));
        assertThat("not enough data returned", response.getBody().length(), is(greaterThan(10)));
    }
    
    
    /** testing data retrieval. REST GET workEntries/yyyy-mm-dd. compare results against database. */
    @Test
    @Transactional
    public void testRetrieveJsonWorkEntriesTomorrowViaRest() {
 
        RestTemplate restTemplate = new TestRestTemplate(TestConstants.TEST_USER_NAME, TestConstants.TEST_USER_PW);
        LocalDate day   = LocalDate.now().plusDays(1);      
        
        String url = baseUrl + "workEntries/" + LocalDateConverter.isoFormat(day);
        LOG.trace("calling out: " + url);
        ResponseEntity<WorkEntryTransfer[]> response = restTemplate.getForEntity(url, WorkEntryTransfer[].class);
        assertThat("http ok expected", response.getStatusCode(), is(HttpStatus.OK));   
        assertThat("workentries returned", response.getBody().length, is(greaterThan(0)));
        
        Arrays.stream(response.getBody()).forEach(wet -> LOG.debug("returned json entry: " + wet.toString()));
        int found = 0;
        for (WorkEntryTransfer wet : response.getBody()) {
            assertThat("has task", wet.getTaskId() , is(greaterThan(0L)));
            assertThat("has task", wet.getTaskName(), not(isEmptyString()));
            assertThat("of requested day", LocalDateConverter.toLocalDate(wet.getAt()), is(day));
            
            // check against actual data
            if (wet.getId() > 0) {
                found++;
                LOG.debug("found persistet workentry was returned " + found);
                WorkEntry we = workEntryRepository.getOne(wet.getId());
                assertThat(wet.getId(), is(we.getId()));
                assertThat(LocalDateConverter.toLocalDate(wet.getAt()), is(we.getAtDate()));
                assertThat(wet.getComment(), is(we.getComment()));
                assertThat(wet.getDuration(), is(we.getDuration()));
                assertThat(wet.getTaskId(), is(we.getTaskId()));
            }
        }
        assertThat("not at least on persistet workentry found", found, is(greaterThan(0)));
    }
    
    
    /** testing data retrieval. REST GET workEntries/yyyy-mm-dd. compare results against database. */
    @Test
    @Transactional
    public void testUpdateJsonWorkEntriesViaRest() {
        
        WorkEntry we = workEntryRepository.findOne(TestConstants.TEST_WORKENTRY);
        assertNotNull(we);
        
        WorkEntryTransfer wet = we.asTransfer();
        wet.setComment("*junit-changed-comment*");
        wet.setDuration(6.6f);
        
        RestTemplate restTemplate = new TestRestTemplate(TestConstants.TEST_USER_NAME, TestConstants.TEST_USER_PW);
        String url = baseUrl + "workEntry";
        LOG.trace("calling out post: " + url + " with " + wet);
        ResponseEntity<WorkEntryTransfer> response = restTemplate.postForEntity(url, wet, WorkEntryTransfer.class);
        assertThat("http ok expected", response.getStatusCode(), is(HttpStatus.OK));   
        assertThat("workentry returned", response.getBody(), notNullValue());
        assertThat("new workentry returned", response.getBody().getDuration(), is(wet.getDuration()));
        
        entityManager.refresh(we);
        LOG.debug("reloaded the updated entry: " + we);
        
        assertThat(we.getDuration(), is(wet.getDuration())); // changes were persisted
        assertThat(we.getComment(), is(wet.getComment()));
    }
    
    /** testing data retrieval. REST GET workEntries/yyyy-mm-dd. compare results against database. */
    @Test
    @Transactional
    public void testInsertJsonWorkEntriesViaRest() {
 
         List<Long> oldIds = workEntryRepository
                .findForTask(TestConstants.TEST_TASK)
                 .stream()
                .map(wet -> wet.getId())
                .collect(Collectors.toList());
         
        LOG.debug("found " + oldIds.size() + " entries for task " + TestConstants.TEST_TASK);

        LocalDate day   = LocalDate.now().plusDays(1);      
               
        WorkEntryTransfer wet = new WorkEntryTransfer();
        wet.setComment("*junit-inserted-comment*");
        wet.setDuration(4.9f);
        wet.setTaskId(TestConstants.TEST_TASK);
        wet.setTaskName("dummy");
        wet.setAt(LocalDateConverter.toDate(day));
        wet.setId(0);
        wet.check();
        
        RestTemplate restTemplate = new TestRestTemplate(TestConstants.TEST_USER_NAME, TestConstants.TEST_USER_PW);
        String url = baseUrl + "workEntry";
        LOG.trace("calling out post: " + url + " with new " + wet);
        ResponseEntity<WorkEntryTransfer> response = restTemplate.postForEntity(url, wet, WorkEntryTransfer.class);
        assertThat("http ok expected", response.getStatusCode(), is(HttpStatus.OK));   
        
        WorkEntry we = workEntryRepository.findOne(response.getBody().getId());
        assertNotNull(we);        
        assertThat("duration 4.9h: " + we.getDuration(), we.getDuration(), is(wet.getDuration())); // insert were persisted
        assertThat(we.getComment(), is(wet.getComment()));
        assertThat(we.getTaskId(), is(TestConstants.TEST_TASK));
        

        assertThat("not a new workentry", oldIds, not(hasItem(we.getTaskId()))); // an actual new workentry
        
        entityManager.flush();
        
        assertTrue("new entry found for task: " + response.getBody() ,
        workEntryRepository
        .findForTask(TestConstants.TEST_TASK)
        .stream()
        .anyMatch(lwe -> lwe.getId() == response.getBody().getId())
        );
        
    }
}
