package org.zerock.spiserver2.repository.search;

import org.springframework.data.domain.Page;
import org.zerock.spiserver2.domain.Todo;
import org.zerock.spiserver2.dto.PageRequestDTO;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
