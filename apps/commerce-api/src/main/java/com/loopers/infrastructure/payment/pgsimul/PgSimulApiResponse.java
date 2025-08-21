package com.loopers.infrastructure.payment.pgsimul;

public record PgSimulApiResponse<T>(Metadata meta, T data) {
	public record Metadata(Result result, String errorCode, String message) {
		public enum Result {
			SUCCESS, FAIL
		}
	}
}
