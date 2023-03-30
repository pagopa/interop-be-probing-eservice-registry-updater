
package it.pagopa.interop.probing.eservice.registry.updater.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateEnum;
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

	@NotBlank(message = "must not be blank")
	@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "must respect the UUID regex")
	private String eserviceId;

	@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "must respect the UUID regex")
	@NotBlank(message = "must not be blank")
	private String versionId;

	@NotBlank(message = "must not be blank")
	@ValidateEnum(enumClass = EserviceTechnology.class)
	private String technology;

	@NotBlank(message = "must not be blank")
	@ValidateEnum(enumClass = EserviceState.class)
	private String state;

	@NotEmpty(message = "list cannot be empty")
	@ValidateStringArraySize(maxSize = 255)
	private String[] basePath;

	@NotBlank(message = "must not be blank")
	@Size(max = 255, message = "must not be longer than 255 chars")
	private String producerName;

	@NotBlank(message = "must not be blank")
	@Size(max = 255, message = "must not be longer than 255 chars")
	private String versionNumber;

}
