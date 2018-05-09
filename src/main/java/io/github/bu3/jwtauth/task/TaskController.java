package io.github.bu3.jwtauth.task;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();

    @ResponseStatus(value = CREATED)
    @PostMapping
    public Task saveTask(@RequestBody Task task){
        tasks.add(task);
        task.setId(UUID.randomUUID().toString());
        return task;
    }

    @GetMapping
    public List<Task> retrieveTasks(){
        return tasks;
    }
}
