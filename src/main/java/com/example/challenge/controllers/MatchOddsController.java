
package com.example.challenge.controllers;

import com.example.challenge.annotations.UUIDConstraint;
import com.example.challenge.controllers.dto.MatchOdds;
import com.example.challenge.controllers.mappers.MatchOddsMapperService;
import com.example.challenge.services.MatchOddsService;
import com.example.challenge.services.exceptions.InvalidMatchIdGiven;
import com.example.challenge.services.exceptions.MismatchedIdsException;
import com.example.challenge.services.exceptions.ResourceNotFoundException;
import com.example.challenge.services.models.MatchOddsModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

/**
 * While match odds endpoints still reside under matches (as match odds are subresources of matches),
 * a different controller is used to better manage the implementation
 */
@RestController
@RequestMapping(path = "matches")
@Validated
@Tag(name = "match-odds-controller", description = "Operations related to match odds")
public class MatchOddsController {
    private static final Logger logger = LoggerFactory.getLogger(MatchOddsController.class);

    private final MatchOddsService matchOddsService;
    private final MatchOddsMapperService matchOddsMapperService;

    public MatchOddsController(MatchOddsService matchOddsService, MatchOddsMapperService matchOddsMapperService) {
        this.matchOddsService = matchOddsService;
        this.matchOddsMapperService = matchOddsMapperService;
    }

    @Operation(summary = "Create a Match odd")
    @ApiResponses(value = { @ApiResponse(responseCode = "201",
                                         description = "Match odd created successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Malformed request or Validation for submitted properties failed",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input!"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @PostMapping("/{matchId}/odds")
    public ResponseEntity<MatchOdds> createMatchOdd (@UUIDConstraint @PathVariable String matchId, @Valid @RequestBody MatchOdds matchOdds) {
        UUID matchIdAsUuid = UUID.fromString(matchId);
        if (optionalPayloadIdNotMatchesUrlId(matchOdds.getMatchId(), matchIdAsUuid)) {
            throw new InvalidMatchIdGiven("Match id inside odds body does not match the match id given");
        }
        var matchOddsModel = matchOddsMapperService.mapToMatchModel(matchOdds);
        matchOddsModel.setMatchId(matchIdAsUuid);
        MatchOddsModel createdMatchOdd = matchOddsService.saveMatchOdd(matchOddsModel);

        return ResponseEntity
                .created(URI.create(String.format("/matches/%s/odds/%s", matchId, createdMatchOdd.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(matchOddsMapperService.mapToMatchOdd(createdMatchOdd));
    }

    @Operation(summary = "Get a Match Odd")
    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                                         description = "Match Odd retrieved successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Invalid UUID on request",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input!"))}),
            @ApiResponse(responseCode = "404",
                         description = "Match Odd does not exist or status is deleted",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Not found"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @GetMapping("/{matchId}/odds/{matchOddId}")
    public ResponseEntity<MatchOdds> getMatchOdd(@UUIDConstraint @PathVariable("matchId") String matchId,
                                                 @UUIDConstraint @PathVariable("matchOddId") String matchOddId) {
        UUID matchIdAsUuid = UUID.fromString(matchId);
        UUID matchOddIdAsUuid = UUID.fromString(matchOddId);

        MatchOddsModel result = matchOddsService.getMatchOdd(matchIdAsUuid, matchOddIdAsUuid);
        if (result == null) {
            throw new ResourceNotFoundException(String.format("Match odd not found with ID: %s", matchOddIdAsUuid));
        }

        return ResponseEntity.ok(matchOddsMapperService.mapToMatchOdd(result));
    }

    @Operation(summary = "Delete a Match odd")
    @ApiResponses(value = { @ApiResponse(responseCode = "200",
                                         description = "Match odd deleted successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Invalid UUID on request",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input!"))}),
            @ApiResponse(responseCode = "404",
                         description = "Match odd does not exist or status is deleted",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Not found"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @DeleteMapping("/{matchId}/odds/{matchOddId}")
    public ResponseEntity<MatchOdds> deleteMatchOdd(@UUIDConstraint @PathVariable("matchId") String matchId,
                                                    @UUIDConstraint @PathVariable("matchOddId") String matchOddId) {

        UUID matchIdAsUuid = UUID.fromString(matchId);
        UUID matchOddIdAsUuid = UUID.fromString(matchOddId);

        var result = matchOddsService.deleteMatchOdd(matchIdAsUuid, matchOddIdAsUuid);

        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(String.format("Match odd not found with ID: %s", matchOddIdAsUuid));
        }
    }

    @Operation(summary = "Update a Match odd")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
                                        description = "Match odd updated successfully"),
            @ApiResponse(responseCode = "400",
                         description = "Malformed request or Validation for submitted properties failed",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Invalid input"))}),
            @ApiResponse(responseCode = "404",
                         description = "Match odd does not exist",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Match does not exist"))}),
            @ApiResponse(responseCode = "500",
                         description = "Internal server error",
                         content = {@Content(mediaType = "text", schema = @Schema(example = "Something went wrong!"))})
    })
    @PutMapping("/{matchId}/odds/{matchOddId}")
    public ResponseEntity<MatchOdds> updateMatchOdd(@UUIDConstraint @PathVariable("matchId") String matchId,
                                                @UUIDConstraint @PathVariable("matchOddId") String matchOddId,
                                                @Valid @RequestBody MatchOdds matchOdds) {

        UUID matchIdAsUuid = UUID.fromString(matchId);
        UUID matchOddIdAsUuid = UUID.fromString(matchOddId);

        if (optionalPayloadIdNotMatchesUrlId(matchOdds.getId(), matchOddIdAsUuid)) {
            throw new MismatchedIdsException(matchOdds.getId().toString(), matchOddId);
        }
        matchOdds.setId(matchOddIdAsUuid);
        var matchModel = matchOddsMapperService.mapToMatchModel(matchOdds);

        MatchOddsModel createdMatchOdd = matchOddsService.updateMatchOdd(matchModel, matchIdAsUuid);

        return ResponseEntity.ok(matchOddsMapperService.mapToMatchOdd(createdMatchOdd));
    }

    private static boolean optionalPayloadIdNotMatchesUrlId(UUID payloadId, UUID urlId) {
        return payloadId != null && !urlId.equals(payloadId);
    }
}
