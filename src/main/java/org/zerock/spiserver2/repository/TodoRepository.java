package org.zerock.spiserver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.spiserver2.domain.Todo;

public interface TodoRepository  extends JpaRepository<Todo,Long> {
}
