package corewar.pit;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class PitResult<T> {

	@Nullable
	private final T result;
	@Nullable
	private final List<PitError> errors;
	@Nullable
	private final List<PitWarning> warnings;

	private PitResult(@Nullable T result, @Nullable Collection<PitError> errors, @Nullable Collection<PitWarning> warnings) {
		this.result = result;
		this.errors = errors == null ? null : new ArrayList<>(errors);
		this.warnings = warnings == null ? null : new ArrayList<>(warnings);
	}

	public static <T> PitResult<T> create(@Nullable T result, @Nullable PitError error) {
		return create(result, error, null);
	}

	public static <T> PitResult<T> create(@Nullable PitError error, @Nullable PitWarning warning) {
		return create(null, error, warning);
	}

	public static <T> PitResult<T> create(@Nullable T result, @Nullable PitError error, @Nullable PitWarning warning) {
		Collection<PitError> errors = error == null ? null : Collections.nCopies(1, error);
		Collection<PitWarning> warnings = warning == null ? null : Collections.nCopies(1, warning);
		return create(result, errors, warnings);
	}

	public static <T> PitResult<T> create(@Nullable T result, @Nullable Collection<PitError> errors, @Nullable Collection<PitWarning> warnings) {
		return new PitResult<>(result, errors, warnings);
	}

	public T getResult() {
		if(result == null) {
			throw new IllegalStateException("There is no result");
		}

		return result;
	}

	public boolean hasErrors() {
		return errors != null && !errors.isEmpty();
	}

	public boolean hasWarnings() {
		return warnings != null && !warnings.isEmpty();
	}

	public List<PitError> getErrors() {
		return errors == null ? Collections.emptyList() : errors;
	}

	public List<PitWarning> getWarnings() {
		return warnings == null ? Collections.emptyList() : warnings;
	}
}
