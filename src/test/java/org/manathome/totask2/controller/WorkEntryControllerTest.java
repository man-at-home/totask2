package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.manathome.totask2.model.WorkEntryTransfer;
import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * testing APP/REST API Controller used by mobile app.
 * 
 * @author man-at-home
 * @since 2015-02-28
 */
public class WorkEntryControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(WorkEntryControllerTest.class);    

    /** testing APP/REST/workEntries/{day}. */
    @Test
    public void testRestWorkEntriesForUser() throws Exception {
        
        LocalDate dt = LocalDate.now(); 
        String day   = LocalDateConverter.isoFormat(dt);      

        LOG.debug("request APP/REST/workEntries/{day} with day: " + day);
        
        /* expecting json data in form of (array of workentry)
          [ {
              "id" : 1,
              "comment" : "entry 4.0 dev",
              "at" : "2015-02-28",
              "duration" : 4.0,
              "taskName" : "demo-develop",
              "taskId" : 4
            }
         */
        
        MvcResult result =
        this.mockMvc
            .perform(get("/APP/REST/workEntries/" + day)
            .with(user("unit-test-user")
            .roles("USER")))
        .andDo(print())                       // output system.out
        .andExpect(status().isOk())
        .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),                        
                Charset.forName("utf8")                     
                )))        
        .andExpect(content().string(containsString("\"id\"")))              // check required fields..
        .andExpect(content().string(containsString("\"at\"")))
        .andExpect(content().string(containsString("\"duration\"")))
        .andExpect(content().string(containsString("\"taskId\"")))
        .andExpect(content().string(containsString("\"taskName\"")))        // check required fields.
        .andReturn();
        
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        
        ObjectMapper objectMapper = new ObjectMapper();
        List<WorkEntryTransfer> entries = objectMapper.readValue(response, new TypeReference<List<WorkEntryTransfer>>() { });
        LOG.debug("deserialized " + entries.size() + " json objects in array");
        
        entries.stream().forEach(wet -> {
            
            LOG.debug("deserialized json: " + wet);
            
            assertNotNull(wet.getAt());
            assertTrue("id invalid " + wet.getId(), wet.getId() >= 0);
            assertTrue("taskId invalid " + wet.getTaskId(), wet.getTaskId() >= 0);
            assertTrue("taskname requires taskId " + wet.getTaskName(), wet.getTaskName() == null || wet.getTaskId() > 0);
            assertNotNull("taskname", wet.getTaskName());
            assertTrue("taskname not filled" + wet.getTaskName(), wet.getTaskName() == null || wet.getTaskName().length() > 2);
            assertTrue("duration invalid " + wet.getDuration() , wet.getDuration() >= 0);
        }
        );
    }
    

    /** testing APP/REST/workEntries. */
    @Test
    public void testRestWorkEntriesForUserToday() throws Exception {
        
        LocalDate dt = LocalDate.now(); 

        LOG.debug("request APP/REST/workEntries");
        
        
        MvcResult result =
        this.mockMvc
            .perform(get("/APP/REST/workEntries/")
            .with(user("unit-test-user")
            .roles("USER")))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"id\"")))              // check required fields..
        .andReturn();
        
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        
        ObjectMapper objectMapper = new ObjectMapper();
        List<WorkEntryTransfer> entries = objectMapper.readValue(response, new TypeReference<List<WorkEntryTransfer>>() { });
        LOG.debug("deserialized " + entries.size() + " json objects in array");
        
        entries.stream().forEach(wet -> {            
            LOG.debug("deserialized json: " + wet);            
            assertTrue("not today(" + dt + ") but " + wet.getAt(), dt.equals(LocalDateConverter.toLocalDate(wet.getAt())));
            assertTrue("id invalid " + wet.getId(), wet.getId() >= 0);
          }
        );
    }    
    
    
    
    
    /** testing APP/REST/workEntry POST (save workEntry). */
    @Test
    public void testRestWorkEntryUpdateForUser() throws Exception {
        
        LocalDate dt = LocalDate.now(); 

        LOG.debug("POST APP/REST/workEntry");
        
        WorkEntryTransfer wet = new WorkEntryTransfer();
        wet.setId(1);
        wet.setTaskId(4);
        wet.setAt(LocalDateConverter.toDate(dt));
        wet.setDuration(5);
        wet.setComment("by junit");
        
        MvcResult result =
        this.mockMvc
            .perform(post("/APP/REST/workEntry")
            .contentType(APPLICATION_JSON_UTF8)
            .content(convertObjectToJsonBytes(wet))              
            .with(user("unit-test-user")
            .roles("USER")))
        .andDo(print())                       // output system.out
        .andExpect(status().isOk())
        .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),                        
                Charset.forName("utf8")                     
                )))        
        .andExpect(content().string(containsString("\"id\"")))              // check required fields..
        .andReturn();
        
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        
        WorkEntryTransfer savedWet = new ObjectMapper().readValue(response, new TypeReference<WorkEntryTransfer>() { });
        LOG.debug("deserialized " + savedWet + " json object.");
        
        assertEquals("taskid", wet.getTaskId(), savedWet.getTaskId()); 
        assertEquals("duration", wet.getDuration(), savedWet.getDuration(), 0.01);
        assertNotNull("id", savedWet.getId());
    }
    
    
    
    /** testing APP/REST/workEntry POST (save workEntry). */
    @Test
    public void testRestWorkEntryInsertForUser() throws Exception {
        
        LocalDate dt = LocalDate.now(); 

        LOG.debug("POST APP/REST/workEntry");
        
        WorkEntryTransfer wet = new WorkEntryTransfer();
        // wet.setId(0);
        wet.setTaskId(6);
        wet.setAt(LocalDateConverter.toDate(dt));
        wet.setDuration(4.1f);
        wet.setComment("inserted 4.1h by junit on task 6");
        
        MvcResult result =
        this.mockMvc
            .perform(post("/APP/REST/workEntry")
            .contentType(APPLICATION_JSON_UTF8)
            .content(convertObjectToJsonBytes(wet))              
            .with(user("unit-test-user")
            .roles("USER")))
        .andDo(print())                       // output system.out
        .andExpect(status().isOk())
        .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),                        
                Charset.forName("utf8")                     
                )))        
        .andExpect(content().string(containsString("\"id\"")))              // check required fields..
        .andReturn();
        
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        
        WorkEntryTransfer savedWet = new ObjectMapper().readValue(response, new TypeReference<WorkEntryTransfer>() { });
        LOG.debug("deserialized new " + savedWet + " json object.");
        
        assertEquals("task.id", wet.getTaskId(), savedWet.getTaskId()); 
        assertEquals("duration", wet.getDuration(), savedWet.getDuration(), 0.01);
        assertEquals("comment", wet.getComment(), savedWet.getComment());
        assertNotNull("task.name", savedWet.getTaskName());
        assertNotNull("id", savedWet.getId());
        assertTrue("saved id is returned", savedWet.getId() > 0);
    }
  
    /** some constants. */
    static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    /** json conversion. */
    static byte[] convertObjectToJsonBytes(Object jsonObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LOG.debug("convertObject() to json:" + mapper.writeValueAsString(jsonObject));
        return mapper.writeValueAsBytes(jsonObject);
    }
}
