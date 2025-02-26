package changhyeon.mybudgetcommunity.service;

import changhyeon.mybudgetcommunity.dto.TodoDto;
import changhyeon.mybudgetcommunity.entity.Todo;
import changhyeon.mybudgetcommunity.entity.User;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;
import changhyeon.mybudgetcommunity.repository.TodoRepository;
import changhyeon.mybudgetcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public List<TodoDto> getTodosByDate(Long userId, LocalDate date) {
        return todoRepository.findByUserIdAndDateOrderByCreatedAtAsc(userId, date)
                .stream()
                .map(TodoDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoDto createTodo(Long userId, TodoDto.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Todo todo = Todo.builder()
                .user(user)
                .title(request.getTitle())
                .date(request.getDate())
                .amount(request.getAmount())
                .build();

        return TodoDto.from(todoRepository.save(todo));
    }

    @Transactional
    public TodoDto updateTodo(Long userId, Long todoId, TodoDto.UpdateRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        if (!todo.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_TODO_OWNER);
        }

        todo.update(request.getTitle(), request.getDate(), request.getAmount());
        return TodoDto.from(todo);
    }

    @Transactional
    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        if (!todo.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_TODO_OWNER);
        }

        todoRepository.delete(todo);
    }
}