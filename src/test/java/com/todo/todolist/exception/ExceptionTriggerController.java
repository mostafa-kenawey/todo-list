package com.todo.todolist.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
class ExceptionTriggerController {

    @GetMapping("/bad-request")
    public void throwBadRequest() {
        throw new BadRequestException("Invalid request");
    }

    @GetMapping("/conflict")
    public void throwConflict() {
        throw new ConflictException("Conflict occurred");
    }

    @GetMapping("/forbidden")
    public void throwForbidden() {
        throw new ForbiddenException("Access denied");
    }

    @GetMapping("/not-found")
    public void throwNotFound() {
        throw new NotFoundException("Not Found");
    }

    @GetMapping("/general-error")
    public void throwGeneral() {
        throw new RuntimeException("Something went wrong");
    }
}
