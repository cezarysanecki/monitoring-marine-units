package pl.devcezz.barentswatch.backend.token;

public record RefreshingProcessed(boolean isValid, String email, String refreshToken) {
}
