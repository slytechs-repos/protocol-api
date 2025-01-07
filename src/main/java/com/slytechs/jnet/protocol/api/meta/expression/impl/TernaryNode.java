/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.jnet.protocol.api.meta.expression.impl;

import java.util.function.Function;

/**
 * Represents a ternary conditional operation (condition ? trueExpr :
 * falseExpr).
 */
final class TernaryNode extends ExprNode {

	private final ExprNode condition;
	private final ExprNode trueExpr;
	private final ExprNode falseExpr;

	TernaryNode(ExprNode condition, ExprNode trueExpr, ExprNode falseExpr, int sourcePosition) {
		super(sourcePosition);

		if (condition == null || trueExpr == null || falseExpr == null) {
			throw new ExpressionException(
					String.format("Invalid ternary operation at position %d: missing expression",
							sourcePosition));
		}

		this.condition = condition;
		this.trueExpr = trueExpr;
		this.falseExpr = falseExpr;
	}

	@Override
	ExprValue evaluate(Function<String, Number> varResolver) {
		ExprValue condValue = evaluateChild(condition, varResolver);

		// Select and evaluate the appropriate branch
		return evaluateChild(
				condValue.asInt() != 0 ? trueExpr : falseExpr,
				varResolver);
	}

	@Override
	public String toString() {
		return String.format("(%s ? %s : %s)",
				condition.toString(),
				trueExpr.toString(),
				falseExpr.toString());
	}
}