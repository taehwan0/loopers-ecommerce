package com.loopers.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Point {
	@Column(name = "pointValue", nullable = false)
	private int pointValue;

	protected Point() {}

	public Point(int pointValue) {
		this.pointValue = pointValue;
	}
}
