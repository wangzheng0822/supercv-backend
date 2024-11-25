package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.user.domain.OkTest;
import com.xzgedu.supercv.user.service.OkTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/oktest")
@Tag(name = "oktest", description = "API test")
public class OkTestController {

    @Autowired
    private OkTestService okTestService;

    @Operation(summary = "Ping the server", description = "A test endpoint to check the server health.")
    @GetMapping("/ping")
    public String ping() {
        log.info("ping...");
        return "pong";
    }

    @Operation(summary = "Fetch OkTest by ID", description = "Retrieve a record from the database by its ID.")
    @GetMapping("/select")
    public OkTest selectOkTest(
            @Parameter(description = "ID of the record to fetch", required = true) @RequestParam("id") long id) {
        return okTestService.selectOkTest(id);
    }

    @Operation(summary = "Insert a new record", description = "Add a new record to the database with the provided name.")

    @PostMapping("/insert")
    public boolean insertOkTest(
            @Parameter(description = "Name of the record to insert", required = true) @RequestParam("name") String name) {
        OkTest okTest = new OkTest();
        okTest.setName(name);
        return okTestService.insertOkTest(okTest);
    }

    @Operation(summary = "Delete a record by ID", description = "Remove a record from the database by its ID.")
    @PostMapping("/delete")
    public boolean deleteOkTest(@RequestParam("id") long id) {
        return okTestService.deleteOkTest(id);
    }

    @Operation(summary = "Update a record by ID", description = "Update an existing record in the database by its ID.")
    @PostMapping("/update")
    public boolean updateOkTest(@RequestParam("id") long id, @RequestParam("name") String name) {
        OkTest okTest = new OkTest();
        okTest.setId(id);
        okTest.setName(name);
        return okTestService.updateOkTest(okTest);
    }
}