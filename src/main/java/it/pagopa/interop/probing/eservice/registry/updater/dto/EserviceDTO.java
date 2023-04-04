
package it.pagopa.interop.probing.eservice.registry.updater.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateStringArraySize;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class EserviceDTO {

	@NotBlank(message = "must not be blank")
	@Size(max = 255, message = "must not be longer than 255 chars")
	private String name;

	@NotNull(message = "must not be null")
	private UUID eserviceId;

	@NotNull(message = "must not be null")
	private UUID versionId;

	@NotNull(message = "must not be null")
	private EserviceTechnology technology;

	@NotNull(message = "must not be null")
	private EserviceState state;

	@NotEmpty(message = "list cannot be empty")
	@ValidateStringArraySize(maxSize = 2048)
	private String[] basePath;

	@NotBlank(message = "must not be blank")
	@Size(max = 255, message = "must not be longer than 255 chars")
	private String producerName;

	@NotBlank(message = "must not be blank")
	@Size(max = 255, message = "must not be longer than 255 chars")
	private String versionNumber;

}
