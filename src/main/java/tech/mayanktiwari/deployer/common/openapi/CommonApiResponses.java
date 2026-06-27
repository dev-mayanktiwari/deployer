package tech.mayanktiwari.deployer.common.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tech.mayanktiwari.deployer.common.response.ApiError;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(
      responseCode = "401",
      description = "Missing or invalid JWT",
      content = @Content(schema = @Schema(implementation = ApiError.class))),
  @ApiResponse(
      responseCode = "403",
      description = "Access denied",
      content = @Content(schema = @Schema(implementation = ApiError.class))),
  @ApiResponse(
      responseCode = "500",
      description = "Internal server error",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
})
public @interface CommonApiResponses {}
