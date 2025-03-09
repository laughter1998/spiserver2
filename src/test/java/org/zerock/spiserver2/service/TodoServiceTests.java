package org.zerock.spiserver2.service;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.spiserver2.dto.TodoDTO;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    public void testGet(){
        Long tno = 50L;

        log.info(todoService.get(tno));
    }

    @Test
    public void testRegister(){

        TodoDTO todoDto = TodoDTO.builder()
                .title("Tilte...")
                .content("Conent,,,,,")
                .dueDate(LocalDate.of(2025,3,9))
                .build();

        log.info(todoService.register(todoDto));
    }
}
