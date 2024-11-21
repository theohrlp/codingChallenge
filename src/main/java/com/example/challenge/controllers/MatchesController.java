package com.example.challenge.controllers;

import com.example.challenge.annotations.UUIDConstraint;
import com.example.challenge.controllers.dto.Match;
import com.example.challenge.controllers.mappers.MatchMapperService;
import com.example.challenge.services.MatchService;
import com.example.challenge.services.exceptions.MismatchedIdsException;
import com.example.challenge.services.exceptions.ResourceNotFoundException;
import com.example.challenge.services.models.MatchModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "matches")
@Validated
@Tag(name = "matches-controller", description = "Operations related to matches")
public class MatchesController {
    private static final Logger logger = LoggerFactory.getLogger(MatchesController.class);

    private final MatchService matchService;
    private final MatchMapperService matchMapperService;

    public MatchesController(MatchService matchService, MatchMapperService matchMapperService) {
        this.matchService = matchService;
        this.matchMapperService = matchMapperService;
    }

    @Operation(summary = "Create a Match")
    @ApiResponses(value = { @ApiResponse(responseCode = "201",
                                         description = "Match created successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Malformed request or Validation for submitted properties failed",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input!"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @PostMapping
    public ResponseEntity<Match> createMatch (@Valid @RequestBody Match match) {
        var matchModel = matchMapperService.mapToMatchModel(match);

        MatchModel createdMatch = matchService.saveMatch(matchModel);

        logger.info("Match: {} created", createdMatch.getId());
        return ResponseEntity
                .created(URI.create((String.format("/matches/" + createdMatch.getId()))))
                .contentType(MediaType.APPLICATION_JSON)
                .body(matchMapperService.mapToMatch(createdMatch));
    }

    @Operation(summary = "Get a Match")
    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                                         description = "Match retrieved successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Invalid UUID on request",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input!"))}),
            @ApiResponse(responseCode = "404",
                         description = "Match does not exist or status is deleted",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Not found"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatch(@UUIDConstraint @PathVariable("id") String idAsString) {
        UUID idAsUuid = UUID.fromString(idAsString);

        Optional<MatchModel> matchModel = matchService.getMatch(idAsUuid);

        if (matchModel.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Match not found with ID: %s", idAsUuid));
        }

        return ResponseEntity.ok(matchMapperService.mapToMatch(matchModel.get()));
    }

    @Operation(summary = "Delete a Match")
    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                                         description = "Match deleted successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Invalid UUID on request",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input!"))}),
            @ApiResponse(responseCode = "404",
                         description = "Match does not exist or status is deleted",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Not found"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Match> deleteMatch(@UUIDConstraint @PathVariable("id") String idAsString) {
        UUID idAsUuid = UUID.fromString(idAsString);

        var result = matchService.deleteMatch(idAsUuid);

        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(String.format("Match not found with ID: %s", idAsUuid));
        }
    }

    @Operation(summary = "Update a Match")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
                                        description = "Match updated successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Malformed request or Validation for submitted properties failed",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input"))}),
            @ApiResponse(responseCode = "404",
                         description = "Match does not exist",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Match does not exist"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@UUIDConstraint @PathVariable("id") String idAsString,@Valid @RequestBody Match match) {
        UUID idAsUuid = UUID.fromString(idAsString);
        if (optionalPayloadIdNotMatchesUrlId(match.getId(), idAsUuid)) {
            throw new MismatchedIdsException(match.getId().toString(), idAsUuid.toString());
        }
        match.setId(idAsUuid);
        var matchModel = matchMapperService.mapToMatchModel(match);

        MatchModel createdMatch = matchService.updateMatch(matchModel);

        return ResponseEntity.ok(matchMapperService.mapToMatch(createdMatch));
    }

    private static boolean optionalPayloadIdNotMatchesUrlId(UUID payloadId, UUID urlId) {
        return payloadId != null && !urlId.equals(payloadId);
    }

    // TODO handle exceptions
    // TODO handle when a wrong enum is given
    // TODO add profiles in maven
}
