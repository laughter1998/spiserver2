package org.zerock.spiserver2.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.spiserver2.dto.PageRequestDTO;
import org.zerock.spiserver2.dto.PageResponseDTO;
import org.zerock.spiserver2.dto.TodoDTO;
import org.zerock.spiserver2.service.TodoService;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno){

        return todoService.get(tno);
    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){

        log.info("List..............." + pageRequestDTO);

        return  todoService.getList(pageRequestDTO);

    }

    @PostMapping("/")
    public Map<String, Long> register( @RequestBody TodoDTO dto){

        log.info("todoDTO:" + dto);

        long tno = todoService.register(dto);

        return Map.of("TNO", tno);
    }
}
