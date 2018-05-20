package com.cmarchive.bank.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
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
        Long userId = 1L;
        Long permanentOperationId = 1L;
        
    	User user = userService.get(userId);
        Operation operation = OperationExample.aOperation()
                .with(o -> o.setUser(user))
                .with(o -> o.setPermanentOperation(permanentOperationService.get(permanentOperationId))).get();
        assertThat(operation.getId()).isNull();
        
        Operation savedOperation = operationService.save(operation);
        
        Operation expectedOperation = operationService.get(savedOperation.getId());
        assertThat(expectedOperation.getDateOperation()).isEqualByComparingTo(savedOperation.getDateOperation());
        assertThat(expectedOperation.getId()).isEqualByComparingTo(savedOperation.getId());
        assertThat(expectedOperation.getIntitule()).isEqualTo(savedOperation.getIntitule());
        assertThat(expectedOperation.getPrix()).isEqualByComparingTo(savedOperation.getPrix());
        
        operationService.delete(savedOperation.getId());
        
        PermanentOperation expectedPermanentOperation = permanentOperationService.get(permanentOperationId);
        assertThat(expectedPermanentOperation).isNotNull();
        
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
        permanentOperationService.save(permanentOperation);

        operationService.addOpPermanenteToOperations(1, permanentOperation);
        
        User expectedUser = userService.get(savedUser.getId());
        assertThat(expectedUser.getOperations().size()).isEqualByComparingTo(1);
        
        LocalDate localDate = LocalDate.of(Year.now().getValue(), 1, permanentOperation.getJour());
        
        Operation expectedOperation = expectedUser.getOperations().get(0);
        assertThat(expectedOperation.getIntitule()).isEqualTo(permanentOperation.getIntitule());
        assertThat(expectedOperation.getDateOperation()).isEqualByComparingTo(localDate);
        assertThat(expectedOperation.getPrix()).isEqualByComparingTo(permanentOperation.getPrix());
        assertThat(expectedOperation.getTypeOperation().getValueType()).isEqualTo(permanentOperation.getTypeOperation().getValueType());
    }
    
    @Test
    public void findByMonth() {
        long userId = 1L;
        User user = userService.get(userId);
        int month = Month.NOVEMBER.getValue();
        
        List<Operation> expectedOperations = operationService.findByMonth(userId, month);
        assertThat(expectedOperations).isEmpty();
        
        LocalDate localDateOp1 = LocalDate.now().withMonth(month).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate localDateOp2 = localDateOp1.plusDays(1);
        LocalDate localDateOp3 = localDateOp1.plusMonths(1);
        
        Operation operation1 = OperationExample.aOperation().with(o -> o.setDateOperation(localDateOp1))
                .with(o -> o.setUser(user)).get();
        Operation operation2 = OperationExample.aOperation().with(o -> o.setDateOperation(localDateOp2))
                .with(o -> o.setUser(user)).get();
        Operation operation3 = OperationExample.aOperation().with(o -> o.setDateOperation(localDateOp3))
                .with(o -> o.setUser(user)).get();
        
        operationService.save(operation1);
        operationService.save(operation2);
        operationService.save(operation3);
        
        expectedOperations = operationService.findByMonth(userId, month);
        assertThat(expectedOperations).hasSize(2);
    }
}
