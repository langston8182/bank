package com.cmarchive.bank.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.exceptions.OperationNotFoundException;
import com.cmarchive.bank.models.examples.OperationExample;
import com.cmarchive.bank.models.examples.PermanentOperationExample;
import com.cmarchive.bank.models.examples.UserExample;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "/data.sql", config = @SqlConfig(transactionMode = TransactionMode.ISOLATED))
public class OperationServiceTest {

    private OperationService operationService;
    private UserService userService;
    private TypeOperationService typeOperationService;
    private PermanentOperationService permanentOperationService;

    @Autowired
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTypeOperationService(TypeOperationService typeOperationService) {
        this.typeOperationService = typeOperationService;
    }

    @Autowired
    public void setPermanentOperationService(PermanentOperationService permanentOperationService) {
        this.permanentOperationService = permanentOperationService;
    }

    @Test
    public void list() {
        Operation operation = OperationExample.aOperation().get();
        operationService.save(operation);
        
        List<Operation> operations = operationService.list();
        assertThat(operations).isNotEmpty();
    }
    
    @Test
    public void get() {
        Operation operation = OperationExample.aOperation().get();
        Operation savedOperation = operationService.save(operation);
        
        Operation expectedOperation = operationService.get(savedOperation.getId());
        assertThat(expectedOperation.getDateOperation()).isEqualByComparingTo(savedOperation.getDateOperation());
        assertThat(expectedOperation.getId()).isEqualByComparingTo(savedOperation.getId());
        assertThat(expectedOperation.getIntitule()).isEqualTo(savedOperation.getIntitule());
        assertThat(expectedOperation.getPrix()).isEqualByComparingTo(savedOperation.getPrix());
    }
    
    @Test
    public void save() {
        Operation operation = OperationExample.aOperation().get();
        assertThat(operation.getId()).isNull();
        
        Operation savedOperation = operationService.save(operation);
        
        Operation expectedOperation = operationService.get(savedOperation.getId());
        assertThat(expectedOperation.getDateOperation()).isEqualByComparingTo(savedOperation.getDateOperation());
        assertThat(expectedOperation.getId()).isEqualByComparingTo(savedOperation.getId());
        assertThat(expectedOperation.getIntitule()).isEqualTo(savedOperation.getIntitule());
        assertThat(expectedOperation.getPrix()).isEqualByComparingTo(savedOperation.getPrix());
    }
    
    @Test(expected = OperationNotFoundException.class)
    public void delete() {
        Operation operation = OperationExample.aOperation().get();
        assertThat(operation.getId()).isNull();
        
        Operation savedOperation = operationService.save(operation);
        
        Operation expectedOperation = operationService.get(savedOperation.getId());
        assertThat(expectedOperation.getDateOperation()).isEqualByComparingTo(savedOperation.getDateOperation());
        assertThat(expectedOperation.getId()).isEqualByComparingTo(savedOperation.getId());
        assertThat(expectedOperation.getIntitule()).isEqualTo(savedOperation.getIntitule());
        assertThat(expectedOperation.getPrix()).isEqualByComparingTo(savedOperation.getPrix());
        
        operationService.delete(savedOperation.getId());
        operationService.get(savedOperation.getId());
    }
    
    @Test
    public void findByUser() {
        User user = UserExample.aUser().get();
        User savedUser = userService.save(user);
        Operation operation1 = OperationExample.aOperation().get();
        Operation operation2 = OperationExample.aOperation().get();
        
        operation1.setUser(user);
        operation2.setUser(user);
        operationService.save(operation1);
        operationService.save(operation2);
        
        List<Operation> operations = operationService.findByUser(savedUser.getId());
        assertThat(operations.size()).isEqualByComparingTo(2);
    }
    
    @Test
    public void addOpPermanenteToOperations() {
        User user = UserExample.aUser().get();
        User savedUser = userService.save(user);
        
        assertThat(user.getOperations()).isEmpty();
        
        PermanentOperation permanentOperation = PermanentOperationExample.aPermanentOperation().get();
        permanentOperation.setUser(user);
        permanentOperation.setTypeOperation(typeOperationService.getCredit());

        operationService.addOpPermanenteToOperations(1, permanentOperation);
        
        User expectedUser = userService.get(savedUser.getId());
        assertThat(expectedUser.getOperations().size()).isEqualByComparingTo(1);
        
        LocalDate localDate = LocalDate.of(Year.now().getValue(), 1, permanentOperation.getJour());
        
        Operation expectedOperation = expectedUser.getOperations().get(0);
        assertThat(expectedOperation.getIntitule()).isEqualTo(permanentOperation.getIntitule());
        assertThat(expectedOperation.getDateOperation()).isEqualByComparingTo(localDate);
        assertThat(expectedOperation.getPrix()).isEqualByComparingTo(permanentOperation.getPrix());
        assertThat(expectedOperation.getTypeOperation().getValue()).isEqualTo(permanentOperation.getTypeOperation().getValue());
    }
    
    public void findByMonth() {
        
    }
}
